# LdoD - MFEs Version

#### Pre-requisites:

- [git](https://git-scm.com/)
- [docker](https://www.docker.com/)
- [docker-compose](https://docs.docker.com/compose/install/)
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

- Replace the dbAlias, dbUsername and dbPassword according the configuration on the mysql-db service in docker-compose.yml

### Contact the administrator to obtain the secrete.properties

### Build and run services

```sh
docker-compose up --build
```

### Publish or Unpublish MFEs

To publish or unpublish MFEs, use the following commands inside the ldod-mfes directory:

```sh
yarn run publish --prefix <mfe directory name> #yarn run publish --prefix about
```

```sh
yarn run unpublish --prefix <mfe directory name> #yarn run unpublish --prefix virtual
```

#### Note: The docker containers are sharing the host network so you must ensure the following ports availability:

- 9000 for ldod-mfes server
- 8000 for edition-ldod server
- 8080 for nginx proxy server

- The mfes ldod version is served on `http://localhost:8080/ldod-mfes`

- The edition-ldod original version is served on `http://localhost:8080`

### Browser compatibility

- IE not supported
- Chrome version 89 or higher
- Firefox version 108 or higher
- Safari to be tested
- Opera version 76 or higher
- Edge version 89 or higher
