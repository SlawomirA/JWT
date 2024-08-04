## Za co odpowiada aplikacja
### JWT

- Generowanie JWT
- Deszyfrowanie JWT 
- Odnawianie JWT refresh tokenem

### Security
- Tworzenie konta
- Autoryzacja (logowanie)
- Logout
- Usuwanie konta
- Nadawanie ról
- Odbieranie ról

### Auditing
Logowane są następujące dane:

- Kto i kiedy stworzył oraz zmodyfikował dany rekord 
    - > @CreationTimestamp
    - > @UpdateTimestamp
    - > @CreatedBy
    - > @LastModifiedBy
-

## Generowanie klucza do application.yml
node -e "console.log(require('crypto').randomBytes(32).toString('hex'))"


## Budowanie aplikacji
pip install pyyaml
./buildDocker.sh