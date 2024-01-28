package org.wenant;

import org.wenant.domain.entity.AuditLog;
import org.wenant.domain.entity.MeterReading;
import org.wenant.domain.entity.User;
import org.wenant.domain.repository.InMemoryMeterReadingRepository;
import org.wenant.domain.repository.InMemoryUserRepository;
import org.wenant.domain.repository.MeterReadingRepository;
import org.wenant.domain.repository.UserRepository;
import org.wenant.service.*;
import org.wenant.service.in.AuthService;
import org.wenant.service.in.MeterReadingService;
import org.wenant.service.in.MeterTypeCatalog;
import org.wenant.service.in.RegistrationService;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Класс, представляющий консольное приложение для работы с системой учета показаний счетчиков.
 */
public class ConsoleApp {
    private final UserService userService;
    private final MeterReadingService meterReadingService;
    private final RegistrationService registrationService;
    private final AuthService authService;
    private final MeterTypeCatalog meterTypeCatalog;
    private final List<AuditLog> auditLog = new ArrayList<>();
    private final Scanner scanner;

    /**
     * Конструктор класса ConsoleApp.
     *
     * @param userService           Сервис пользователей.
     * @param meterReadingService   Сервис показаний счетчиков.
     * @param registrationService   Сервис регистрации пользователей.
     * @param authService           Сервис аутентификации пользователей.
     * @param meterTypeCatalog      Каталог типов счетчиков.
     * @param scanner               Объект Scanner для ввода с консоли.
     */
    public ConsoleApp(UserService userService, MeterReadingService meterReadingService, RegistrationService registrationService, AuthService authService, MeterTypeCatalog meterTypeCatalog, Scanner scanner) {
        this.userService = userService;
        this.meterReadingService = meterReadingService;
        this.registrationService = registrationService;
        this.authService = authService;
        this.meterTypeCatalog = meterTypeCatalog;
        this.scanner = scanner;
    }

    /**
     * Точка входа в приложение.
     */
    public static void main(String[] args) {
        UserRepository userRepository = new InMemoryUserRepository();
        UserService userService = new UserService(userRepository);

        MeterReadingRepository meterReadingRepository = new InMemoryMeterReadingRepository();
        MeterReadingService meterReadingService = new MeterReadingService(meterReadingRepository);

        RegistrationService registrationService = new RegistrationService(userRepository);
        AuthService authService = new AuthService(userService);
        MeterTypeCatalog meterTypeCatalog = new MeterTypeCatalog();
        Scanner scanner = new Scanner(System.in);

        ConsoleApp consoleApp = new ConsoleApp(userService, meterReadingService, registrationService, authService, meterTypeCatalog, scanner);


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
                        if ("admin".equals(authenticatedUser.getRole())) {
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
                        } else if ("user".equals(authenticatedUser.getRole())) {
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
                                        consoleApp.addNewMeterReadingsForUser(authenticatedUser, meterTypeCatalog);
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
        meterTypeCatalog.addMeterType(newMeterType);
        auditLog.add(new AuditLog(authenticatedUser.getUsername(), "Добавление нового счетчика"));
    }

    /**
     * Добавляет новые показания счетчиков для пользователя.
     *
     * @param authenticatedUser Аутентифицированный пользователь, добавляющий показания.
     * @param meterTypeCatalog  Каталог типов счетчиков.
     */
    public void addNewMeterReadingsForUser(User authenticatedUser, MeterTypeCatalog meterTypeCatalog) {
        List<String> meterTypes = meterTypeCatalog.getMeterTypes();
        YearMonth date = YearMonth.now();

        for (String meterType : meterTypes) {
            if (isMeterReadingExists(authenticatedUser, date, meterType)) {
                System.out.println("Показания для " + meterType + " уже были введены в этом месяце.");
                continue;
            }

            try {
                System.out.println(meterType + ": ");
                double value = scanner.nextDouble();
                scanner.nextLine();

                MeterReading meterReading = new MeterReading(authenticatedUser, value, date, meterType);
                meterReadingService.addNew(meterReading);
                auditLog.add(new AuditLog(authenticatedUser.getUsername(),
                        "Добавление показаний для счетчика " + meterType));
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: Некорректный формат ввода.");
                scanner.nextLine();
            }
        }
    }

    /**
     * Проверяет, существуют ли уже показания для указанного пользователя, даты и типа счетчика.
     *
     * @param user      Пользователь.
     * @param date      Дата.
     * @param meterType Тип счетчика.
     * @return true, если показания уже существуют, иначе false.
     */
    private boolean isMeterReadingExists(User user, YearMonth date, String meterType) {
        return meterReadingService.isMeterReadingExists(user, date, meterType);
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
                    meterReading.getMeterType(),
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
        if ("admin".equals(authenticatedUser.getRole())) {
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
        Map<YearMonth, List<MeterReading>> userReadings = meterReadingService.getAllForUser(userToDisplay);

        if (userReadings.isEmpty()) {
            System.out.println("Еще нет показаний.");
        }

        auditLog.add(new AuditLog(authenticatedUser.getUsername(),
                "Просмотр истории подачи показаний пользователея: " + userToDisplay.getUsername()));
        for (List<MeterReading> meterReadings : userReadings.values()) {
            displayMeterReadings(meterReadings);
        }
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
        Map<YearMonth, List<MeterReading>> userReadings = meterReadingService.getLatestMeterReadings(userToDisplay);


        YearMonth latestMonth = userReadings.keySet().stream()
                .max(Comparator.naturalOrder())
                .orElse(null);

        if (latestMonth != null) {
            auditLog.add(new AuditLog(authenticatedUser.getUsername(),
                    "Получение актуальных показаний пользователя: " + userToDisplay.getUsername()));
            List<MeterReading> latestMeterReadings = userReadings.getOrDefault(latestMonth, Collections.emptyList());
            displayMeterReadings(latestMeterReadings);
            return latestMeterReadings;
        } else {
            System.out.println("Нет показаний для пользователя " + userToDisplay.getUsername());
            return Collections.emptyList();
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
