#!/bin/bash

# Sprawdź, czy Python jest zainstalowany
if ! command -v python &> /dev/null
then
    echo "Python could not be found. Please install Python to proceed."
    exit
fi

# Sprawdź, czy PyYAML jest zainstalowany
python -c "import yaml" 2>/dev/null
if [ $? -ne 0 ]; then
    echo "PyYAML is not installed. Installing now..."
    pip install pyyaml
    if [ $? -ne 0 ]; then
        echo "Failed to install PyYAML. Please install it manually with 'pip install pyyaml'."
        exit 1
    fi
fi

# Ustaw domyślną wartość dla DEPLOY_ENV
DEPLOY_ENV="test"
PUSH_IMAGE=false

# Parsowanie argumentów
for arg in "$@"
do
    case $arg in
        production)
        DEPLOY_ENV="production"
        shift # Usuń argument z listy
        ;;
        --push)
        PUSH_IMAGE=true
        shift # Usuń argument z listy
        ;;
        *)
        echo "Unknown argument: $arg"
        echo "Usage: $0 [test|production] [--push]"
        exit 1
        ;;
    esac
done

# Wczytaj nazwę projektu z application.yml za pomocą Python
PROJECT_NAME=$(python -c "
import yaml
with open('src/main/resources/application.yml', 'r') as stream:
    try:
        config = yaml.safe_load(stream)
        print(config['spring']['application']['name'])
    except yaml.YAMLError as exc:
        print(exc)
")

# Usuń ewentualne białe znaki
PROJECT_NAME=$(echo "$PROJECT_NAME" | xargs)

# Sprawdź, czy PROJECT_NAME jest pusty i ustaw domyślną wartość
if [ -z "$PROJECT_NAME" ]; then
  PROJECT_NAME="default-name"
fi

# Sprawdź zawartość PROJECT_NAME
echo "PROJECT_NAME: $PROJECT_NAME"

# Uzyskaj aktualną datę w formacie dzień-miesiąc-rok-godzina-minuta-sekunda
DATE=$(date +"%d-%m-%Y-%H-%M-%S")

# Generuj tag obrazu
IMAGE_TAG="${PROJECT_NAME}-${DEPLOY_ENV}:${DATE}"

# Sprawdź zawartość IMAGE_TAG
echo "IMAGE_TAG: $IMAGE_TAG"

# Zbuduj obraz Dockerowy z użyciem wygenerowanego tagu
docker build --build-arg PROJECT_NAME=${PROJECT_NAME} -t "${IMAGE_TAG}" .

echo "BUDOWANIE UKOŃCZONE"
# Wypchnij obraz, jeśli flaga --push jest podana
if [ "$PUSH_IMAGE" = true ]; then
    echo "PUSHOWANIE OBRAZU:"
    # Tagowanie obrazu dla Docker Hub
    FINAL_TAG="sandrzejczak/${PROJECT_NAME}:${DATE}-${DEPLOY_ENV}"
    docker tag "${IMAGE_TAG}" "${FINAL_TAG}"

    # Wypchnięcie obrazu do Docker Hub
    docker push "${FINAL_TAG}"
    echo "PUSHOWANIE OBRAZU UKOŃCZONE"
fi
