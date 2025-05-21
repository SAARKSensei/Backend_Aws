//package com.sensei.backend.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class OTPVerificationDTO {
//    private String dtCode;
//    private String otpCode;
//    private String phoneNumber;
//    private String email;
//}


package com.sensei.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OTPVerificationDTO {
   // private String idToken;

    private String email;
//    private String phoneNumber;
    private String otpCode; // In Cognito, OTP is treated as a password
}
