package org.beko.aspects;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.beko.annotations.Auditable;
import org.beko.model.Audit;
import org.beko.model.types.ActionType;
import org.beko.model.types.AuditType;
import org.beko.service.AuditService;

@Aspect
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditService auditService;

    @Pointcut("(within(@org.beko.annotations.Auditable *) || execution(@org.beko.annotations.Auditable * *(..))) && execution(* *(..))")
    public void annotatedByAuditable() {
    }

    @Around("annotatedByAuditable()")
    public Audit audit(ProceedingJoinPoint pjp) {
        var methodSignature = (MethodSignature) pjp.getSignature();

        Auditable audit = methodSignature.getMethod().getAnnotation(Auditable.class);
        ActionType actionType = audit.actionType();
        String payload = audit.login();
        if (payload.isEmpty()) {
            payload = audit.userId();
        }

        return auditService.audit(payload, actionType, AuditType.SUCCESS);
    }

    @AfterThrowing(pointcut = "auditPointcut() && @annotation(audit)")
    public void auditFailure(ProceedingJoinPoint pjp) {
        var methodSignature = (MethodSignature) pjp.getSignature();

        Auditable audit = methodSignature.getMethod().getAnnotation(Auditable.class);
        ActionType actionType = audit.actionType();
        String payload = audit.login();
        if (payload.isEmpty()) {
            payload = audit.userId();
        }

        auditService.audit(audit.login(), actionType, AuditType.FAIL);
    }
}