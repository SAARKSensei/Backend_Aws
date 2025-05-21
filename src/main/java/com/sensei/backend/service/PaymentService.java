package com.sensei.backend.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.sensei.backend.dto.PaymentRequestDTO;
import com.sensei.backend.entity.Payment;
import com.sensei.backend.exception.PaymentException;
import com.sensei.backend.repository.PaymentRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class PaymentService {

    @Autowired
    private RazorpayClient razorpayClient;

    @Autowired
    private PaymentRepository paymentRepository;

    @Value("${razorpay.key.secret}")
    private String secret;


    // Method to create an order
    public Payment createOrder(PaymentRequestDTO requestDTO) {
        try {
            JSONObject options = new JSONObject();
            options.put("amount", requestDTO.getAmount() * 100); // Amount in paise
            options.put("currency", requestDTO.getCurrency());
            options.put("receipt", requestDTO.getReceipt());

            Order order = razorpayClient.orders.create(options);
            System.out.println(order+"this is order");
            Payment payment = new Payment();
            payment.setRazorpayOrderId(order.get("id"));
            payment.setAmount(requestDTO.getAmount());
            payment.setPaymentDate(LocalDateTime.now());
            return paymentRepository.save(payment);

        } catch (RazorpayException e) {
            throw new PaymentException("Failed to create Razorpay order");
        }
    }

    // Method to verify and capture payment
    public void verifyAndCapturePayment(String orderId, String paymentId, String signature) {
        Payment payment = paymentRepository.findByRazorpayOrderId(orderId)
                .orElseThrow(() -> new PaymentException("Order not found with ID: " + orderId));

        if (isSignatureValid(orderId, paymentId, signature)) {

            // Signature is valid, proceed to capture the payment
            System.out.println("Signature is valid. Updating payment details.");
            payment.setRazorpayPaymentId(paymentId);
            payment.setRazorpaySignature(signature);
            payment.setPaymentDate(LocalDateTime.now());  // Update with actual payment time

            paymentRepository.save(payment);
        } else {
            System.out.println("Invalid signature detected!");
            throw new PaymentException("Invalid payment signature");
        }
    }

    // Helper method to validate the signature using HMAC-SHA256
    private boolean isSignatureValid(String orderId, String paymentId, String signature) {
        try {
//            String secret = "2ce0gS8tLtvcTNgw2YlI6rc2"; // Ideally, get it from application properties
            String payload = orderId + "|" + paymentId;

            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);

            byte[] hash = mac.doFinal(payload.getBytes());
//            String generatedSignature = Base64.getEncoder().encodeToString(hash);
//
//            return generatedSignature.equals(signature);
            String generatedSignature = bytesToHex(hash); // Ensure HEX encoding!


            // Print debug information
//            System.out.println("🔍 Expected Signature (Generated): " + generatedSignature);
//            System.out.println("🔍 Received Signature (From Razorpay): " + signature);

            return generatedSignature.equalsIgnoreCase(signature);
        } catch (Exception e) {
            throw new PaymentException("Error while validating signature");
        }
    }
    // Helper function to convert bytes to hex
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
