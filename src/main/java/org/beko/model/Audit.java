package org.beko.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.beko.model.types.ActionType;
import org.beko.model.types.AuditType;

/**
 * Represents an audit log entry capturing information about a user's actions.
 * Each audit entry includes a unique identifier, user login, audit type, and action type.
 *
 * @author qaisar
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Audit {
    /**
     * The unique identifier for the audit entry.
     */
    private Long id;
    /**
     * The login of the user associated with the audit entry.
     */
    private String login;
    /**
     * The type of audit, indicating the success or failure of an action.
     */
    private AuditType auditType;
    /**
     * The type of action performed by the user.
     */
    private ActionType actionType;
}