
package com.backend.housing.application.dto.admin;

public class DashboardSummary {
    private long totalUsers;
    private long totalProperties;
    private long pendingProperties;
    private long activeContracts;
    private double monthlyIncome;
    private double totalIncome;
    
    public DashboardSummary() {}
    
    public DashboardSummary(long totalUsers, long totalProperties, long pendingProperties, 
                           long activeContracts, double monthlyIncome, double totalIncome) {
        this.totalUsers = totalUsers;
        this.totalProperties = totalProperties;
        this.pendingProperties = pendingProperties;
        this.activeContracts = activeContracts;
        this.monthlyIncome = monthlyIncome;
        this.totalIncome = totalIncome;
    }
    
    
    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
    
    public long getTotalProperties() { return totalProperties; }
    public void setTotalProperties(long totalProperties) { this.totalProperties = totalProperties; }
    
    public long getPendingProperties() { return pendingProperties; }
    public void setPendingProperties(long pendingProperties) { this.pendingProperties = pendingProperties; }
    
    public long getActiveContracts() { return activeContracts; }
    public void setActiveContracts(long activeContracts) { this.activeContracts = activeContracts; }
    
    public double getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(double monthlyIncome) { this.monthlyIncome = monthlyIncome; }
    
    public double getTotalIncome() { return totalIncome; }
    public void setTotalIncome(double totalIncome) { this.totalIncome = totalIncome; }
}