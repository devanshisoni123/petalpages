# PetalPages

A private, full-stack journaling app. Write daily entries, keep notes, and track your cycle, all behind your own login.

**Frontend preview:** [devanshisoni123.github.io/petalpages](https://devanshisoni123.github.io/petalpages)

> The backend is currently offline, so sign-up and login are unavailable. The full code is in this repo.

## Features

- Secure sign-up and login with JWT authentication
- Journal entries: create, edit, delete
- Notes
- Period tracker
- 11 pages, fully responsive

## Tech stack

| Layer | Technology |
|---|---|
| Frontend | HTML, CSS, vanilla JavaScript (hosted on GitHub Pages) |
| Backend | Spring Boot, Java, REST API (hosted on Railway) |
| Database | MySQL |
| Auth | JWT |

## Project structure

```
petalpages/
├── backend/     Spring Boot API (Maven)
└── frontend/    Static pages, CSS and JS
```

## Run locally

**Backend**

```bash
cd backend
./mvnw spring-boot:run
```

On Windows use `mvnw.cmd spring-boot:run`. You need Java and a running MySQL instance. The app reads its configuration from environment variables: `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD` and `JWT_SECRET` (a random string of at least 32 characters).

**Frontend**

Open `frontend/index.html` in a browser, or serve the folder with any static server. Point the API base URL in the frontend JS at `http://localhost:8080`.

## Author

Built by [Devanshi Soni](https://github.com/devanshisoni123).
