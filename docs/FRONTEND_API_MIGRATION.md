# Frontend API Migration Guide (Issue #35)

> [!WARNING]  
> **Breaking API Changes**  
> We have completed a major backend refactor to align with production-grade enterprise standards. All endpoints, error responses, and list queries have new response structures. Please review this document carefully to update the frontend integration.

## 1. Unified API Responses (`ApiResponse<T>`)

Previously, endpoints returned raw payloads (e.g., `ChildUserDTO` or `List<ChildUserDTO>`). 
Now, **all successful responses** are wrapped in a standard `ApiResponse` object.

### Old Response Structure
```json
{
  "childId": "c8f9b9f3-8b7c-4d56-a146-2b4a3a6b5e67",
  "childName": "John Doe",
  ...
}
```

### New Response Structure
All data will now be under the `data` field of the response body.
```json
{
  "status": "SUCCESS",
  "message": "Request processed successfully",
  "data": {
    "childId": "c8f9b9f3-8b7c-4d56-a146-2b4a3a6b5e67",
    "childName": "John Doe",
    ...
  },
  "timestamp": "2026-08-26T12:00:00"
}
```

---

## 2. Standardized Error Handling (RFC 7807 ProblemDetail)

We have migrated to Spring's native RFC 7807 `ProblemDetail` for all exceptions. The shape of error responses has completely changed.

### Old Error Structure (Varied)
```json
{
  "timestamp": "...",
  "status": 404,
  "error": "Not Found",
  "message": "User not found"
}
```
*(or sometimes just a raw string)*

### New Error Structure (RFC 7807)
Errors will consistently contain `type`, `title`, `status`, and `detail`. For validation errors, a custom `errors` map will be provided.
```json
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Validation failed",
  "instance": "/api/parent-users",
  "errors": {
    "email": "must be a well-formed email address",
    "phone": "must not be blank"
  }
}
```

---

## 3. Pagination Support (`Pageable`)

To prevent performance issues, we are transitioning list-returning endpoints to use pagination. Currently applied to `/api/parent-users` and `/api/children`.

### How to request pages
You can now pass `page` (0-indexed) and `size` parameters to the query string.
`GET /api/children?page=0&size=20`

### New Response Structure for Lists
Instead of returning a JSON array, the `data` field now contains a `Page` object with metadata.
```json
{
  "status": "SUCCESS",
  "message": "Request processed successfully",
  "data": {
    "content": [
      { "childId": "..." },
      { "childId": "..." }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 20,
      "sort": { "empty": true, "sorted": false, "unsorted": true },
      ...
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 2,
    "first": true,
    "size": 20,
    "number": 0,
    "sort": { "empty": true, "sorted": false, "unsorted": true },
    "numberOfElements": 2,
    "empty": false
  },
  "timestamp": "2026-08-26T12:05:00"
}
```

## Summary of Action Items for Frontend
1. **Unwrap successful responses:** Change `response.data` to `response.data.data` in your Axios/Fetch interceptors.
2. **Update error handling:** Ensure error toasts read from `error.response.data.detail` and form validation reads from `error.response.data.errors`.
3. **Update list rendering:** Change `response.data` arrays to `response.data.data.content` for paginated endpoints, and implement UI controls for `totalPages`.
