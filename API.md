# 共享小说平台 - 后端接口文档

## 基本信息

- **Base URL**: `/api`
- **统一响应格式**: `Result<T>`

```json
{
  "state": "1",        // "1" 成功, "0" 失败
  "message": "提示信息",
  "data": {}           // 泛型数据，可为对象/数组/字符串/Boolean
}
```

- **鉴权方式**: JWT Token（存储在 Cookie 中，key 为 `Token`）
- **需登录接口**: 所有涉及 用户/收藏/评论/小说发布 的操作均需在请求头中携带 `Cookie: Token=xxx`

---

## 一、用户模块 — UserController

### 1.1 用户注册

```
POST /api/register
Content-Type: application/json

Request Body:
{
  "uname": "用户名",
  "pwd": "密码"
}

Response:
{
  "state": "1",
  "message": "注册成功",
  "data": "uid"        // 新用户ID（字符串）
}
```

### 1.2 用户登录

```
POST /api/login
Content-Type: application/json

Request Body:
{
  "uname": "用户名",
  "pwd": "密码"
}

Response (成功):
{
  "state": "1",
  "message": "登录成功",
  "data": "token字符串"
}
```

### 1.3 修改密码

```
POST /api/resetPwd
Cookie: Token=xxx
Content-Type: application/json

Request Body:
{
  "oldPwd": "旧密码",
  "newPwd": "新密码"
}

Response:
{
  "state": "1",
  "message": "密码修改成功",
  "data": ""
}
```

### 1.4 注销账号

```
POST /api/deleteUser
Cookie: Token=xxx
Content-Type: application/json

Request Body:
{
  "pwd": "当前密码"
}

Response:
{
  "state": "1",
  "message": "账号注销成功",
  "data": ""
}
```

---

## 二、小说模块 — UserToNovelController

### 2.1 发布小说

```
POST /api/addNovel
Cookie: Token=xxx
Content-Type: application/json

Request Body:
{
  "novel": {
    "title": "小说标题",
    "content": "小说内容"
  },
  "user": {
    "pwd": "当前登录密码"   // 操作鉴权
  }
}

Response:
{
  "state": "1",
  "message": "发布成功",
  "data": 1             // 新小说ID
}
```

### 2.2 删除小说

```
POST /api/delNovel
Cookie: Token=xxx
Content-Type: application/json

Request Body:
{
  "novel": {
    "id": 1,
    "title": "小说标题"
  },
  "user": {
    "pwd": "当前登录密码"   // 操作鉴权
  }
}

Response:
{
  "state": "1",
  "message": "删除成功",
  "data": ""
}
```

### 2.3 举报小说

```
POST /api/reportNovel
Cookie: Token=xxx
Content-Type: application/json

Request Body:
{
  "id": 1               // 小说ID
}

Response:
{
  "state": "1",
  "message": "举报成功",
  "data": ""
}
```

---

## 三、收藏模块 — UserToCollectionController

### 3.1 收藏小说

```
POST /api/addCol
Cookie: Token=xxx
Content-Type: application/json

Request Body:
{
  "id": 1               // 小说ID
}

Response:
{
  "state": "1",
  "message": "成功收藏",
  "data": ""
}
```

### 3.2 取消收藏

```
POST /api/delCol
Cookie: Token=xxx
Content-Type: application/json

Request Body:
{
  "id": 1               // 小说ID
}

Response:
{
  "state": "1",
  "message": "取消收藏",
  "data": ""
}
```

---

## 四、评论模块 — CommentsController

### 4.1 添加评论

```
POST /api/addCom
Cookie: Token=xxx
Content-Type: application/json

Request Body:
{
  "nid": 1,             // 小说ID
  "content": "评论内容"
}

Response:
{
  "state": "1",
  "message": "评论成功",
  "data": ""
}
```

### 4.2 删除评论

```
POST /api/delCom
Cookie: Token=xxx
Content-Type: application/json

Request Body:
{
  "id": 1               // 评论ID
}

Response:
{
  "state": "1",
  "message": "评论删除成功",
  "data": ""
}
```

---

## 五、评论点赞模块 — CommentsLikeController

### 5.1 点赞评论

```
POST /api/addCommentsLike
Cookie: Token=xxx
Content-Type: application/x-www-form-urlencoded

Params:
  cid: 1                // 评论ID

Response:
{
  "state": "1",
  "message": "点赞成功",
  "data": ""
}
```

### 5.2 取消评论点赞

```
POST /api/delCommentsLike
Cookie: Token=xxx
Content-Type: application/x-www-form-urlencoded

Params:
  cid: 1                // 评论ID

Response:
{
  "state": "1",
  "message": "取消点赞",
  "data": ""
}
```

---

## 六、小说标签模块 — NovelLabelController

### 6.1 添加标签

