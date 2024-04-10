# Documentação da API SBO - Skyward Bridge Operations

## Visão Geral

Este README fornece informações sobre como interagir com a API da Ponte Basculante (SBO - Skyward Bridge Operations). A API permite operações CRUD (Create, Read, Update, Delete) em pontes basculantes, bem como movimentação das mesmas.

## Autenticação

Todos os endpoints da API requerem autenticação por meio de um token JWT (JSON Web Token). Você pode obter um token fazendo uma requisição POST para o endpoint de autenticação: POST /api/authenticate

### Papéis de Usuário

- **ADMIN**: Permite acessar endpoints de gerenciamento de pontes (CRUD) e movimentação (/move).
- **OPERATOR**: Permite acessar apenas o endpoint de movimentação (/move).

## Estados da Ponte

A ponte basculante possui três campos principais que descrevem seu estado:

- **position**: Indica a posição atual da ponte na plataforma.
    - `BASE`: Na base da plataforma (no chão).
    - `MIDDLE`: Em algum local no meio do percurso de elevação ou descida.
    - `HIGHEST`: No ponto mais alto.

- **status**: Indica se a ponte está parada ou em movimento.
    - `STOPPED`: Parada.
    - `MOVING`: Em movimento.

- **direction**: Indica a direção que a ponte está apontando.
    - `UP`: Próximo movimento será subir.
    - `DOWN`: Próximo movimento será descer.

## Máquina de Estados da Ponte

A ponte segue uma máquina de estados conforme descrito abaixo:

1. **Estado Inicial**: BASE, STOPPED, UP
    - Endpoint `/move` acionado: Ponte vai para MIDDLE, MOVING, UP
2. **Elevação Completa**: HIGHEST, STOPPED, DOWN (após tempo de movimentação)
    - Se `/move` for acionado antes do tempo de movimentação terminar: MIDDLE, STOPPED, DOWN
3. **Descida**: MIDDLE, MOVING, DOWN
4. **Retorno à Base**: BASE, STOPPED, UP (após tempo de movimentação)
    - Caso a ponte esteja em HIGHEST e `/move` for acionado: MIDDLE, MOVING, DOWN, e em seguida, MIDDLE, MOVING, UP.

## Limitação de Operação

Se a ponte estiver no ponto mais alto da elevação, será necessário aguardar 1 minuto para movimentá-la novamente. Nesse caso, uma exceção com o código 422 será retornada: "Operation blocked - please wait 1 minute to operate". 

-----------------------------------
## Jhipster documentation
## Project Structure

Node is required for generation and recommended for development. `package.json` is always generated for a better development experience with prettier, commit hooks, scripts and so on.

In the project root, JHipster generates configuration files for tools like git, prettier, eslint, husky, and others that are well known and you can find references in the web.

`/src/*` structure follows default Java structure.

- `.yo-rc.json` - Yeoman configuration file
  JHipster configuration is stored in this file at `generator-jhipster` key. You may find `generator-jhipster-*` for specific blueprints configuration.
- `.yo-resolve` (optional) - Yeoman conflict resolver
  Allows to use a specific action when conflicts are found skipping prompts for files that matches a pattern. Each line should match `[pattern] [action]` with pattern been a [Minimatch](https://github.com/isaacs/minimatch#minimatch) pattern and action been one of skip (default if ommited) or force. Lines starting with `#` are considered comments and are ignored.
- `.jhipster/*.json` - JHipster entity configuration files
- `/src/main/docker` - Docker configurations for the application and services that the application depends on

## Development

To start your application in the dev profile, run:

```
./gradlew
```

For further instructions on how to develop with JHipster, have a look at [Using JHipster in development][].

### JHipster Control Center

JHipster Control Center can help you manage and control your application(s). You can start a local control center server (accessible on http://localhost:7419) with:

```
docker-compose -f src/main/docker/jhipster-control-center.yml up
```

## Building for production

### Packaging as jar

To build the final jar and optimize the simple application for production, run:

```
./gradlew -Pprod clean bootJar
```

To ensure everything worked, run:

```
java -jar build/libs/*.jar
```

Refer to [Using JHipster in production][] for more details.

### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

```
./gradlew -Pprod -Pwar clean bootWar
```

## Testing

To launch your application's tests, run:

```
./gradlew test integrationTest jacocoTestReport
```

For more information, refer to the [Running tests page][].

### Code quality

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker-compose -f src/main/docker/sonar.yml up -d
```

Note: we have turned off authentication in [src/main/docker/sonar.yml](src/main/docker/sonar.yml) for out of the box experience while trying out SonarQube, for real use cases turn it back on.

You can run a Sonar analysis with using the [sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) or by using the gradle plugin.

Then, run a Sonar analysis:

```
./gradlew -Pprod clean check jacocoTestReport sonarqube
```

For more information, refer to the [Code quality page][].

## Using Docker to simplify development (optional)

You can use Docker to improve your JHipster development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

For example, to start a postgresql database in a docker container, run:

```
docker-compose -f src/main/docker/postgresql.yml up -d
```

To stop it and remove the container, run:

```
docker-compose -f src/main/docker/postgresql.yml down
```

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

```
npm run java:docker
```

Or build a arm64 docker image when using an arm64 processor os like MacOS with M1 processor family running:

```
npm run java:docker:arm64
```

Then run:

```
docker-compose -f src/main/docker/app.yml up -d
```

When running Docker Desktop on MacOS Big Sur or later, consider enabling experimental `Use the new Virtualization framework` for better processing performance ([disk access performance is worse](https://github.com/docker/roadmap/issues/7)).

For more information refer to [Using Docker and Docker-Compose][], this page also contains information on the docker-compose sub-generator (`jhipster docker-compose`), which is able to generate docker configurations for one or several JHipster applications.

## Continuous Integration (optional)

To configure CI for your project, run the ci-cd sub-generator (`jhipster ci-cd`), this will let you generate configuration files for a number of Continuous Integration systems. Consult the [Setting up Continuous Integration][] page for more information.

[jhipster homepage and latest documentation]: https://www.jhipster.tech
[jhipster 7.9.3 archive]: https://www.jhipster.tech/documentation-archive/v7.9.3
[using jhipster in development]: https://www.jhipster.tech/documentation-archive/v7.9.3/development/
[using docker and docker-compose]: https://www.jhipster.tech/documentation-archive/v7.9.3/docker-compose
[using jhipster in production]: https://www.jhipster.tech/documentation-archive/v7.9.3/production/
[running tests page]: https://www.jhipster.tech/documentation-archive/v7.9.3/running-tests/
[code quality page]: https://www.jhipster.tech/documentation-archive/v7.9.3/code-quality/
[setting up continuous integration]: https://www.jhipster.tech/documentation-archive/v7.9.3/setting-up-ci/
[node.js]: https://nodejs.org/
[npm]: https://www.npmjs.com/
