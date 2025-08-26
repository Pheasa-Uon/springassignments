package com.pheasa.springassignments.configuration;

import com.pheasa.springassignments.configuration.annotations.AuditFilter;
import com.pheasa.springassignments.configuration.annotations.MyRetryable;
import com.pheasa.springassignments.entity.AuditLog;
import com.pheasa.springassignments.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditFilterAspect {

    private final AuditLogRepository auditLogRepository;
    private static final AtomicLong COUNTER = new AtomicLong(0);

    @Around("@annotation(auditFilter) || @annotation(myRetryable)")
    public Object auditAndRetry(ProceedingJoinPoint joinPoint, AuditFilter auditFilter, MyRetryable myRetryable) throws Throwable {

        // Generate processId
        long requestNumber = COUNTER.incrementAndGet();
        String processId = requestNumber + "-" + UUID.randomUUID().toString().replace("-", "");
        MDC.put("processId", processId);

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String params = Arrays.toString(joinPoint.getArgs());

        AuditLog auditLog = new AuditLog();
        auditLog.setProcessId(processId);
        auditLog.setClassName(className);
        auditLog.setMethodName(methodName);
        auditLog.setParameters(params);
        auditLog.setTimestamp(Instant.now());

        int maxAttempts = myRetryable != null ? myRetryable.maxRetries() : 1;
        long delay = myRetryable != null ? myRetryable.retryDelay() : 0;
        Class<? extends Throwable>[] retryFor = myRetryable != null ? myRetryable.retryFor() : new Class[]{};
        Class<? extends Throwable>[] noRetryFor = myRetryable != null ? myRetryable.noRetryFor() : new Class[]{};

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            Instant startTime = Instant.now();
            try {
                log.info("[{}] Executing {}.{} attempt {}/{}", processId, className, methodName, attempt, maxAttempts);
                Object result = joinPoint.proceed();

                long spent = Instant.now().toEpochMilli() - startTime.toEpochMilli();
                auditLog.setExecutionTimeMs(spent);
                auditLog.setSuccess(true);
                auditLog.setReturnValue(result != null ? result.toString() : "null");
                auditLogRepository.save(auditLog);

                log.info("[{}] Success {}.{} in {} ms", processId, className, methodName, spent);
                return result;

            } catch (Throwable ex) {
                long spent = Instant.now().toEpochMilli() - startTime.toEpochMilli();
                auditLog.setExecutionTimeMs(spent);
                auditLog.setSuccess(false);
                auditLog.setErrorMessage(ex.getMessage());
                auditLogRepository.save(auditLog);

                boolean shouldNotRetry = Arrays.stream(noRetryFor).anyMatch(c -> c.isAssignableFrom(ex.getClass()));
                boolean shouldRetry = retryFor.length == 0 || Arrays.stream(retryFor).anyMatch(c -> c.isAssignableFrom(ex.getClass()));

                if (shouldNotRetry || attempt == maxAttempts || !shouldRetry) {
                    log.error("[{}] {}.{} failed after {} attempt(s): {}", processId, className, methodName, attempt, ex.getMessage());
                    throw ex;
                }

                log.warn("[{}] {}.{} failed on attempt {}/{}. Retrying in {}ms. Error: {}", processId, className, methodName, attempt, maxAttempts, delay, ex.getMessage());
                Thread.sleep(delay);
            }
        }

        MDC.remove("processId");
        throw new RuntimeException("Retry logic reached unreachable point");
    }
}