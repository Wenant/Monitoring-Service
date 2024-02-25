package org.wenant.starter.domain.repository;

import org.wenant.starter.domain.entity.Audit;

public interface AuditRepository {
    void save(Audit audit);

    Long getUserIdByUsername(String username);
}

