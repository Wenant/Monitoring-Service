package org.wenant.service.in;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.wenant.domain.entity.MeterReading;
import org.wenant.domain.entity.MeterTypeCatalog;
import org.wenant.domain.entity.User;
import org.wenant.domain.repository.MeterReadingRepository;

import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MeterReadingServiceTest {

    @Mock
    private MeterReadingRepository meterReadingRepository;

    @InjectMocks
    private MeterReadingService meterReadingService;

    @Test
    public void testAddNew() {
        User user = new User(1L, "user1", "password123", User.Role.USER);
        MeterTypeCatalog meterTypeCatalog = new MeterTypeCatalog(1l, "Вода");
        MeterReading meterReading =
                new MeterReading(user, 1.0, YearMonth.now(), meterTypeCatalog);

        meterReadingService.addNew(meterReading);

        verify(meterReadingRepository, times(1)).save(meterReading);
    }




}
