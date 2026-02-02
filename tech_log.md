# Logger

## The need for Log capture

There are multiple reasons why we may need to capture the application activity.

1. Recording unusual circumstances or errors that may be happening in the program
2. Getting the info about whats going in the application.

### Log Levels

There are 7 logs levels

| Level  | Value | Used for |
| :---------------- | :------: | ----: |
| SEVERE | 1000 | Indicates some serious failure |
| WARNING | 900 | Potential Problem |
| INFO | 800 | General Info |
| CONFIG | 700 | Configuration Info |
| FINE | 500 | General Developer Info |
| FINER | 400 | Detailed Developer Info |
| FINEST | 300 | Specialized Developer Info |

-----
**a. Severe** occurs when something terrible has occurred and the application cannot continue further. Ex like database unavailable, out of memory.

**b. Warning** may occur whenever the user has given wrong input or credentials.

**c. Info** is for the use of administrators or advanced users. It denotes mostly the actions that have lead to a change in state for the application.

**d. Configuration** Information may be like what CPU the application is running on, how much is the disk and memory space.

**Fine Finer and Finest** provide tracing information. When what is happening/ has happened in our application.

**e. FINE** displays the most important messages out of these.

**f. FINER** outputs a detailed tracing message and may include logging calls regarding method entering, exiting, throwing exceptions.

**g. FINEST** provides highly detailed tracing message.
Furthermore, there are two special Logging levels.

#### There are two special types

| Level  | Value | Used for |
| :---------------- | :------: | ----: |
| OFF | Integer.MAX_VALUE | Capturing nothing|
| ALL | Integer.MIN_VALUE | Capturing Everything |

## Logging Principles

Before code, enforce these rules:

1. Never log raw stack traces without context

2. Never log sensitive data (passwords, tokens, OTPs)

3. Every request must have a correlation ID

4. Logs must be structured (JSON), not plain text

5. Logging must be async.