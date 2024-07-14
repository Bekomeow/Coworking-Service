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
    List<Audit> showAllAudits();

    /**
     * Performs an audit for the specified login, action type, and audit type.
     *
     * @param login      the login associated with the action
     * @param actionType the type of action being audited
     * @param auditType  the result of the audit (SUCCESS or FAIL)
     * @return
     */
    Audit audit(String login, ActionType actionType, AuditType auditType);
}
