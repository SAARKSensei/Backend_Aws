//package com.sensei.backend.config;
//
//import com.sensei.backend.security.CognitoLogoutHandler;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        CognitoLogoutHandler cognitoLogoutHandler = new CognitoLogoutHandler();
//
//        http.csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(authz -> authz
//                        .requestMatchers(new AntPathRequestMatcher("/api/**")).permitAll()
//                        .anyRequest().authenticated())
//                .oauth2Login(Customizer.withDefaults()) // Enable OAuth2 login with AWS Cognito
//                .logout(logout -> logout
//                        .logoutSuccessHandler(cognitoLogoutHandler)); // Handle logout with Cognito
//
//        return http.build();
//    }
//}


//package com.sensei.backend.config;
//
//import com.sensei.backend.security.CognitoLogoutHandler;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.cors.CorsConfigurationSource;
//
//import java.util.List;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        CognitoLogoutHandler cognitoLogoutHandler = new CognitoLogoutHandler();
//
//        http
//                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ Enable CORS
//                .csrf(csrf -> csrf.disable()) // ❌ Disable CSRF for APIs
//                .authorizeHttpRequests(authz -> authz
//                        .requestMatchers(new AntPathRequestMatcher("/api/**")).permitAll()
//                        .anyRequest().authenticated()
//                )
//                .oauth2Login(Customizer.withDefaults()) // Enable OAuth2 login with AWS Cognito
//                .logout(logout -> logout
//                        .logoutSuccessHandler(cognitoLogoutHandler) // Handle logout with Cognito
//                );
//
//        return http.build();
//    }
//
//    // ✅ Define a CORS Configuration Bean
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOriginPatterns(List.of("http://localhost:3000", "https://sensei-website-preview.vercel.app/")); // ✅ Allows all origins
//        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        configuration.setAllowedHeaders(List.of("*"));
//        configuration.setAllowCredentials(true); // ✅ Required if using authentication
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
//}
// package com.sensei.backend.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
// import org.springframework.web.cors.CorsConfigurationSource;

// import java.util.List;

// @Configuration
// public class SecurityConfig {

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//                 .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ Enable the new policy
//                 .csrf(csrf -> csrf.disable()) // ❌ Disable strict checking for APIs
//                 .authorizeHttpRequests(authz -> authz
//                         .requestMatchers(new AntPathRequestMatcher("/api/**")).permitAll() // Allow public access to API
//                         .anyRequest().authenticated()
//                 )
//                 .logout(logout -> logout.logoutSuccessUrl("/")); 

//         return http.build();
//     }

//     // ✅ This is the fixed 
//     @Bean
//     public CorsConfigurationSource corsConfigurationSource() {
//         CorsConfiguration configuration = new CorsConfiguration();
        
//         // This star "*" means "Allow Everyone"
//         configuration.setAllowedOriginPatterns(List.of("*")); 
        
//         configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//         configuration.setAllowedHeaders(List.of("*"));
//         configuration.setAllowCredentials(true); 

//         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//         source.registerCorsConfiguration("/**", configuration);
//         return source;
//     }
// }
package com.sensei.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf().disable()

            .authorizeRequests()
                // Public health endpoint
                .requestMatchers(new AntPathRequestMatcher("/actuator/health")).permitAll()

                // Public APIs (change later)
                .requestMatchers(new AntPathRequestMatcher("/api/**")).permitAll()

                // Everything else needs login
                .anyRequest().authenticated()
            .and()

            // 🔑 Enable Basic Auth
            .httpBasic();

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
