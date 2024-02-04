package org.wenant;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.wenant.database.DatabaseConnector;
import org.wenant.database.MigrationRunner;
import org.wenant.domain.entity.AuditLog;
import org.wenant.domain.entity.MeterReading;
import org.wenant.domain.entity.MeterTypeCatalog;
import org.wenant.domain.entity.User;
import org.wenant.domain.entity.User.Role;
import org.wenant.domain.repository.*;
import org.wenant.service.UserService;
import org.wenant.service.in.AuthService;
import org.wenant.service.in.MeterReadingService;
import org.wenant.service.in.MeterTypeCatalogService;
import org.wenant.service.in.RegistrationService;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Класс, представляющий консольное приложение для работы с системой учета показаний счетчиков.
 */
public class ConsoleApp {
    private final UserService userService;
    private final MeterReadingService meterReadingService;
    private final RegistrationService registrationService;
    private final AuthService authService;
    private final MeterTypeCatalogService meterTypeCatalogService;
    private final List<AuditLog> auditLog = new ArrayList<>();
    private final Scanner scanner;

    /**
     * Конструктор класса ConsoleApp.
     *
     * @param userService             Сервис пользователей.
     * @param meterReadingService     Сервис показаний счетчиков.
     * @param registrationService     Сервис регистрации пользователей.
     * @param authService             Сервис аутентификации пользователей.
     * @param meterTypeCatalogService Каталог типов счетчиков.
     * @param scanner                 Объект Scanner для ввода с консоли.
     */
    public ConsoleApp(UserService userService, MeterReadingService meterReadingService, RegistrationService registrationService, AuthService authService, MeterTypeCatalogService meterTypeCatalogService, Scanner scanner) {
        this.userService = userService;
        this.meterReadingService = meterReadingService;
        this.registrationService = registrationService;
        this.authService = authService;
        this.meterTypeCatalogService = meterTypeCatalogService;
        this.scanner = scanner;
    }

