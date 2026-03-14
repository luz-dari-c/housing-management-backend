// Ruta: com.backend.housing.application.dto.admin.PaymentAdminResponse.java
package com.backend.housing.application.dto.admin;

import java.time.LocalDateTime;

public class PaymentAdminResponse {
    private Long id;
    private String transactionId;
    private double amount;
    private String concept; // PUBLICATION, RENT, COMMISSION
    private String userEmail;
    private String userName;
    private Long propertyId;
    private String propertyTitle;
    private String status; // PENDING, COMPLETED, FAILED, REFUNDED
    private LocalDateTime paymentDate;
    private String paymentMethod;
    
    public PaymentAdminResponse() {}
    
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    
    public String getConcept() { return concept; }
    public void setConcept(String concept) { this.concept = concept; }
    
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    
    public Long getPropertyId() { return propertyId; }
    public void setPropertyId(Long propertyId) { this.propertyId = propertyId; }
    
    public String getPropertyTitle() { return propertyTitle; }
    public void setPropertyTitle(String propertyTitle) { this.propertyTitle = propertyTitle; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}