```
POST /api/addLabel
Cookie: Token=xxx
Content-Type: application/json

Request Body:
[
  { "nid": 1, "lid": 2 },
  { "nid": 1, "lid": 3 }
]

Response:
{
  "state": "1",
  "message": "添加标签成功",
  "data": ""
}
```

### 6.2 移除标签

```
POST /api/delLabel
Cookie: Token=xxx
Content-Type: application/json

Request Body:
[
  { "id": 1 },          // NovelLabel 记录ID
  { "id": 2 }
]

Response:
{
  "state": "1",
  "message": "移除标签成功",
  "data": ""
}
```

---

## 七、管理员认证模块 — AdminController

### 7.1 管理员登录（JWT）

```
POST /api/alogin
Content-Type: application/json

Request Body:
{
  "aname": "管理员名",
  "pwd": "密码"
}

Response (成功):
{
  "state": "1",
  "message": "登录成功",
  "data": "token字符串"
}
```

### 7.2 管理员登录（备用）

```
POST /api/adminLogin
Content-Type: application/json

Request Body:
{
  "aname": "管理员名",
  "pwd": "密码"
}

Response:
{
  "state": "1",
  "message": "登录成功",
  "data": "token字符串"
}
```

### 7.3 新增管理员

```
POST /api/addAdmin
Content-Type: application/json

Request Body:
[
  { "aname": "操作管理员", "pwd": "操作密码" },          // 执行操作的管理员
  { "aname": "新管理员", "pwd": "新管理员密码" }        // 要添加的管理员
]

Response:
{
  "state": "1",
  "message": "添加成功",
  "data": ""
}
```

### 7.4 删除管理员

```
POST /api/delAdmin
Content-Type: application/json

Request Body:
[
  { "aname": "操作管理员", "pwd": "操作密码" },          // 执行操作的管理员
  { "aname": "目标管理员" }                             // 要被删除的管理员
]

Response:
{
  "state": "1",
  "message": "删除成功",
  "data": ""
}
```

---

## 八、管理员-小说操作 — AdminToNovelController

> 以下接口均需 Admin Token 鉴权（从 Cookie 中获取）

### 8.1 审核小说

```
POST /api/auditNovel
Cookie: Token=xxx
Content-Type: application/json

Request Params:
  record: "审核意见"

Request Body:
{
  "novel": {
    "id": 1,
    "state": 1           // -1:不通过, 0:审核中, 1:通过, 3:违规下架
  }
}

Response:
{
  "state": "1",
  "message": "操作成功",
  "data": null
}
```

### 8.2 管理员删除小说

```
POST /api/deleteNovelByAdmin
Cookie: Token=xxx
Content-Type: application/json

Request Body:
{
  "novel": {
    "id": 1
  }
}

Response:
{
  "state": "1",
  "message": "操作成功",
  "data": null
}
```

---

## 九、管理员-评论操作 — AdminToCommentsController

### 9.1 审核评论

```
POST /api/auditComments
Cookie: Token=xxx
Content-Type: application/json

Request Body:
{
  "comments": {
    "id": 1,
    "nid": 1,
    "state": -1          // 0:正常, 1:被举报, -1:封禁
  }
}

Response:
{
  "state": "1",
  "message": "操作成功",
  "data": null
}
```

---

## 十、管理员-用户操作 — AdminToUserController

> 以下接口均需 Admin Token 鉴权（从 Cookie 中获取）

### 10.1 审核用户（封禁/解封）

```
POST /api/auditUser
Cookie: Token=xxx
Content-Type: application/json

Request Body:
{
  "user": {
    "uid": 1,
    "banned": 1          // 0:正常, 1:封禁
  }
}

Response:
{
  "state": "1",
  "message": "操作成功",
  "data": null
}
```

### 10.2 管理员删除用户

```
POST /api/deleteUserByAdmin
Cookie: Token=xxx
Content-Type: application/json

Request Body:
{
  "user": {
    "uid": 1
  }
}

Response:
{
  "state": "1",
  "message": "操作成功",
  "data": null
}
```

### 10.3 管理员重置用户密码

```
POST /api/resetPwdByAdmin
Cookie: Token=xxx
Content-Type: application/json

Request Body:
{
  "user": {
    "uid": 1,
    "pwd": "新密码"
  }
}

Response:
{
  "state": "1",
  "message": "操作成功",
  "data": null
}
```

---

## 十一、查询展示模块 — ShowController

### 11.1 首页小说列表

```
GET /api/index?condition=&sort=&current=1&size=10

Params:
  condition  (可选) 搜索条件
  sort       (可选) 排序方式
  current    页码
  size       每页条数

Response:
{
  "state": "1",
  "message": "查询成功",
  "data": {}             // 分页结果
}
```

### 11.2 按标签分类查询

