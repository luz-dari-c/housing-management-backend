
package com.backend.housing.application.dto.admin;

import java.util.Map;

public class SystemMetricsResponse {
    private long totalUsers;
    private Map<String, Long> usersByRole;
    private long totalProperties;
    private long pendingProperties;
    private long approvedProperties;
    private long rentedProperties;
    private long soldProperties;
    private long rejectedProperties;
    private double totalAdminIncome;
    private double monthlyAdminIncome;
    private long activeContracts;
    private long totalPayments;
    
   
    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
    
    public Map<String, Long> getUsersByRole() { return usersByRole; }
    public void setUsersByRole(Map<String, Long> usersByRole) { this.usersByRole = usersByRole; }
    
    public long getTotalProperties() { return totalProperties; }
    public void setTotalProperties(long totalProperties) { this.totalProperties = totalProperties; }
    
    public long getPendingProperties() { return pendingProperties; }
    public void setPendingProperties(long pendingProperties) { this.pendingProperties = pendingProperties; }
    
    public long getApprovedProperties() { return approvedProperties; }
    public void setApprovedProperties(long approvedProperties) { this.approvedProperties = approvedProperties; }
    
    public long getRentedProperties() { return rentedProperties; }
    public void setRentedProperties(long rentedProperties) { this.rentedProperties = rentedProperties; }
    
    public long getSoldProperties() { return soldProperties; }
    public void setSoldProperties(long soldProperties) { this.soldProperties = soldProperties; }
    
    public long getRejectedProperties() { return rejectedProperties; }
    public void setRejectedProperties(long rejectedProperties) { this.rejectedProperties = rejectedProperties; }
    
    public double getTotalAdminIncome() { return totalAdminIncome; }
    public void setTotalAdminIncome(double totalAdminIncome) { this.totalAdminIncome = totalAdminIncome; }
    
    public double getMonthlyAdminIncome() { return monthlyAdminIncome; }
    public void setMonthlyAdminIncome(double monthlyAdminIncome) { this.monthlyAdminIncome = monthlyAdminIncome; }
    
    public long getActiveContracts() { return activeContracts; }
    public void setActiveContracts(long activeContracts) { this.activeContracts = activeContracts; }
    
    public long getTotalPayments() { return totalPayments; }
    public void setTotalPayments(long totalPayments) { this.totalPayments = totalPayments; }
}