---
typora-root-url: assets
---

# 1 modules

| modules | desc                     | comment                                                      |
| ------- | ------------------------ | ------------------------------------------------------------ |
| server  | 服务内部的业务逻辑       |                                                              |
| client  | 开放给其他后端服务调用的 | 只有interface，没有implements                                |
| sdk     | 开放给前端调用的         | - json<br />- validation<br />- Identification(auth)<br />- Exceptions |

# 2 Exceptions

| Exceptions | desc |
| ---- | ---- |
|  `ValidationException(Map<filedName,errorMessage>)`    | 校验接口参数 |
| `BusinessException(hintMessage)`     | 可以直接抛给用户的提示语 |
|   `ErrorCodeException(code,errorMessage)`   | 需要前端处理的异常。<br />其中，`code`是标记 |
|  `UnknownException(Throwable)`    | 代码BUG |


> The `UnknownException` must be handled before publishing to the production environment

# 3 Unified Exception model

```
data class UnifiedExceptionModel(
    val reason: Reason? = null,
    val code: String? = null,
    val message: String? = null,
    val fieldError: List<ErrorCode>? = null,
    val cause: Exception? = null
)
enum class Reason {
    ValidationException, BusinessException, ErrorCodeException, UnknownException
}
data class ErrorCode(val code: String, val message: String)

class BusinessException(message: String) : java.lang.RuntimeException(message)
class ErrorCodeException(val code: String, message: String) : java.lang.RuntimeException(message)
class ValidationException(val fieldError: List<ErrorCode>) : java.lang.RuntimeException()

data class OAuthError(val error: String, val error_description: String)
```





