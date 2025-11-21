![Java](https://img.shields.io/badge/Java-17+-orange)
![Maven](https://img.shields.io/badge/Maven-Build-blue)
![Status](https://img.shields.io/badge/Stage-MVP-success)
![License](https://img.shields.io/badge/License-MIT-green)


# 🎮 PepperMode Game Tracker

**PepperMode Game Tracker** — минималистичный CLI-инструмент для отслеживания прогресса в видеоиграх.  
Создан как первый этап pet-проекта в экосистеме **PepperMode**,  
где игры рассматриваются как искусство — с вниманием к атмосфере, истории и развитию персонажей.

---
PepperMode Game Tracker создан как учебный pet-проект,
чтобы отработать ключевые навыки backend-разработки:  
структурирование приложения по слоям (domain → repo → service → api),
работу с JSON-хранилищами, создание REST API, валидацию DTO,
обработку ошибок, тестирование, а также написание CLI-инструментов.

Проект стал фундаментом для изучения Spring Boot и подготовки к
следующим более сложным backend-проектам.


## 🧩 Project Overview

**Current Phase:** `CLI MVP ✅`  
**Next:** JSON Save ⏳ → REST API 🚀

Проект позволяет:
- Добавлять игры и игровые сессии
- Просматривать статистику (средняя длина, топ по жанрам, распределение)
- Работать в консоли (без базы данных)

---

## 🧱 Tech Stack

| Layer          | Technology                                  |
|----------------|----------------------------------------------|
| Core Language  | Java 17+                                     |
| Build System   | Maven                                        |
| Architecture   | Modular (domain → repo → service → cli/api)  |
| Future Plans   | Spring Boot, REST API, JSON persistence      |

---

## 📦 Persistence

- In-memory (default dev mode)
- JSON files:
    - `data/games.json`
    - `data/sessions.json`
- Переключение режима в `Main`:
  ```java
  useFileStorage = true / false;

## ⚙️ How to Run Locally

```bash
# 1. Clone the repository
git clone https://github.com/PepperSan/peppermode-game-tracker.git

# 2. Open in IntelliJ IDEA or any IDE with Maven support

# 3. Build the project
mvn clean package

# 4. Run the CLI app
java -cp target/peppermode-game-tracker-1.0-SNAPSHOT.jar com.peppermode.tracker.cli.Main

```
## 🚀 Run Spring Boot API

Если хочешь запустить приложение как **REST-сервер**, а не как CLI-инструмент:
```bash
mvn spring-boot:run
```
По умолчанию сервер поднимается на:
```bash
http://localhost:8080/api
```

## 🌐 REST API

Base URL: `http://localhost:8080/api`

---

### 📘 API Overview

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/games` | Список игр |
| POST | `/api/games` | Создать игру |
| GET | `/api/games/{id}` | Получить игру |
| PUT | `/api/games/{id}` | Обновить игру |
| DELETE | `/api/games/{id}` | Удалить игру |
| DELETE | `/api/games` | Удалить все игры |
| POST | `/api/sessions` | Создать игровую сессию |
| GET | `/api/sessions` | Все сессии |
| GET | `/api/sessions/{id}` | Сессия по ID |
| DELETE | `/api/sessions/{id}` | Удалить сессию |
| GET | `/api/stats/avg-session` | Средняя длина сессии |
| GET | `/api/stats/top-time` | Топ игр по времени |
| GET | `/api/stats/genres-count` | Количество игр по жанрам |


# 🎮 Games API

### 📌 Получить список игр
```bash
GET /api/games
```
### Получить игру по ID
```bash
GET /api/games/{id}
```
### Создать игру
```bash
POST /api/games
Content-Type: application/json
```
### Пример Body:
```bash
{
"title": "Ghost of Tsushima",
"genre": "Action",
"platform": "PS5",
"releaseYear": 2020
}
```
### Обновить игру
```bash
PUT /api/games/{id}
```
### Удалить игру
```bash
DELETE /api/games/{id}
```
### Удалить все игры
```bash
DELETE /api/games
```
# 🕒 Sessions API

### Создать игровую сессию
```bash
POST /api/sessions
```
Body пример:
```bash
{
"gameId": "uuid",
"minutesPlayed": 45
}
```
### Получить все сессии
```bash
GET /api/sessions
```
### Получить сессии по игре
```bash
GET /api/sessions/game/{gameId}
```
### Получить сессию по ID
```bash
GET /api/sessions/{sessionId}
```
### Удалить сессию
```bash
DELETE /api/sessions/{sessionId}
```
### Удалить все сессии
```bash
DELETE /api/sessions
```
# 📊 Stats API

### Средняя длина игровой сессии
```bash
GET /api/stats/avg-session
```
### Топ игр по времени
```bash
GET /api/stats/top-time?limit=3
```
### Количество игр по жанрам
```bash
GET /api/stats/genres-count
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
- Добавить PostgreSQL storage
- Desktop UI (JavaFX)
- Swagger UI
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
- [ ] Swagger UI
- [ ] PostgreSQL storage
- [ ] Desktop UI (JavaFX)
- [ ] JWT Auth (опционально)

## 🤝 Contribution
Пул-реквесты приветствуются!  
Для значительных изменений — пожалуйста, создайте issue перед PR.