```
GET /api/classify?condition=&sort=&current=1&size=10&labelId=1

Params:
  condition  (可选) 搜索条件
  sort       (可选) 排序方式
  current    页码
  size       每页条数
  labelId    标签ID

Response:
{
  "state": "1",
  "message": "查询成功",
  "data": {}            // 分页结果
}
```

### 11.3 搜索小说

```
GET /api/search?condition=&sort=&current=1&size=10&name=关键字

Params:
  condition  (可选) 搜索条件
  sort       (可选) 排序方式
  current    页码
  size       每页条数
  name       搜索关键字（按标题搜索）

Response:
{
  "state": "1",
  "message": "查询成功",
  "data": {}            // 分页结果
}
```

### 11.4 获取用户名

```
GET /api/showUname?id=1

Params:
  id   用户ID

Response:
{
  "state": "1",
  "message": "查询成功",
  "data": "用户名"
}
```

### 11.5 获取所有标签

```
GET /api/labels

Response:
{
  "state": "1",
  "message": "查询成功",
  "data": [
    { "id": 1, "name": "玄幻" },
    { "id": 2, "name": "言情" }
  ]
}
```

### 11.6 根据用户名获取 UID

```
GET /api/showUid?uname=用户名

Params:
  uname   用户名

Response:
{
  "state": "1",
  "message": "查询成功",
  "data": 1             // 用户ID
}
```

### 11.7 获取用户信息

```
GET /api/showUser?uid=1

Params:
  uid   用户ID

Response:
{
  "state": "1",
  "message": "查询成功",
  "data": {
    "uid": 1,
    "uname": "用户名",
    "createTime": "2024-01-01 12:00:00",
    "banned": 0,
    "pwd": ""           // 密码字段返回空字符串
  }
}
```

### 11.8 获取用户发布的小说

```
GET /api/showNovels?uid=1

Params:
  uid   用户ID

Response:
{
  "state": "1",
  "message": "查询成功",
  "data": [
    {
      "id": 1,
      "title": "小说标题",
      "content": "内容",
      "createTime": "2024-01-01 12:00",
      "uid": 1,
      "views": 100,
      "state": 1
    }
  ]
}
```

### 11.9 获取用户收藏的小说

```
GET /api/showCollections?uid=1

Params:
  uid   用户ID

Response:
{
  "state": "1",
  "message": "查询成功",
  "data": [
    { /* Novel 对象数组 */ }
  ]
}
```

### 11.10 获取小说详情

```
GET /api/showNovel?nid=1

Params:
  nid   小说ID

Response:
{
  "state": "1",
  "message": "查询成功",
  "data": {
    "id": 1,
    "title": "标题",
    "content": "内容",
    "createTime": "2024-01-01 12:00",
    "uid": 1,
    "views": 100,
    "state": 1
  }
}
```

### 11.11 获取小说评论

```
GET /api/showComments?nid=1

Params:
  nid   小说ID

Response:
{
  "state": "1",
  "message": "查询成功",
  "data": [
    {
      "id": 1,
      "nid": 1,
      "uid": 1,
      "content": "评论内容",
      "state": 0
    }
  ]
}
```

### 11.12 查看是否收藏

```
GET /api/showLike?nid=1&uid=1

Params:
  nid   小说ID
  uid   用户ID

Response:
{
  "state": "1",
  "message": "查询成功",
  "data": true           // true:已收藏, false:未收藏
}
```

### 11.13 AI 智能推荐

```
GET /api/showRecommend?uid=1

Params:
  uid   用户ID

Response:
{
  "state": "1",
  "message": "查询成功",
  "data": [
    { /* Novel 对象数组 - 推荐的小说列表 */ }
  ]
}
```

### 11.14 获取用户评论历史

```
GET /api/showUserComments?uid=1

Params:
  uid   用户ID

Response:
{
  "state": "1",
  "message": "查询成功",
  "data": [
    { /* Comments 对象数组 */ }
  ]
}
```

### 11.15 获取被举报的小说

```
GET /api/getJvbao

Response:
{
  "state": "1",
  "message": "查询成功",
  "data": [
    { /* Novel 对象数组（state=2 被举报） */ }
  ]
}
```

### 11.16 获取待审核的小说

```
GET /api/getShenhe

Response:
{
  "state": "1",
  "message": "查询成功",
  "data": [
    { /* Novel 对象数组（state=0 待审核） */ }
  ]
}
```

### 11.17 获取应封禁的用户列表

```
GET /api/getBanUsers

Response:
{
  "state": "1",
  "message": "查询成功",
  "data": [
    { /* User 对象数组（违规次数 >= 3 次） */ }
  ]
}
```

### 11.18 获取已封禁的用户

