# Реализация аутентификации и авторизации пользователей с помощью JWT.

## Технологии:
1. Java 17, Spring(Boot,Security,Data).
2. JWT.
3. PostgreSQL.
4. Mapstruct.

## В проекте реализован следующий функционал:
1. Валидация входящих данных.
2. Кодировка Bcrypt пароля пользователя.
3. Security настроен через Config и частично через аннотацию @PreAuthorize(то есть как на уровне URL так и на уровне методов(PreAuthorize)).
4. Аутентификация с помощью JWT.
5. Авторизация - 2 роли USER и ADMIN.

## Инструкция по использованию
1. Локально поднять приложение, поднять БД postgres(см application.properties).
2. С помощью Postman или аналогов отправить POST запрос на http://localhost:8080/auth/registration с RequestBody(JSON) следующего содержания:<br>
{
    "username": "Test",
    "yearOfBirth": 2000,
    "password": "Test"
}<br>
4. Ответом на вышеуказанны запрос будет JWT, который нужно добавить в загаловок Authorization в GET запрос по адресу http://localhost:8080/hello или http://localhost:8080/hello/showUser
5. Вы аутентифицированны и авторизированны!<br>
Чтобы протестировать авторизацию - необходимо вручную изменить запись в бд, поменяв роль на ROLE_ADMIN в колонке role для любого из зарегистированных вами пользователей(по умолчанию пользователь имеет ROLE_USER). Это позволит вам получить response по GET запросу http://localhost:8080/hello/admin, куда закрыт доступ для USERs.
<br>
Чтобы обновить токен, необходимо отправить POST запрос по адресу http://localhost:8080/auth/login с RequestBody(JSON) следующего содержания:<br>
{
    "username": "Test",
    "password": "Test"
}<br>
