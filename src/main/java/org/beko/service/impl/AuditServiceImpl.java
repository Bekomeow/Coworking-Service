package org.beko.service.impl;

import lombok.RequiredArgsConstructor;
import org.beko.dao.AuditDAO;
import org.beko.dao.impl.AuditDAOImpl;
import org.beko.model.Audit;
import org.beko.model.types.ActionType;
import org.beko.model.types.AuditType;
import org.beko.service.AuditService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing audits.
 */
@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {
    private final AuditDAO auditDAO;

    /**
     * Saves an audit record.
     *
     * @param audit the audit record to save
     * @return the saved audit record
     */
    public Audit save(Audit audit) {
        return auditDAO.save(audit);
    }

    /**
     * Retrieves a list of all audit records.
     *
     * @return the list of all audit records
     */
    public List<Audit> getAllAudits() {
        return auditDAO.findAll();
    }

    /**
     * Performs an audit for a specific action.
     *
     * @param username      the login associated with the action
     * @param actionType the type of action
     * @param auditType  the type of audit (SUCCESS or FAIL)
     * @return
     */
    public Audit record(String username, ActionType actionType, AuditType auditType) {
        Audit audit = Audit.builder()
                .username(username)
                .actionType(actionType)
                .auditType(auditType)
                .build();

        return save(audit);
    }
}
