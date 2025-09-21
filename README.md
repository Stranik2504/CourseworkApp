# Coursework Mobile App (Android)

Мобильное приложение для курсовой работы: создание тестовых заданий, их прохождение, просмотр результатов. Поддерживаются роли администратора (преподаватель) и обычного пользователя. Приложение общается с серверным API.

## Возможности
- Регистрация и вход/выход.
- Просмотр списка заданий.
- Прохождение тестов с вариантами ответов.
- Просмотр собственных результатов.
- Роль администратора:
    - Создание заданий (название, короткое/полное описание, список вопросов с вариантами).
    - Назначение задания пользователям.
    - Удаление заданий.

## Роли
- Обычный пользователь:
    - Авторизация, прохождение тестов, просмотр результатов.
- Администратор:
    - Всё выше + создание/удаление заданий, выбор пользователей для назначения.
- Незалогиненный:
    - Просмотр доступных заданий (без возможности прохождения), регистрация/вход.

## Технологии
- Android (Java), AndroidX, Material Components.
- RecyclerView, ConstraintLayout/FlexboxLayout/GridLayout.
- Gson (`@SerializedName`) для сериализации.
- Хранение токена/состояний: `SharedPreferences`.
- Асинхронность: `Executor`/`Handler`.

## Основные экраны
- Главная: список доступных тестов.
- Решения: обзор решённых заданий и/или результатов.
- Аккаунт: профиль, статистика, выход.
- Админ: конструктор задания и выбор пользователей.

## Навигация
Нижняя панель (`BottomNavigationView`) с динамическим меню:
- Незалогиненный: `Главная`, `Аккаунт`.
- Залогиненный: `Главная`, `Решения`, `Аккаунт`.

## Структура проекта (ключевые файлы)
- Экран входа/регистрации: `app/src/main/java/dev/stranik/coursework/LoginFragment.java`, `RegistrationFragment.java`
- Профиль/выход: `app/src/main/java/dev/stranik/coursework/AccountFragment.java`
- Список заданий: `app/src/main/java/dev/stranik/coursework/TaskListFragment.java`
- Просмотр задания: `app/src/main/java/dev/stranik/coursework/ViewTaskFragment.java`
- Прохождение теста: `app/src/main/java/dev/stranik/coursework/TaskPassingFragment.java`
- Создание задания (админ): `app/src/main/java/dev/stranik/coursework/CreateTaskFragment.java`, `ChooseUserFragment.java`
- Решения/результаты: `app/src/main/java/dev/stranik/coursework/ListTaskSolutions.java`
- Адаптеры списков: `app/src/main/java/dev/stranik/coursework/utils/*.java`
- Макеты экранов: `app/src/main/res/layout/*.xml`
- Нижнее меню: `app/src/main/res/menu/register_user_menu.xml`, `app/src/main/res/menu/unregister_user_menu.xml`
- Точка входа: `app/src/main/java/dev/stranik/coursework/MainActivity.java`

## Взаимодействие с API
Сетевая логика инкапсулирована в `StaticWorkData`:
- Аутентификация: `loginUser(...)`, `registerUser(...)`, `saveToken()`, `loadToken()`.
- Данные пользователя: `getUserInfo()`, `getUsers()`.
- Задания (списки/создание/удаление): `getTasks(...)`, `createTask(...)`, `deleteTask(...)`.
- Прохождение: `getTaskItems(...)`, `submitAnswers(...)`.
- Состояние: `isAdmin()`, `getId()`, `setAdmin(...)`, `setId(...)`, `reset()`.

Настройте базовый URL и заголовки авторизации в `StaticWorkData` (обычно в константах/методах этого класса).

## Требования
- Android Studio Narwhal 3 Feature Drop \| 2025.1.3.
- Android SDK (целевой/минимальный уровни настраиваются в Gradle).
- Встроенный JDK (Android Studio, рекомендуется 17).
- Доступный сервер API и корректный `BASE_URL` в `StaticWorkData`.

## Сборка и запуск
1. Клонировать репозиторий и открыть в Android Studio.
2. Дождаться Sync Gradle.
3. Указать URL бэкенда в `StaticWorkData` (файл: `app/src/main/java/dev/stranik/coursework/utils/data/StaticWorkData.java`).
4. Запустить конфигурацию `app` на эмуляторе/устройстве.

Альтернатива (Windows, терминал из Android Studio):
- `.\gradlew clean assembleDebug`
- APK: `app/build/outputs/apk/debug/`

## Бизнес-потоки
- Авторизация:
    - Успех → сохранение токена (`SharedPreferences`), обновление меню, переход в профиль.
- Создание задания (админ):
    - Заполнение названия/описаний → конструктор вопросов/ответов → выбор пользователей → отправка на сервер.
- Прохождение теста:
    - Пошаговые вопросы (RadioGroup), выбор ответа → отправка результатов → экран решений.

## UI/UX
- Карточки списков с закруглённым фоном (`'app/src/main/res/drawable/card_background.xml'`).
- Цветовая палитра с белым текстом на синих фонах.
- FAB для добавления задания доступен только админу.

## Безопасность
- Токен хранится в `SharedPreferences`.
- Проверьте обработку истечения токена и ошибки сети в `StaticWorkData`.

## Лицензия
MIT License. См. файл `LICENSE` в корне репозитория.
