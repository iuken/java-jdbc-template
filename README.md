# Шаблон для практической работы по теме JDBC

Приложение представляет из себя спринг-бут приложение, которое через JDBC будет подключаться к БД, используя 
JdbcTemplate, будет выполнять запросы. Используется база данных [H2](https://www.h2database.com/html/main.html), 
которая разворачивается в памяти и может быть легко сброшена до изначальных данных.

Склонировать репозиторий. Реализовать методы в классе `JdbcTemplatePublicAuction`, заполнить сущнлости полями, 
сконфигурировать бины. Для удобства выполнения рекомендуется имплементировать бины `BidMapper`, `UserMapper`, `BidMapper`.
`DataSource` и `JdbcTemplate` **должны быть** созданы в конфигурации приложения. Для заполнения БД 
данными используйте файл `data.sql` в `src/main/resources`. 

Результат выполнения работы можно проверить запустив тест `JdbcTemplatePublicAuctionTest`.

>
> Для удобства при запуске приложения на эндпоинте `/h2-console` будет поднята админка для доступа в базу данных H2
> 
