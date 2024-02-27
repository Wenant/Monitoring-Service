package org.wenant.service.in;

import org.springframework.stereotype.Service;
import org.wenant.domain.dto.MeterReadingDto;
import org.wenant.domain.dto.ReadingDto;
import org.wenant.domain.model.MeterReading;
import org.wenant.domain.model.MeterTypeCatalog;
import org.wenant.domain.model.User;
import org.wenant.domain.repository.interfaces.MeterReadingRepository;
import org.wenant.mapper.MeterReadingMapper;
import org.wenant.service.UserService;

import java.time.YearMonth;
import java.util.List;

/**
 * Сервис для управления показаниями счетчиков.
 * Обеспечивает добавление новых показаний, получение и анализ показаний для пользователя.
 */
@Service
public class MeterReadingService {

    private final MeterReadingRepository meterReadingRepository;
    private final MeterTypeCatalogService meterTypeCatalogService;
    private final UserService userService;
    private final MeterReadingMapper meterReadingMapper;

    public MeterReadingService(MeterReadingRepository meterReadingRepository,
                               MeterTypeCatalogService meterTypeCatalogService,
                               UserService userService,
                               MeterReadingMapper meterReadingMapper) {
        this.meterReadingRepository = meterReadingRepository;
        this.meterTypeCatalogService = meterTypeCatalogService;
        this.userService = userService;
        this.meterReadingMapper = meterReadingMapper;
    }


    public MeterReadingStatus addNewReading(ReadingDto readingDto, String username) {
        MeterTypeCatalog meterType = meterTypeCatalogService.getMeterTypeById(readingDto.getTypeId());
        if (meterType != null) {
            User user = userService.getUserByUsername(username);
            YearMonth date = YearMonth.now();
            MeterReading newMeterReading = new MeterReading(user, readingDto.getValue(), date, meterType);
            if (!meterReadingRepository.isMeterReadingExists(user, date, meterType)) {
                meterReadingRepository.addMeterReading(newMeterReading);
                return MeterReadingStatus.SUCCESS;
            } else {
                return MeterReadingStatus.ALREADY_EXISTS;
            }

        } else {
            return MeterReadingStatus.INVALID_TYPE_ID;
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
        return meterReadingMapper.meterReadingListToDtoList(meterReadingRepository.getAllForUser(user));
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
        return meterReadingMapper.meterReadingListToDtoList(meterReadingRepository.getByUserAndDate(user, date));
    }

    /**
     * Получает актуальные показания счетчиков для конкретного пользователя.
     *
     * @param username Пользователь, для которого нужно получить актуальные показания счетчиков.
     * @return Список MeterReading, содержащий актуальные показания счетчиков для указанного пользователя.
     */
    public List<MeterReadingDto> getLatestMeterReadings(String username) {
        User user = userService.getUserByUsername(username);
        return meterReadingMapper.meterReadingListToDtoList(meterReadingRepository.getLatestMeterReadings(user));
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

    public enum MeterReadingStatus {
        SUCCESS,
        ALREADY_EXISTS,
        INVALID_TYPE_ID,
        INVALID_TOKEN
    }
}
