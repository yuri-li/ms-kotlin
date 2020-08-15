---
typora-root-url: assets
---

# 1 Shortcut keys for log(intellij idea)

![image-20200726011820496](/image-20200726011820496.png)

# 2 endpoints

| endpoint         | desc                                    |
| ---------------- | --------------------------------------- |
| `/playground`    | 用于测试的`GraphQL IDE`                 |
| `/graphql`       |                                         |
| `/sdl`           | 获取`Schema Definition Language format` |
| `/subscriptions` |                                         |

# 3 Sending GraphQL Queries in Postman

## 3.1 fetch sdl

```
GET http://localhost:8080/sdl
```

![image-20200726013034603](/image-20200726013034603.png)

## 3.2 Config GraphQL Schema

![image-20200726013712030](/image-20200726013712030.png)



## 3.3 Send a query

![image-20200726013910268](/image-20200726013910268.png)

# 4 function

`GraphQL`允许查询model的properties & function

![image-20200726045201646](/image-20200726045201646.png)

------

![image-20200726052935558](/image-20200726052935558.png)

# 5 coroutine

> 示例: `kotlin coroutine + webflux + GraphQL`

```
package org.study.account.model

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.slf4j.LoggerFactory

data class User(
    val id: String,
    val name: String,
    val age: Int
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    suspend fun talk(): List<String> = coroutineScope {
        log.info("start--- ${id}")
        val list = listOf(async { ask() }.await(), async { answer() }.await())
        log.info("end--- ${id}")
        list
    }

    private suspend fun ask(): String {
        delay(2000)
        return "hi,how are you?"
    }

    private suspend fun answer(): String {
        delay(3000)
        return "not bad"
    }
}
```



 







