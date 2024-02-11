package org.wenant.service.in;

import org.wenant.domain.dto.MeterReadingDto;
import org.wenant.domain.dto.ReadingDto;
import org.wenant.domain.entity.MeterReading;
import org.wenant.domain.entity.MeterTypeCatalog;
import org.wenant.domain.entity.User;
import org.wenant.domain.repository.MeterReadingRepository;
import org.wenant.mapper.MeterReadingMapper;
import org.wenant.service.UserService;

import java.time.YearMonth;
import java.util.List;

/**
 * Сервис для управления показаниями счетчиков.
 * Обеспечивает добавление новых показаний, получение и анализ показаний для пользователя.
 */
public class MeterReadingService {

    private final MeterReadingRepository meterReadingRepository;
    private final MeterTypeCatalogService meterTypeCatalogService;
    private final UserService userService;

    public MeterReadingService(MeterReadingRepository meterReadingRepository, MeterTypeCatalogService meterTypeCatalogService, UserService userService) {
        this.meterReadingRepository = meterReadingRepository;
        this.meterTypeCatalogService = meterTypeCatalogService;
        this.userService = userService;
    }


//    public void addNew(MeterReading meterReading) {
//        meterReadingRepository.save(meterReading);
//    }meterReadingDto


    public void addNew(ReadingDto readingDto, String username) {
        MeterTypeCatalog meterType = meterTypeCatalogService.getMeterTypeById(readingDto.getTypeId());
        if (meterType != null) {
            User user = userService.getUserByUsername(username);
            YearMonth date = YearMonth.now();
            MeterReading newMeterReading = new MeterReading(user, readingDto.getValue(), date, meterType);
            if (!meterReadingRepository.isMeterReadingExists(user, date, meterType)) {
                meterReadingRepository.save(newMeterReading);
            } else {
                throw new IllegalArgumentException("Already exists");
            }

        } else {
            throw new IllegalArgumentException("Invalid meterType");
        }

    }

    /**
     * Получает все показания счетчиков для конкретного пользователя.
     *
     * @param username Пользователь, для которого нужно получить показания счетчиков.
     * @return Список MeterReading, содержащий показания счетчиков для указанного пользователя.
     */
    public List<MeterReadingDto> getAllForUser(String username) {
        User user = userService.getUserByUsername(username);
        return MeterReadingMapper.INSTANCE.meterReadingListToDtoList(meterReadingRepository.getAllForUser(user));
    }

    /**
     * Получает показания счетчиков для конкретного пользователя на определенную дату.
     *
     * @param username Пользователь, для которого нужно получить показания счетчиков.
     * @param date     Дата, на которую нужно получить показания счетчиков.
     * @return Список MeterReading, представляющий показания счетчиков на указанную дату.
     */
    public List<MeterReadingDto> getByUserAndDate(String username, YearMonth date) {
        User user = userService.getUserByUsername(username);
        return MeterReadingMapper.INSTANCE.meterReadingListToDtoList(meterReadingRepository.getByUserAndDate(user, date));
    }

    /**
     * Получает актуальные показания счетчиков для конкретного пользователя.
     *
     * @param username Пользователь, для которого нужно получить актуальные показания счетчиков.
     * @return Список MeterReading, содержащий актуальные показания счетчиков для указанного пользователя.
     */
    public List<MeterReadingDto> getLatestMeterReadings(String username) {
        User user = userService.getUserByUsername(username);
        return MeterReadingMapper.INSTANCE.meterReadingListToDtoList(meterReadingRepository.getLatestMeterReadings(user));
    }

    /**
     * Проверяет существование показаний счетчика для конкретного пользователя, на указанную дату и тип счетчика.
     *
     * @param user             Пользователь, для которого выполняется проверка.
     * @param date             Дата, для которой выполняется проверка.
     * @param meterTypeCatalog Тип счетчика, для которого выполняется проверка.
     * @return true, если показания существуют, в противном случае - false.
     */
    public boolean isMeterReadingExists(User user, YearMonth date, MeterTypeCatalog meterTypeCatalog) {
        return meterReadingRepository.isMeterReadingExists(user, date, meterTypeCatalog);
    }
}
