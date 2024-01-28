package org.wenant.service.in;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Каталог типов счетчиков.
 * Содержит список предопределенных типов счетчиков и предоставляет методы для добавления новых и получения существующих типов.
 */
public class MeterTypeCatalog {

    private final List<String> meterTypes;

    /**
     * Конструктор класса MeterTypeCatalog.
     * Инициализирует каталог предопределенными типами счетчиков.
     */
    public MeterTypeCatalog() {
        this.meterTypes = new ArrayList<>(Arrays.asList("Холодная вода", "Горячая вода", "Отопление"));
    }

    /**
     * Добавляет новый тип счетчика в каталог.
     *
     * @param meterType Новый тип счетчика для добавления.
     */
    public void addMeterType(String meterType) {
        meterTypes.add(meterType);
    }

    /**
     * Получает список всех доступных типов счетчиков в каталоге.
     *
     * @return Список строк, представляющих типы счетчиков.
     */
    public List<String> getMeterTypes() {
        return new ArrayList<>(meterTypes);
    }
}
