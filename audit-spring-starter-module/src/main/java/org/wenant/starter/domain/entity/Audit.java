package org.wenant.starter.domain.entity;

import java.sql.Timestamp;

public class Audit {
    private final Long id;
    private final Long userId;
    private final String action;
    private final Timestamp date;
    private final String tableName;
    private final String newValue;

    public Audit(Long id, Long userId, String action, Timestamp date, String tableName, String newValue) {
        this.id = id;
        this.userId = userId;
        this.action = action;
        this.date = date;
        this.tableName = tableName;
        this.newValue = newValue;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getAction() {
        return action;
    }

    public Timestamp getDate() {
        return date;
    }

    public String getTableName() {
        return tableName;
    }

    public String getNewValue() {
        return newValue;
    }
}
