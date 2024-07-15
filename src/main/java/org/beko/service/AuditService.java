package org.beko.service;

import org.beko.model.Audit;
import org.beko.model.types.ActionType;
import org.beko.model.types.AuditType;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The service interface for auditing functionality.
 */
@Service
public interface AuditService {
    /**
     * Retrieves a list of all audit records.
     *
     * @return the list of audit records
     */
    List<Audit> getAllAudits();

    Audit save(Audit audit);

    Audit record(String username, ActionType actionType, AuditType auditType);
}
