package org.beko.service.impl;

import lombok.RequiredArgsConstructor;
import org.beko.dao.AuditDAO;
import org.beko.model.Audit;
import org.beko.model.types.ActionType;
import org.beko.model.types.AuditType;
import org.beko.service.AuditService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link AuditService} interface.
 */
@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditDAO auditDAO;

    /**
     * Saves an audit record.
     *
     * @param audit the audit record to save
     */
    public Audit save(Audit audit) {
        return auditDAO.save(audit);
    }

    /**
     * Retrieves a list of all audit records.
     *
     * @return the list of all audit records
     */
    @Override
    public List<Audit> showAllAudits() {
        return auditDAO.findAll();
    }

    /**
     * Performs an audit for a specific action.
     *
     * @param login      the login associated with the action
     * @param actionType the type of action
     * @param auditType  the type of audit (SUCCESS or FAIL)
     * @return
     */
    @Override
    public Audit audit(String login, ActionType actionType, AuditType auditType) {
        Audit audit = Audit.builder()
                .login(login)
                .auditType(auditType)
                .actionType(actionType)
                .build();

        return save(audit);
    }
}