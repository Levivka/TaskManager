
# Task Management System

## Требования
- Java 17+
- Docker и Docker Compose
- Gradle

## Локальный запуск
Подготовка:
1. Клонируйте репозиторий:
```
git clone https://github.com/Levivka/TaskManager.git
```
```
cd TaskManagementSystem
```

2. Запустите dev окружение (PostgreSQL) через Docker Compose:
```
cd Docker
```
```
docker-compose up -d
```
Запуск REST API:
1. Соберите проект:
```
./gradlew clean build
```

2. Запустите приложение:
```
./gradlew bootRun
```
**Либо**

Откройте проект в любой IDE и скомпилируйте **TaskManagerSystemApplication** по пути _src/main/java/com/example/taskmanagmentsystem/TaskManagmentSystemApplication.java_



Документация Swagger UI будет доступна по адресу: http://localhost:8084/swagger-ui.html
