# Проектная работа по курсу OTUS "Разработчик на Spring Framework" (2022-08)

Студент: Савенко Максим Анатольевич
Дата старта курса: 31 августа 2022 г.

## Тема: Онлайн кинотеатр

Краткое описание

- Сайт предоставляющий просмотр видеоконтента.
- Доступна фильтрация по категориям/жанрам.
- Панель управления контентом (админка).
- В качестве контента используются коды третьисторонних видеохостингов (например youtube, vimeo)
- Зарегистрированные зрители могут ставить оценки.

## Требования для сборки и выполнения

- Java 11 (JDK)
- Docker (для сборки образа)
- Docker compose (для запуска окружения и демо)
- GNU Make (для запуска автоматизированных скриптов - опционально)

## Демо

Для запуска демо создан скрипт автоматизации сборки контейнеров и развертыванию демо данных.

### Сборка контейнеров

Чтобы собрать образы докер для всех модулей проекта, выполните:

```shell
make docker-buid
```

### Запуск демо

```shell
make start-demo
```

### Остановка демо

```shell
make stop-demo
```

Если необходимо сделать очистку ресурсов (удалить сеть, вольюмы и т.д):

```shell
make stop-demo-clean
```


### Ресурсы

| URL                                            | Описание                                    |
|------------------------------------------------|---------------------------------------------|
| http://localhost:18080/                        | Фронтенд                                    |
| http://localhost:19900/backend/swagger-ui.html | Бэкенд REST                                 |
| http://localhost:18761/                        | Eureka                                      |
| http://localhost:9001                          | Minio (user: minioadmin, pass: minioadmin)  |
| http://localhost:9090                          | Prometheus                                  |
| http://localhost:3000                          | Grafana (user: admin, pass: admin)          |
| jdbc:postgresql://postgres:5432/movies         | Postgresql (user: postgres, pass: postgres) |

Предопределенные пользователи системы

| Роль  | Логин                           |
|-------|---------------------------------|
| USER  | login: `user` password: `pass`  |
| ADMIN | login: `admin` password: `pass` |


## Среда разработки

Для окружения есть скрипты для запуска, аналогичные описанным выше для Демо.

При запуске окружения будут запущены:

| URL                                     | Описание                                    |
|-----------------------------------------|---------------------------------------------|
| jdbc:postgresql://localhost:5432/movies | Postgresql (user: postgres, pass: postgres) |
| http://localhost:9001                   | Minio (user: minioadmin, pass: minioadmin)  |
| http://localhost:9090                   | Promitheus                                  |
| http://localhost:3000                   | Grafana (user: admin, pass: admin)          |


*Запуск*

```shell
make start
```

*Остановка*

```shell
make stop
```

*Остановка с очисткой*

```shell
make stop-clean
```
