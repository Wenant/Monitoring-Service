# Monitoring-Service

Проект Monitoring-Service представляет собой консольное приложение для учета и отображения показаний счетчиков пользователей.

## Описание

Приложение предоставляет следующий функционал:

- Регистрация нового пользователя
- Авторизация пользователя
- Подача месячных показаний счетчиков
- Просмотр актуальных показаний счетчиков
- Просмотр истории подачи показаний по датам
- Контроль прав доступа пользователей (администратор и обычный пользователь)
- Аудит действий пользователей


## Инструкции по сборке и запуску

- mvn clean package
- docker-compose build
- docker-compose run -i --rm your_app


