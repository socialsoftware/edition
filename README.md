# LdoD - MFEs Version

#### Pre-requisites:

- [git](https://git-scm.com/)
- [docker](https://www.docker.com/)
- [docker-compose](https://docs.docker.com/compose/install/)
- [Maven](https://maven.apache.org/)
- [Node](https://nodejs.org/en/)
- Internet connection

### Clone git rep

```sh
git clone -b ldod-mfes --single-branch git@github.com:socialsoftware/edition.git
```

### create and configure specific.properties file

```sh
cd edition-ldod
cp src/main/resources/specific.properties.example src/main/resources/specific.properties
```

Replace PATH according the environment

### Apply the procedure above for the fenix-framework.properties

- Replace the dbUsername and dbPassword according the configuration on the mysql-db service in docker-compose.yml

### Contact the administrator to obtain the secrete.properties

### Compile edition-ldod

```sh
cd edition-ldod
mvn clean install -DskipTests -P war
```

### Build and run services

```sh
docker-compose build
```

```sh
docker-compose up
```

### Publish or Unpublish MFE at one go

```sh
cd ldod-mfes
npm run install
```

```sh
sh scripts/publish-all.sh
```

```sh
sh scripts/unpublish-all.sh
```

### Publish or Unpublish one MFE at a time

```sh
cd ldod-mfes/<mfe>
```

```sh
yarn run publish
```

```sh
yarn run unpublish
```

#### Note: The docker containers are sharing the host network so you must ensure the following ports availability:

- 9000 for ldod-mfes server
- 8000 for edition-ldod server
- 8080 for nginx proxy server

- The mfes ldod version is served on `http://localhost:8080/ldod-mfes`

- The edition-ldod original version is served on `http://localhost:8080`
