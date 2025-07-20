FROM postgres:latest

# Копируем все SQL-файлы в initdb директорию
COPY ./init/01_schema.sql /docker-entrypoint-initdb.d/
COPY ./init/02_test-data.sql /docker-entrypoint-initdb.d/
