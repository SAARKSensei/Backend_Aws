//package com.sensei.backend.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class OTPRequestDTO {
//    private String dtCode;
//    private String phoneNumber;
//    private String email;
//    private String orderId;
//    private int otpTTL;
//    private int otpLength;
//    private String channel;
//}

package com.sensei.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OTPRequestDTO {
//    private String phoneNumber;
    private String email;  // Updated to accept email instead of phone
}


