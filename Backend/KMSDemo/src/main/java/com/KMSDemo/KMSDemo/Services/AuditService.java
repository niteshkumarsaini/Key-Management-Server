package com.KMSDemo.KMSDemo.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.KMSDemo.KMSDemo.Entities.AuditLog;
import com.KMSDemo.KMSDemo.Repositories.AuditLogRepo;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;




@Service
public class AuditService {
	
	@Autowired
	private AuditLogRepo auditRepo;
	
	

    public void logEvent(String action, HttpServletRequest request, String result) {
        logEvent(action, request, result, null);
    }

    public void logEvent(String action, HttpServletRequest request, String result, String details) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getName();
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        String ipAddress = request.getRemoteAddr();

        AuditLog auditLog = new AuditLog();
        auditLog.setUsername(username);
        auditLog.setRole(role);
        auditLog.setAction(action);
        auditLog.setIpAddress(ipAddress);
        auditLog.setResult(result);
        auditLog.setDetails(details);

        auditRepo.save(auditLog);
    }
	
	
	

}
