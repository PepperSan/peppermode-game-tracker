![Java](https://img.shields.io/badge/Java-17+-orange)
![Maven](https://img.shields.io/badge/Maven-Build-blue)
![Status](https://img.shields.io/badge/Stage-MVP-success)
![License](https://img.shields.io/badge/License-MIT-green)


# PepperMode Game Tracker

**PepperMode Game Tracker** — минималистичный CLI-инструмент для отслеживания прогресса в видеоиграх.  
Создан как первый этап pet-проекта в экосистеме **PepperMode**,  
где игры рассматриваются как искусство — с вниманием к атмосфере, истории и развитию персонажей.

---

Проект задуман как учебный, чтобы:

- отработать ключевые навыки backend-разработки;
- потренироваться в слоистой архитектуре (domain → repo → service → api);
- поработать с JSON-хранилищами и REST API;
- попрактиковать DTO, валидацию, обработку ошибок и тестирование;
- собрать удобный, но простой CLI-инструмент.

Сейчас проект служит фундаментом для изучения Spring Boot  
и подготовки к следующим более сложным backend-проектам.

## Project Overview

**Current Phase:** CLI MVP ✅  
**Next:** JSON Save ↔ REST API 🚀

Проект позволяет:

- добавлять игры и игровые сессии;
- смотреть базовую статистику (длительность сессий, топ по времени, жанры);
- работать как с in-memory режимом, так и с JSON-хранилищем.

---

## 🧱 Tech Stack

| Layer           | Technology                                              |
|-----------------|----------------------------------------------------------|
| Core Language   | Java 17+                                                 |
| Build System    | Maven                                                    |
| Architecture    | Modular (domain → repo → service → cli/api)             |
| Persistence     | In-memory storage + JSON file storage                   |
| Validation      | Spring Boot Validation (JSR-380)                         |
| REST Framework  | Spring Web (Controllers, DTO, Error Handling)            |
| Testing         | JUnit 5, Spring MockMvc                                  |
| Documentation   | Swagger UI (auto-generated API docs)                     |
| Future Plans    | PostgreSQL, Docker, JWT Auth, Desktop UI (JavaFX)        |

---


## 📦 Persistence & Storage Modes

Приложение поддерживает два режима хранения данных:

### 🔹 1. In-Memory (по умолчанию)
- Данные хранятся только в оперативной памяти.
- После перезапуска приложения всё очищается.
- Удобно для разработки и тестирования.

### 🔹 2. File Storage (JSON)
- Данные сохраняются в файлы:

```bash
data/games.json
data/sessions.json
```
Обеспечивает простое персистентное хранение между запусками.

#### 🔧 Переключение режима

Изменяется конфигурацией:
```bash
pepper.storage.file=true;
```

Файл настроек находится по пути:

`src/main/resources/application.properties`


При смене параметра репозитории автоматически переключаются между RAM и JSON-хранилищем.


---
## 🚀 How to Run Locally

### 1. Clone the repository
```bash
git clone https://github.com/PepperSan/peppermode-game-tracker.git
cd peppermode-game-tracker
```
### 2. Build the project
```bash
mvn clean package
```
### 3. Run as CLI app
```bash
java -cp target/peppermode-game-tracker-1.0-SNAPSHOT.jar \
     com.peppermode.tracker.cli.Main
```
### 4. Run as REST API (Spring Boot)
```bash
mvn spring-boot:run
```

По умолчанию сервер поднимается на:

`http://localhost:8080/api`

### 🤖 Swagger UI

После запуска Spring Boot доступна интерактивная документация:

`http://localhost:8080/swagger-ui/index.html`


---

## 🚀 Run Spring Boot API

Если хочешь запустить приложение как **REST-сервер**, а не как CLI-инструмент:
```bash
mvn spring-boot:run
```
По умолчанию сервер поднимается на:
```bash
http://localhost:8080/api
```
## 📚 Swagger UI

После запуска Spring Boot API ты можешь просматривать и тестировать все эндпоинты через интерактивную документацию Swagger UI.

**URL:**

👉 `http://localhost:8080/swagger-ui/index.html`

Swagger UI автоматически подхватывает все аннотации `@Operation`, `@ApiResponse`, `@Tag`  
и строит понятный интерфейс для работы с API.


## 🔗 REST API
**Base URL:** `http://localhost:8080/api`


---

## 📚 API Overview

| Category | Method | Endpoint                    | Description                                  |
|----------|--------|-----------------------------|----------------------------------------------|
| Games    | GET    | `/api/games`               | Получить список игр                          |
| Games    | GET    | `/api/games/{id}`          | Получить игру по ID                          |
| Games    | POST   | `/api/games`               | Создать игру                                 |
| Games    | PUT    | `/api/games/{id}`          | Обновить игру                                |
| Games    | DELETE | `/api/games/{id}`          | Удалить игру                                 |
| Games    | DELETE | `/api/games`               | Удалить все игры                             |
| Sessions | POST   | `/api/sessions`            | Создать игровую сессию                       |
| Sessions | GET    | `/api/sessions`            | Получить все сессии                          |
| Sessions | GET    | `/api/sessions/{id}`       | Получить сессию по ID                        |
| Sessions | GET    | `/api/sessions/game/{gameId}` | Сессии для указанной игры                 |
| Sessions | DELETE | `/api/sessions/{id}`       | Удалить сессию по ID                         |
| Sessions | DELETE | `/api/sessions`            | Удалить все сессии                           |
| Stats    | GET    | `/api/stats/avg-session`   | Средняя длина игровой сессии                 |
| Stats    | GET    | `/api/stats/top-time`      | Топ игр по времени (`limit` — необязательный) |
| Stats    | GET    | `/api/stats/genres-count`  | Количество игр по жанрам (`platform` — опция) |

# 🎮 Games API

### 📄 Получить список игр
```bash
GET /api/games
```

**Ответ:**
```bash
[
{
"id": "uuid",
"title": "Ghost of Tsushima",
"genre": "Action",
"platform": "PS5",
"releaseYear": 2020
}
]
```
### 🔍 Получить игру по ID
```bash
GET /api/games/{id}
```

**Ответ:**
```bash
{
"id": "uuid",
"title": "Ghost of Tsushima",
"genre": "Action",
"platform": "PS5",
"releaseYear": 2020
}
```
### ➕ Создать игру
```bash
POST /api/games
Content-Type: application/json
```
**Пример Body:**
```bash
{
"title": "Ghost of Tsushima",
"genre": "Action",
"platform": "PS5",
"releaseYear": 2020
}
```
**Ответ:**
```bash
{
"id": "uuid",
"title": "Ghost of Tsushima",
"genre": "Action",
"platform": "PS5",
"releaseYear": 2020
}
```
### ♻ Обновить игру
```bash
PUT /api/games/{id}
```
**Body пример:**
```bash
{
"title": "Ghost of Tsushima — Director's Cut",
"genre": "Action",
"platform": "PS5",
"releaseYear": 2021
}
```
### 🗑 Удалить игру
```bash
DELETE /api/games/{id}
```
### 🗑🗑 Удалить все игры
```bash
DELETE /api/games
```
# 🕒 Sessions API

### ➕ Создать игровую сессию
```bash
POST /api/sessions
```
**Body пример:**
```bash
{
  "gameId": "uuid",
  "minutesPlayed": 45,
  "characterName": "Jin",
  "notes": "Boss fight"
}
```
**Ответ:**
```bash
{
"id": "uuid",
"gameId": "uuid",
"start": "2025-11-19T21:35:39.494426",
"minutesPlayed": 45,
"characterName": "Jin",
"notes": "Boss fight"
}
```
### 📄 Получить все сессии
```bash
GET /api/sessions
```
**Ответ:**
```bash
[
{
"id": "uuid",
"gameId": "uuid",
"start": "2025-11-19T21:35:39.494426",
"minutesPlayed": 45,
"characterName": null,
"notes": null
}
]
```
### 🔍 Получить сессии по игре
```bash
GET /api/sessions/game/{gameId}
```
### 🔍 Получить сессию по ID
```bash
GET /api/sessions/{sessionId}
```
### 🗑 Удалить сессию
```bash
DELETE /api/sessions/{sessionId}
```
### 🗑🗑 Удалить все сессии
```bash
DELETE /api/sessions
```
# 📊 Stats API

### 🕒 Средняя длина игровой сессии
Возвращает среднее время всех игровых сессий.
```bash
GET /api/stats/avg-session
```
**Пример ответа:**
```bash
{
"averageMinutes": 42.5
}
```

### ⏱️ Топ игр по времени
Возвращает игры, отсортированные по суммарному времени, по убыванию.
```bash
GET /api/stats/top-time?limit=3
```
**Параметры:**

limit — количество игр в топе (необязательно)

**Пример ответа:**
```bash
[
{
"gameId": "uuid",
"title": "Ghost of Tsushima",
"totalMinutes": 350
}
]
```
### 🧩 Количество игр по жанрам
```bash
GET /api/stats/genres-count
# опционально: ?platform=PS5
```
**Пример ответа:**
```bash
{
"Action": 4,
"RPG": 2,
"Adventure": 1
}
```
## 🏛 Архитектура проекта

```text
src
├─ domain           // Модели Game и PlaySession
├─ repo             // Интерфейсы репозиториев
│  ├─ file          // JSON-репозитории
│  └─ inmemory      // In-memory репозитории
├─ service          // Бизнес-логика и статистика
├─ api              // REST-контроллеры + DTO + валидация
└─ cli              // Консольное приложение
```
## 🧩 Data Model

### Game

```json
{
  "id": "uuid",
  "title": "Ghost of Tsushima",
  "genre": "Action",
  "platform": "PS5",
  "releaseYear": 2020
}
```
### PlaySession
```json
{
"id": "uuid",
"gameId": "uuid",
"start": "2025-11-19T21:35:39.494426",
"minutesPlayed": 45,
"characterName": null,
"notes": null
}
```


## 🖥 CLI Usage 

```bash
# Запустить приложение
java -cp target/peppermode-game-tracker-1.0-SNAPSHOT.jar com.peppermode.tracker.cli.Main

# Примеры 
> list           # список игр
> add-game       # добавить игру
> stats          # показать статистику
```
## ✅ Тестирование

- JUnit 5 — юнит-тесты сервисов
- Spring MockMvc — тесты REST-контроллеров (`GameController`, `SessionController`, `StatsController`)

Запуск тестов:

```bash
mvn test
```
## 🚀 Future Plans

- PostgreSQL storage
- Desktop UI (JavaFX)
- JWT Auth
- Docker Compose для БД


## 🗺️ Roadmap

- [x] CLI MVP (игры, сессии, статистика)
- [x] JSON persistence (games.json, sessions.json)
- [x] REST API
- [x] File & InMemory репозитории
- [x] DTO + Validation
- [x] MockMvc Tests
- [x] Basic error handling
- [x] README документация
- [x] Swagger UI
- [ ] PostgreSQL storage
- [ ] Desktop UI (JavaFX)
- [ ] JWT Auth (опционально)

### 🧪 Test TODO

Идеи для расширения автотестов (на будущее):

- **GameControllerTest:** больше негативных кейсов (невалидные id, пустые поля).
- **GameControllerValidationTest:** проверки сообщений об ошибках для `genre` и `releaseYear`.
- **SessionControllerTest:** кейсы с пустыми списками сессий и несуществующими играми.
- **StatsControllerTest:** нестандартные входные данные для статистики.
- **Service layer:** дополнительные edge-кейсы для `StatsServiceTest`.



## 🤝 Contribution
Пул-реквесты приветствуются!  
Для значительных изменений — пожалуйста, создайте issue перед PR.
