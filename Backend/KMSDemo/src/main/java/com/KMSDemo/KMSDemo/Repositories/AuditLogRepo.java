package com.KMSDemo.KMSDemo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.KMSDemo.KMSDemo.Entities.AuditLog;

@Repository
public interface AuditLogRepo extends JpaRepository<AuditLog,Long>{

}
