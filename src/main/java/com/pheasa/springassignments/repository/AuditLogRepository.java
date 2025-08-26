package com.pheasa.springassignments.repository;

import com.pheasa.springassignments.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

}
