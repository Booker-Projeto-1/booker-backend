# Booker Backend
Repositório responsável pelo código do backend da aplicação Booker

## Getting Started

### Dependencies

* Describe any prerequisites, libraries, OS version, etc., needed before installing program.
* Java 17
* IntelliJ/Eclipse/IDE da sua escolha
* PostgreSQL (futuramente)

### Installing

* Instale o Java 17 na sua máquina:

```
apt install openjdk-17-jdk openjdk-17-jre
```
* Clone o repositorio
```
git clone git@github.com:Booker-Projeto-1/booker-backend.git
```

* Importe o projeto na sua IDE de preferência

### Executing program

* Abra o Terminal na pasta do projeto (nesse caso o projeto é o booker e não o booker-backend - nome do repositório)
* Execute o comando a seguir:
```
./gradlew bootrun
```

* Abra o navegador em http://localhost:8080/ (Hello World) ou em http://localhost:8080/h2-console para ver o Banco de Dados
* As informações de acesso ao Banco de Dados estão no arquivo application.properties
