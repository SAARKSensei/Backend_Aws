# Sensei Authentication & Refresh Token Flow

This document outlines how the stateless refresh token architecture works for both the Web platform and the Flutter Mobile App.

## 1. Endpoints & API Usage

### A. Web Login (Preserved Backward Compatibility)
For the web frontend, we continue to return the raw Access Token as a plain string.
- **Endpoint:** `POST /api/auth/google`
- **Params:** `idToken=<google_token>`
- **Response Type:** `String`
- **Example Response:**
  ```text
  eyJhbGciOiJIUzI1NiJ9.eyJzdWIi...
  ```

### B. Mobile App Login
The Flutter app needs both tokens. We achieve this by passing the `client=app` parameter.
- **Endpoint:** `POST /api/auth/google?client=app`
- **Params:** `idToken=<google_token>`
- **Response Type:** `JSON` (Wrapped in standard `ApiResponse`)
- **Example Response:**
  ```json
  {
      "status": "SUCCESS",
      "message": "Request processed successfully",
      "data": {
          "accessToken": "eyJhbGci...",
          "refreshToken": "eyJhbGci..."
      }
  }
  ```

### C. Refreshing the Token
When the `accessToken` expires (it throws a `401 Unauthorized`), the Flutter app must use the `refreshToken` to get a new pair of tokens.
- **Endpoint:** `POST /api/auth/refresh`
- **Body (JSON):**
  ```json
  {
      "refreshToken": "<the_saved_refresh_token>"
  }
  ```
- **Response Type:** `JSON` (Wrapped in standard `ApiResponse`)
- **Example Response:**
  ```json
  {
      "status": "SUCCESS",
      "message": "Request processed successfully",
      "data": {
          "accessToken": "<NEW_ACCESS_TOKEN>",
          "refreshToken": "<NEW_REFRESH_TOKEN>"
      }
  }
  ```

---

## 2. Flutter Implementation Guide (The "Real Flow")

Here is how the Flutter frontend developer should implement this system:

### Phase 1: First Login
1. User taps "Sign in with Google".
2. App calls `POST /api/auth/google?client=app`.
3. App extracts `data.accessToken` and `data.refreshToken` from the JSON response.
4. App securely saves BOTH tokens to the device (e.g., using `flutter_secure_storage`).

### Phase 2: Normal API Usage
1. Whenever the app makes a request to the backend (e.g., fetching a Report Card), it reads the `accessToken` from secure storage.
2. It attaches it to the headers: `Authorization: Bearer <accessToken>`.

### Phase 3: The "Silent Refresh" (Token Rotation)
1. Eventually, the `accessToken` will expire.
2. The backend will reject a normal API request with a `403/401` error.
3. The app's HTTP client (like `Dio` interceptors) should **catch** this error globally.
4. Without showing any error to the user, the interceptor pauses the original request and calls `POST /api/auth/refresh` using the saved `refreshToken`.
5. The backend returns a brand new `accessToken` and a brand new `refreshToken`.
6. The app overwrites the old tokens in secure storage with the new ones.
7. The interceptor automatically **retries** the original API request with the new `accessToken`.
8. The request succeeds, and the user never notices that they were technically logged out for a split second!

#### Flutter Dio Interceptor Example
Here is a high-level example of how the Flutter developer can implement this using the `dio` package:

```dart
import 'package:dio/dio.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class AuthInterceptor extends Interceptor {
  final Dio dio;
  final FlutterSecureStorage storage = const FlutterSecureStorage();

  AuthInterceptor(this.dio);

  @override
  void onRequest(RequestOptions options, RequestInterceptorHandler handler) async {
    // 1. Attach access token to every request
    final accessToken = await storage.read(key: 'accessToken');
    if (accessToken != null) {
      options.headers['Authorization'] = 'Bearer $accessToken';
    }
    return handler.next(options);
  }

  @override
  void onError(DioException err, ErrorInterceptorHandler handler) async {
    // 2. Catch 401/403 Unauthorized errors
    if (err.response?.statusCode == 401 || err.response?.statusCode == 403) {
      final refreshToken = await storage.read(key: 'refreshToken');
      
      if (refreshToken != null) {
        try {
          // 3. Call refresh endpoint silently
          final refreshResponse = await dio.post(
            'https://your-api.com/api/auth/refresh',
            data: {'refreshToken': refreshToken},
          );

          if (refreshResponse.statusCode == 200) {
            // 4. Save new tokens
            final newAccessToken = refreshResponse.data['data']['accessToken'];
            final newRefreshToken = refreshResponse.data['data']['refreshToken'];
            await storage.write(key: 'accessToken', value: newAccessToken);
            await storage.write(key: 'refreshToken', value: newRefreshToken);

            // 5. Retry the original request with new access token
            final opts = err.requestOptions;
            opts.headers['Authorization'] = 'Bearer $newAccessToken';
            final cloneReq = await dio.fetch(opts);
            
            return handler.resolve(cloneReq); // Success!
          }
        } catch (e) {
          // Refresh token failed/expired. Force user to login screen.
        }
      }
    }
    return handler.next(err);
  }
}
```

---

## 3. Postman Testing Guide
If you want to test this locally without the app:

1. **Get Tokens:** Use the test login endpoint to simulate the app:
   `POST http://localhost:9090/api/auth/test-login?email=admin.sensei.org.in@gmail.com&client=app`
2. **Copy the Refresh Token:** Copy the `refreshToken` from the `data` block.
3. **Refresh:** Make a `POST` to `http://localhost:9090/api/auth/refresh`.
   - In the Body -> raw -> JSON, paste:
     ```json
     { "refreshToken": "YOUR_COPIED_TOKEN_HERE" }
     ```
4. **Verify:** You will receive a brand new pair of tokens!
