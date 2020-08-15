---
typora-root-url: assets
---

# 1 hello world

![image-20200727122625469](/image-20200727122625469.png)

# 2 Queries and Mutations

## 2.1 Fields

```
fun hero(): Hero {
    val friends = listOf(
        User(UUID.randomUUID().toString(), "Luke Skywalker", 18),
        User(UUID.randomUUID().toString(), "Han Solo", 16),
        User(UUID.randomUUID().toString(), "Leia Organa", 17)
    )
    return Hero("R2-D2", friends)
}

data class Hero(val name: String, val friends: List<User>)
data class User(
    val id: String,
    val name: String,
    val age: Int
)
```