```
GET /api/getBannedUsers

Response:
{
  "state": "1",
  "message": "查询成功",
  "data": [
    { /* User 对象数组（banned=1） */ }
  ]
}
```

### 11.19 获取被举报的评论

```
GET /api/getJvbaoComments

Response:
{
  "state": "1",
  "message": "查询成功",
  "data": [
    { /* Comments 对象数组（state=1 被举报） */ }
  ]
}
```

### 11.20 获取管理员列表

```
GET /api/getAdmins?aname=admin

Params:
  aname   当前管理员名（需为 "admin" 最高管理员）

Response:
{
  "state": "1",
  "message": "查询成功",
  "data": [
    { /* Admin 对象数组（不包含最高管理员 admin） */ }
  ]
}
```

### 11.21 获取所有用户

```
GET /api/getAllUsers

Response:
{
  "state": "1",
  "message": "查询成功",
  "data": [
    { /* User 对象数组 */ }
  ]
}
```

### 11.22 获取所有小说

```
GET /api/getAllNovels

Response:
{
  "state": "1",
  "message": "查询成功",
  "data": [
    { /* Novel 对象数组 */ }
  ]
}
```

### 11.23 获取审核记录

```
GET /api/getShenheRecord

Response:
{
  "state": "1",
  "message": "查询成功",
  "data": [
    { /* AdminToNovel 对象数组 */ }
  ]
}
```

---

## 十二、热门数据模块 — HotDataController

### 12.1 获取浏览量

```
GET /api/getViews?nid=1

Params:
  nid   小说ID

Response:
  100                   // 直接返回 Integer 浏览量（非标准 Result 格式）
```

### 12.2 浏览量加一

```
POST /api/addViews?nid=1

Params:
  nid   小说ID

Response:
  101                   // 直接返回 Integer 最新浏览量（非标准 Result 格式）
```

### 12.3 获取评论点赞状态

```
GET /api/getCommentsLike?cid=1&uid=1

Params:
  cid   评论ID
  uid   用户ID

Response:
  true                  // 直接返回 Boolean（非标准 Result 格式）
```

---

## 数据模型

### Novel（小说）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Integer | 小说ID（主键，自增） |
| title | String | 小说标题 |
| content | String | 小说内容 |
| createTime | String | 创建时间 (yyyy-MM-dd HH:mm) |
| uid | Integer | 发布用户ID |
| views | Integer | 浏览量，默认 0 |
| state | Integer | 状态：0=审核中，1=通过，-1=不通过，2=被举报，3=违规下架 |

### User（用户）

| 字段 | 类型 | 说明 |
|------|------|------|
| uid | Integer | 用户ID（主键，自增） |
| uname | String | 用户名（唯一） |
| pwd | String | 密码（BCrypt 加密） |
| createTime | String | 注册时间 (yyyy-MM-dd HH:mm:ss) |
| banned | Integer | 封禁状态：0=正常，1=封禁 |

### Admin（管理员）

| 字段 | 类型 | 说明 |
|------|------|------|
| aid | Integer | 管理员ID（主键，自增） |
| aname | String | 管理员名（唯一） |
| pwd | String | 密码（BCrypt 加密） |

### Comments（评论）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Integer | 评论ID（主键，自增） |
| nid | Integer | 所属小说ID |
| uid | Integer | 评论用户ID |
| content | String | 评论内容 |
| state | Integer | 状态：0=正常，1=被举报，-1=封禁 |

### Collection（收藏）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Integer | 记录ID（主键，自增） |
| uid | Integer | 用户ID |
| nid | Integer | 小说ID |
| createTime | String | 收藏时间 (yyyy-MM-dd HH:mm:ss) |

### Label（标签）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Integer | 标签ID（主键，自增） |
| name | String | 标签名（唯一） |

### NovelLabel（小说-标签关联）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Integer | 记录ID（主键，自增） |
| nid | Integer | 小说ID |
| lid | Integer | 标签ID |

### CommentsLike（评论点赞）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Integer | 记录ID（主键，自增） |
| cid | Integer | 评论ID |
| uid | Integer | 用户ID |
| createTime | String | 点赞时间 |

### AdminToNovel（审核记录）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Integer | 记录ID（主键，自增） |
| aid | Integer | 管理员ID |
| nid | Integer | 小说ID |
| createTime | String | 审核时间 |
| record | String | 审核意见 |

### AdminToComments（评论审核记录）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Integer | 记录ID（主键，自增） |
| aid | Integer | 管理员ID |
| cid | Integer | 评论ID |
| createTime | String | 审核时间 |
| record | String | 审核意见 |

### AdminToUser（用户操作记录）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Integer | 记录ID（主键，自增） |
| aid | Integer | 管理员ID |
| uid | Integer | 用户ID |
| createTime | String | 操作时间 |
| record | String | 操作记录 |