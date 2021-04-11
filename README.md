# Seng302 Example Project

Basic project template using `gradle`, `npm`, `Spring Boot`, `Vue.js` and `Gitlab CI`.

## Basic Project Structure

A frontend sub-project (web GUI):

- `frontend/src` Frontend source code (Vue.js)
- `frontend/public` publicly accessible web assets (e.g., icons, images, style sheets)
- `frontend/dist` Frontend production build

A backend sub-project (business logic and persistence server):

- `backend/src` Backend source code (Java - Spring)
- `backend/out` Backend production build

## How to run

### Frontend / GUI

    $ cd frontend
    $ npm install
    $ npm run serve

Running on: http://localhost:9500/ by default

### Backend / server

    cd backend
    ./gradlew bootRun

Running on: http://localhost:9499/ by default

## Contributors

- SENG302 teaching team
- Ayub Momahamed
- Dan Bublik
- David Frost
- Jackie Chen
- Kye Oldham
- Lachlan Reynolds
- Omar Sheta
- Ronan Avery

## Technologies / Dependencies

Frontend / GUI
- Axios
- Vuejs
- Vue-Router
- Vuesax 3 UI component framework

Backend / server
- Spring Boot (JPA, Security, REST)
- H2 Database Engine
- MariaDB

## References

- [Spring Boot Docs](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Spring JPA docs](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Vue docs](https://vuejs.org/v2/guide/)
- [Learn resources](https://learn.canterbury.ac.nz/course/view.php?id=10577&section=11)
