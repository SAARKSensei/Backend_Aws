/*package com.sensei.backend.dto;



import lombok.Data;

@Data
public class PaymentRequestDTO {
	private double amount;
    private String currency;
    private String receipt;
    private String userId;  // ✅ Add this field
    
}
*/
package com.sensei.backend.application.dto;

public class PaymentRequestDTO {
    
    private String userId;
    private double amount;
    private String orderId;
    private String paymentId;
    private String signature;

    // Constructors
    public PaymentRequestDTO() {}

    public PaymentRequestDTO(String userId, double amount, String orderId, String paymentId, String signature) {
        this.userId = userId;
        this.amount = amount;
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.signature = signature;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "PaymentRequestDTO{" +
                "userId='" + userId + '\'' +
                ", amount=" + amount +
                ", orderId='" + orderId + '\'' +
                ", paymentId='" + paymentId + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}