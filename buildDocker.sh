#!/bin/bash

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

# Wczytaj nazwę projektu z application.properties za pomocą grep i cut
PROJECT_NAME=$(grep -E '^spring\.application\.name=' src/main/resources/application.properties | cut -d '=' -f 2)

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
docker build --build-arg PROJECT_NAME=${PROJECT_NAME} --build-arg BUILD_DATE=${DATE} -t "${IMAGE_TAG}" .

echo "BUDOWANIE UKOŃCZONE"
if [ "$PUSH_IMAGE" = true ]; then # Wypchnij obraz, jeśli flaga --push jest podana
    echo "PUSHOWANIE OBRAZU:"
    # Tagowanie obrazu dla Docker Hub
    FINAL_TAG="sandrzejczak/${PROJECT_NAME}:${DATE}-${DEPLOY_ENV}"
    docker tag "${IMAGE_TAG}" "${FINAL_TAG}"

    # Wypchnięcie obrazu do Docker Hub
    docker push "${FINAL_TAG}"
    echo "PUSHOWANIE OBRAZU UKOŃCZONE"
fi
