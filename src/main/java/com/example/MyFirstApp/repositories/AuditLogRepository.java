package com.example.MyFirstApp.repositories;

import com.example.MyFirstApp.entities.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    @Query("SELECT a FROM AuditLog a")
    List<AuditLog> findAllLogs();
}
