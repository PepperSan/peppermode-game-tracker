# 🎮 PepperMode Game Tracker

**PepperMode Game Tracker** — минималистичный CLI-инструмент для отслеживания прогресса в видеоиграх.  
Создан как первый этап pet-проекта в экосистеме **PepperMode**,  
где игры рассматриваются как искусство — с вниманием к атмосфере, истории и развитию персонажей.

---

## 🧩 Project Overview

**Current Phase:** `CLI MVP ✅`  
**Next:** JSON Save ⏳ → REST API 🚀

Проект позволяет:
- Добавлять игры и игровые сессии
- Просматривать статистику: среднюю длину сессий, топ по жанрам и т.д.
- Работать в консоли (без базы данных)

---

## 🧱 Tech Stack

| Layer | Technology |
|-------|-------------|
| Core Language | Java 17+ |
| Build System | Maven |
| Architecture | Modular (domain → repo → service → cli) |
| Future Plans | Spring Boot, REST API, JSON persistence |

---

### Persistence
- In-memory (default dev mode)
- JSON files (`data/games.json`, `data/sessions.json`)
  Переключение в `Main`: `useFileStorage = true|false`.


---

## ⚙️ How to Run Locally

```bash
# 1. Clone the repository
git clone https://github.com/PepperSan/peppermode-game-tracker.git

# 2. Open in IntelliJ IDEA or any IDE with Maven support
# 3. Build the project
mvn clean package

# 4. Run the CLI app
java -cp target/peppermode-game-tracker-1.0-SNAPSHOT.jar com.peppermode.tracker.cli.Main

## REST API

Base URL: `http://localhost:8080/api`

### Games
- `GET /games` — список игр
- `GET /games/{id}` — игра по id
- `POST /games` — создать игру  
  Body:
  ```json
  {"title":"Ghost of Tsushima","genre":"Action","platform":"PS5","releaseYear":2020}

