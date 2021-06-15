docker rm -f splitbill-db
mvn clean install
docker run -d -it --name splitbill-db --rm -p 5432:5432  -e POSTGRES_USER=postgres  -e POSTGRES_PASSWORD=dbpassword  -e POSTGRES_DB=splitbill  postgres:10-alpine
mvn -pl rest exec:exec