    /**
     * Точка входа в приложение.
     */
    public static void main(String[] args) {
        UserRepository userRepository = new JdbcUserRepository();
        UserService userService = new UserService(userRepository);

        MeterTypeCatalogRepository meterTypeCatalogRepository = new JdbcMeterTypeCatalogRepository();
        MeterReadingRepository meterReadingRepository = new JdbcMeterReadingRepository(meterTypeCatalogRepository);
        MeterReadingService meterReadingService = new MeterReadingService(meterReadingRepository);

        MeterTypeCatalogService meterTypeCatalogService = new MeterTypeCatalogService(meterTypeCatalogRepository);

        RegistrationService registrationService = new RegistrationService(userRepository);
        AuthService authService = new AuthService(userService);

        Scanner scanner = new Scanner(System.in);
        ConsoleApp consoleApp = new ConsoleApp(userService, meterReadingService, registrationService, authService, meterTypeCatalogService, scanner);

        MigrationRunner.runMigrations();

        int choice;

        do {
            System.out.println("Выберите действие:");
            System.out.println("1. Регистрация");
            System.out.println("2. Логин");
            System.out.println("0. Выход");

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    consoleApp.registrationNewUser();
                    break;
                case 2:
                    User authenticatedUser = consoleApp.authenticateUser();
                    if (authenticatedUser != null) {
                        if (authenticatedUser.getRole() == Role.ADMIN) {
                            // Меню для админа
                            int adminChoice;
                            do {
                                System.out.println("\n Меню администратора:");
                                System.out.println("1. Просмотр всех пользователей");
                                System.out.println("2. Добавить новые тип счетчика");
                                System.out.println("3. Просмотр истории подачи показаний пользователея");
                                System.out.println("4. Получить актуальные показания");
                                System.out.println("5. Просмотр показаний пользователея за конкретный месяц");
                                System.out.println("0. Выход");

                                adminChoice = scanner.nextInt();
                                scanner.nextLine();

                                switch (adminChoice) {
                                    case 1:
                                        consoleApp.displayAllUsers(authenticatedUser);
                                        break;
                                    case 2:
                                        consoleApp.addNewMeterType(authenticatedUser);
                                        break;
                                    case 3:
                                        consoleApp.displayUserMeterReadings(authenticatedUser);
                                        break;
                                    case 4:
                                        consoleApp.getLatestMeterReadings(authenticatedUser);
                                        break;
                                    case 5:
                                        consoleApp.displayMeterReadingsByUserAndDate(authenticatedUser);
                                        break;
                                    case 0:
                                        consoleApp.auditUserLogout(authenticatedUser);
                                        System.out.println("Выход из меню администратора.");
                                        break;
                                    default:
                                        System.out.println("Некорректный выбор. Попробуйте еще раз.");
                                }

                            } while (adminChoice != 0);
                        } else if (authenticatedUser.getRole() == Role.USER) {
                            // Меню для пользователя
                            int userChoice;
                            do {
                                System.out.println("\n Меню пользователя:");
                                System.out.println("1. Добавить новые показания счетчиков");
                                System.out.println("2. Просмотр моих показаний за конкретный месяц");
                                System.out.println("3. Просмотр моей истории подачи показаний");
                                System.out.println("4. Получить актуальные показания");
                                System.out.println("0. Выход");

                                userChoice = scanner.nextInt();
                                scanner.nextLine();

                                switch (userChoice) {
                                    case 1:
                                        consoleApp.addNewMeterReadingsForUser(authenticatedUser, meterTypeCatalogService);
                                        break;
                                    case 2:
                                        consoleApp.displayMeterReadingsByUserAndDate(authenticatedUser);
                                        break;
                                    case 3:
                                        consoleApp.displayUserMeterReadings(authenticatedUser);
                                        break;
                                    case 4:
                                        consoleApp.getLatestMeterReadings(authenticatedUser);
                                        break;
                                    case 0:
                                        consoleApp.auditUserLogout(authenticatedUser);
                                        System.out.println("Выход из меню пользователя.");
                                        break;
                                    default:
                                        System.out.println("Некорректный выбор. Попробуйте еще раз.");
                                }

                            } while (userChoice != 0);
                        }
                    }
                    break;
                case 0:
                    consoleApp.displayAuditLog();
                    System.out.println("Выход из программы.");
                    break;
                default:
                    System.out.println("Некорректный выбор. Попробуйте еще раз.");
            }

        } while (choice != 0);


    }

    /**
     * Регистрирует нового пользователя в системе.
     */
    public void registrationNewUser() {
        System.out.println("Имя пользователя: ");
        String username = scanner.nextLine();
        System.out.println("Пароль пользователя: ");
        String password = scanner.nextLine();

        // Выполняем регистрацию пользователя и обрабатываем результат
        switch (registrationService.registerUser(username, password)) {
            case SUCCESS:
                auditLog.add(new AuditLog(username, "Успешная регистрация пользователя"));
                System.out.println("Регистрация прошла успешно!");
                break;
            case INVALID_USERNAME:
                System.out.println("Ошибка: Некорректное имя пользователя.");
                break;
            case INVALID_PASSWORD:
                System.out.println("Ошибка: Некорректный пароль.");
                break;
            case USERNAME_ALREADY_EXISTS:
                System.out.println("Ошибка: Пользователь с таким именем уже существует.");
                break;
            default:
                System.out.println("Неизвестный статус регистрации.");
        }
    }

    /**
     * Аутентифицирует пользователя в системе.
     *
     * @return Аутентифицированного пользователя или null, если аутентификация не удалась.
     */
    public User authenticateUser() {
        System.out.println("Имя пользователя: ");
        String username = scanner.nextLine();
        System.out.println("Пароль пользователя: ");
        String password = scanner.nextLine();

        // Пытаемся аутентифицировать пользователя и обрабатываем результат
        User authenticatedUser = authService.authenticateUser(username, password);

        if (authenticatedUser != null) {
            auditLog.add(new AuditLog(username, "Вход пользователя"));
            System.out.println("Авторизация успешна!");
            return authenticatedUser;
        } else {
            System.out.println("Ошибка: Некорректные логин или пароль.");
            return null;
        }
    }


    /**
     * Отображает всех пользователей в системе.
     *
     * @param authenticatedUser Администратор, запрашивающий список пользователей.
     */
    public void displayAllUsers(User authenticatedUser) {
        auditLog.add(new AuditLog(authenticatedUser.getUsername(), "Запрос получения всех пользователей"));
        List<User> users = userService.getAllUsers();
        System.out.println(users.toString());
    }

    /**
     * Добавляет новый тип счетчика в каталог.
     *
     * @param authenticatedUser Администратор, добавляющий новый тип счетчика.
     */
    public void addNewMeterType(User authenticatedUser) {
        System.out.println("Введите название для нового счетчика");
        String newMeterType = scanner.nextLine();
        meterTypeCatalogService.addMeterType(newMeterType);
        auditLog.add(new AuditLog(authenticatedUser.getUsername(), "Добавление нового счетчика"));
    }

    /**
     * Добавляет новые показания счетчиков для пользователя.
     *
     * @param authenticatedUser       Аутентифицированный пользователь, добавляющий показания.
     * @param meterTypeCatalogService Каталог типов счетчиков.
     */
    public void addNewMeterReadingsForUser(User authenticatedUser, MeterTypeCatalogService meterTypeCatalogService) {
        List<MeterTypeCatalog> meterTypes = meterTypeCatalogService.getMeterTypes();
        YearMonth date = YearMonth.now();

        for (MeterTypeCatalog type : meterTypes) {
            if (isMeterReadingExists(authenticatedUser, date, type)) {
                System.out.println("Показания для " + type.getMeterType() + " уже были введены в этом месяце.");
                continue;
            }

            try {
                System.out.println(type.getMeterType() + ": ");
                double value = scanner.nextDouble();
                scanner.nextLine();

                MeterReading meterReading = new MeterReading(authenticatedUser, value, date, type);
                meterReadingService.addNew(meterReading);
                auditLog.add(new AuditLog(authenticatedUser.getUsername(),
                        "Добавление показаний для счетчика " + type));
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: Некорректный формат ввода.");
                scanner.nextLine();
            }
        }
    }


    /**
     * Проверяет существование показаний счетчика для пользователя на указанную дату.
     *
     * @param user              Пользователь, для которого проверяется наличие показаний.
     * @param date              Дата, на которую проверяется наличие показаний.
     * @param meterTypeCatalog Каталог типов счетчиков, для которого проверяется наличие показаний.
     * @return true, если показания существуют, иначе false.
     */
    private boolean isMeterReadingExists(User user, YearMonth date, MeterTypeCatalog meterTypeCatalog) {
        return meterReadingService.isMeterReadingExists(user, date, meterTypeCatalog);
    }


    /**
     * Отображает список показаний счетчиков.
     *
     * @param meterReadings Список показаний счетчиков.
     */
    public void displayMeterReadings(List<MeterReading> meterReadings) {
        for (MeterReading meterReading : meterReadings) {
            System.out.printf("Пользователь: %s, Дата: %s, %s: %.2f%n",
                    meterReading.getUser().getUsername(),
                    meterReading.getDate(),
                    meterReading.getMeterTypeCatalog().getMeterType(),
                    meterReading.getValue());
        }
    }

    /**
     * Возвращает пользователя для отображения на основе роли.
     *
     * @param authenticatedUser Аутентифицированный пользователь.
     * @return Пользователь для отображения.
     */
    private User getUserForDisplay(User authenticatedUser) {
        if (authenticatedUser.getRole() == Role.ADMIN) {
            System.out.println("Имя пользователя: ");
            return userService.getUserByUsername(scanner.nextLine());
        } else {
            return authenticatedUser;
        }
    }

    /**
     * Отображает историю подачи показаний для пользователя.
     *
     * @param authenticatedUser Аутентифицированный пользователь.
     */
    public void displayUserMeterReadings(User authenticatedUser) {
        User userToDisplay = getUserForDisplay(authenticatedUser);
        List<MeterReading> userReadings = meterReadingService.getAllForUser(userToDisplay);

        if (userReadings.isEmpty()) {
            System.out.println("Еще нет показаний.");
        }

        auditLog.add(new AuditLog(authenticatedUser.getUsername(),
                "Просмотр истории подачи показаний пользователя: " + userToDisplay.getUsername()));

        displayMeterReadings(userReadings);
    }

    /**
     * Отображает показания счетчиков для пользователя за указанную дату.
     *
     * @param authenticatedUser Аутентифицированный пользователь.
     */
    public void displayMeterReadingsByUserAndDate(User authenticatedUser) {
        User userToDisplay = getUserForDisplay(authenticatedUser);
        System.out.println("Введите дату в формате YYYY-MM (например, 2023-09): ");
        String inputDate = scanner.nextLine();

        try {
            YearMonth date = YearMonth.parse(inputDate);
            auditLog.add(new AuditLog(authenticatedUser.getUsername(),
                    "Просмотр показаний пользователея: " + userToDisplay.getUsername() +
                            " за дату: " + inputDate));
            List<MeterReading> meterReadings = meterReadingService.getByUserAndDate(userToDisplay, date);

            if (meterReadings.isEmpty()) {
                System.out.println("На указанную дату нет показаний.");
            } else {
                displayMeterReadings(meterReadings);
            }
        } catch (DateTimeParseException e) {
            System.out.println("Ошибка: Некорректный формат даты. Используйте YYYY-MM.");
        }
    }

    /**
     * Получает и отображает актуальные показания счетчиков для пользователя.
     *
     * @param authenticatedUser Аутентифицированный пользователь.
     * @return Список актуальных показаний счетчиков.
     */
    public List<MeterReading> getLatestMeterReadings(User authenticatedUser) {
        User userToDisplay = getUserForDisplay(authenticatedUser);
        List<MeterReading> userReadings = meterReadingService.getLatestMeterReadings(userToDisplay);


        if (userReadings != null) {
            auditLog.add(new AuditLog(authenticatedUser.getUsername(),
                    "Актуальные показания пользователя: " + userToDisplay.getUsername()));
            displayMeterReadings(userReadings);
            return userReadings;
        } else {
            System.out.println("Нет показаний для пользователя " + userToDisplay.getUsername());
            return null;
        }
    }

    /**
     * Записывает в журнал аудита выход пользователя из системы.
     *
     * @param authenticatedUser Аутентифицированный пользователь.
     */
    public void auditUserLogout(User authenticatedUser) {
        auditLog.add(new AuditLog(authenticatedUser.getUsername(), "Выход из системы"));
    }

    /**
     * Отображает журнал аудита.
     */
    public void displayAuditLog() {
        for (AuditLog logEntry : auditLog) {
            System.out.println(logEntry.toString());
        }
    }
}
