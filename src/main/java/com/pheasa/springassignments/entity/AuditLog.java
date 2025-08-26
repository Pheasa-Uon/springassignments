package com.pheasa.springassignments.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;

@Entity
@Table(name = "audit_log")
@Data
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // <-- Primary key
    private Long id;

    private String processId;
    private String username;
    private String className;
    private String methodName;

    @Column(length = 2000) // prevent truncation for long args
    private String parameters;

    @Column(length = 2000) // prevent truncation for long results
    private String returnValue;

    private Long executionTimeMs;
    private Instant timestamp;
    private boolean success;

    @Column(length = 1000)
    private String errorMessage;
}
