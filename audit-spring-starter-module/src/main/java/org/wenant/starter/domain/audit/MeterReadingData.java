package org.wenant.starter.domain.audit;

import java.time.YearMonth;

public interface MeterReadingData {
    Long getUserId();

    double getValue();

    YearMonth getDate();

    String getMeterType();
}
