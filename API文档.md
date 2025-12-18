# README

## 个人博客后端API文档

## 1. 认证相关接口 (AuthController)

### 1.1 用户注册
- **请求方法**: POST
- **请求路径**: `/api/auth/register`
- **请求参数**:
  ```json
  {
    "username": "testuser",
    "password": "password123",
    "email": "test@example.com",
    "nickname": "测试用户"
  }
  ```
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "注册成功",
    "data": {
      "id": 1,
      "username": "testuser",
      "email": "test@example.com",
      "nickname": "测试用户",
      "createTime": "2024-05-20T12:00:00",
      "updateTime": "2024-05-20T12:00:00"
    }
  }
  ```
- **权限要求**: 无

### 1.2 用户登录
- **请求方法**: POST
- **请求路径**: `/api/auth/login`
- **请求参数**:
  ```json
  {
    "username": "testuser",
    "password": "password123"
  }
  ```
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "登录成功",
    "data": {
      "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiZXhwIjoxNzY1OTc4MzcwfQ...",
      "user": {
        "id": 1,
        "username": "testuser",
        "email": "test@example.com",
        "nickname": "测试用户",
        "roles": ["ROLE_USER"]
      }
    }
  }
  ```
- **权限要求**: 无

## 2. 文章相关接口 (ArticleController)

### 2.1 创建文章
- **请求方法**: POST
- **请求路径**: `/api/articles`
- **请求参数**:
  ```json
  {
    "title": "测试文章",
    "content": "文章内容",
    "summary": "文章摘要",
    "categoryId": 1,
    "tagIds": [1, 2],
    "status": 1
  }
  ```
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "文章创建成功",
    "data": {
      "id": 1,
      "title": "测试文章",
      "content": "文章内容",
      "summary": "文章摘要",
      "categoryId": 1,
      "userId": 1,
      "status": 1,
      "viewCount": 0,
      "likeCount": 0,
      "commentCount": 0,
      "createTime": "2024-05-20T12:00:00",
      "updateTime": "2024-05-20T12:00:00"
    }
  }
  ```
- **权限要求**: 登录用户

### 2.2 更新文章
- **请求方法**: PUT
- **请求路径**: `/api/articles/{id}`
- **请求参数**:
  ```json
  {
    "title": "更新后的文章",
    "content": "更新后的内容",
    "summary": "更新后的摘要",
    "categoryId": 1,
    "tagIds": [1, 3],
    "status": 1
  }
  ```
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "文章更新成功",
    "data": {
      "id": 1,
      "title": "更新后的文章",
      "content": "更新后的内容",
      "summary": "更新后的摘要",
      "categoryId": 1,
      "userId": 1,
      "status": 1,
      "viewCount": 0,
      "likeCount": 0,
      "commentCount": 0,
      "createTime": "2024-05-20T12:00:00",
      "updateTime": "2024-05-20T13:00:00"
    }
  }
  ```
- **权限要求**: 文章作者或管理员

### 2.3 删除文章
- **请求方法**: DELETE
- **请求路径**: `/api/articles/{id}`
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "文章删除成功"
  }
  ```
- **权限要求**: 文章作者或管理员

### 2.4 获取文章详情
- **请求方法**: GET
- **请求路径**: `/api/articles/{id}`
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "获取文章成功",
    "data": {
      "id": 1,
      "title": "测试文章",
      "content": "文章内容",
      "summary": "文章摘要",
      "categoryId": 1,
      "userId": 1,
      "status": 1,
      "viewCount": 5,
      "likeCount": 2,
      "commentCount": 1,
      "createTime": "2024-05-20T12:00:00",
      "updateTime": "2024-05-20T12:00:00",
      "category": {
        "id": 1,
        "name": "技术"
      },
      "tags": [
        {"id": 1, "name": "Java"},
        {"id": 2, "name": "Spring Boot"}
      ]
    }
  }
  ```
- **权限要求**: 无

### 2.5 获取文章列表
- **请求方法**: GET
- **请求路径**: `/api/articles`
- **请求参数**:
  - page: 页码，默认1
  - size: 每页条数，默认10
  - categoryId: 分类ID（可选）
  - tagId: 标签ID（可选）
  - keyword: 搜索关键词（可选）
  - orderBy: 排序字段（可选）
  - asc: 是否升序（可选）
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "获取文章列表成功",
    "data": {
      "records": [
        {
          "id": 1,
          "title": "测试文章",
          "content": "文章内容",
          "summary": "文章摘要",
          "categoryId": 1,
          "userId": 1,
          "status": 1,
          "viewCount": 5,
          "likeCount": 2,
          "commentCount": 1,
          "createTime": "2024-05-20T12:00:00",
          "updateTime": "2024-05-20T12:00:00",
          "category": {
            "id": 1,
            "name": "技术"
          },
          "tags": [
            {"id": 1, "name": "Java"},
            {"id": 2, "name": "Spring Boot"}
          ]
        }
      ],
      "total": 1,
      "size": 10,
      "current": 1,
      "orders": [],
      "optimizeCountSql": true,
      "searchCount": true,
      "countId": null,
      "maxLimit": null,
      "pages": 1
    }
  }
  ```
- **权限要求**: 无

### 2.6 获取用户文章列表
- **请求方法**: GET
- **请求路径**: `/api/articles/user/{userId}`
- **请求参数**:
  - page: 页码，默认1
  - size: 每页条数，默认10
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "获取用户文章列表成功",
    "data": {
      "records": [
        {
          "id": 1,
          "title": "测试文章",
          "content": "文章内容",
          "summary": "文章摘要",
          "categoryId": 1,
          "userId": 1,
          "status": 1,
          "viewCount": 5,
          "likeCount": 2,
          "commentCount": 1,
          "createTime": "2024-05-20T12:00:00",
          "updateTime": "2024-05-20T12:00:00"
        }
      ],
      "total": 1,
      "size": 10,
      "current": 1
    }
  }
  ```
- **权限要求**: 无

### 2.7 获取我发布的文章列表
- **请求方法**: GET
- **请求路径**: `/api/articles/my`
- **请求参数**:
  - page: 页码，默认1
  - size: 每页条数，默认10
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "获取我发布的文章列表成功",
    "data": {
      "records": [
        {
          "id": 1,
          "title": "测试文章",
          "content": "文章内容",
          "summary": "文章摘要",
          "categoryId": 1,
          "userId": 1,
          "status": 1,
          "viewCount": 5,
          "likeCount": 2,
          "commentCount": 1,
          "createTime": "2024-05-20T12:00:00",
          "updateTime": "2024-05-20T12:00:00"
        }
      ],
      "total": 1,
      "size": 10,
      "current": 1
    }
  }
  ```
- **权限要求**: 登录用户

## 3. 分类相关接口 (CategoryController)

### 3.1 创建分类
- **请求方法**: POST
- **请求路径**: `/api/categories`
- **请求参数**:
  ```json
  {
    "name": "技术",
    "description": "技术相关文章"
  }
  ```
- **响应示例**:
  ```json
  {
    "id": 1,
    "name": "技术",
    "description": "技术相关文章",
    "createTime": "2024-05-20T12:00:00",
    "updateTime": "2024-05-20T12:00:00"
  }
  ```
- **权限要求**: 管理员

### 3.2 更新分类
- **请求方法**: PUT
- **请求路径**: `/api/categories/{id}`
- **请求参数**:
  ```json
  {
    "name": "技术类",
    "description": "技术相关文章分类"
  }
  ```
- **响应示例**:
  ```json
  {
    "id": 1,
    "name": "技术类",
    "description": "技术相关文章分类",
    "createTime": "2024-05-20T12:00:00",
    "updateTime": "2024-05-20T13:00:00"
  }
  ```
- **权限要求**: 管理员

### 3.3 删除分类
- **请求方法**: DELETE
- **请求路径**: `/api/categories/{id}`
- **响应示例**:
  ```
  204 No Content
  ```
- **权限要求**: 管理员

### 3.4 获取分类列表
- **请求方法**: GET
- **请求路径**: `/api/categories`
- **响应示例**:
  ```json
  [
    {
      "id": 1,
      "name": "技术",
      "description": "技术相关文章",
      "createTime": "2024-05-20T12:00:00",
      "updateTime": "2024-05-20T12:00:00"
    },
    {
      "id": 2,
      "name": "生活",
      "description": "生活相关文章",
      "createTime": "2024-05-20T12:00:00",
      "updateTime": "2024-05-20T12:00:00"
    }
  ]
  ```
- **权限要求**: 无

### 3.5 获取分类详情
- **请求方法**: GET
- **请求路径**: `/api/categories/{id}`
- **响应示例**:
  ```json
  {
    "id": 1,
    "name": "技术",
    "description": "技术相关文章",
    "createTime": "2024-05-20T12:00:00",
    "updateTime": "2024-05-20T12:00:00"
  }
  ```
- **权限要求**: 无

## 4. 标签相关接口 (TagController)

### 4.1 创建标签
- **请求方法**: POST
- **请求路径**: `/api/tags`
- **请求参数**:
  ```json
  {
    "name": "Java"
  }
  ```
- **响应示例**:
  ```json
  {
    "id": 1,
    "name": "Java",
    "createTime": "2024-05-20T12:00:00",
    "updateTime": "2024-05-20T12:00:00"
  }
  ```
- **权限要求**: 管理员

### 4.2 更新标签
- **请求方法**: PUT
- **请求路径**: `/api/tags/{id}`
- **请求参数**:
  ```json
  {
    "name": "Java开发"
  }
  ```
- **响应示例**:
  ```json
  {
    "id": 1,
    "name": "Java开发",
    "createTime": "2024-05-20T12:00:00",
    "updateTime": "2024-05-20T13:00:00"
  }
  ```
- **权限要求**: 管理员

### 4.3 删除标签
- **请求方法**: DELETE
- **请求路径**: `/api/tags/{id}`
- **响应示例**:
  ```
  204 No Content
  ```
- **权限要求**: 管理员

### 4.4 获取标签列表
- **请求方法**: GET
- **请求路径**: `/api/tags`
- **响应示例**:
  ```json
  [
    {
      "id": 1,
      "name": "Java",
      "createTime": "2024-05-20T12:00:00",
      "updateTime": "2024-05-20T12:00:00"
    },
    {
      "id": 2,
      "name": "Spring Boot",
      "createTime": "2024-05-20T12:00:00",
      "updateTime": "2024-05-20T12:00:00"
    }
  ]
  ```
- **权限要求**: 无

### 4.5 获取标签详情
- **请求方法**: GET
- **请求路径**: `/api/tags/{id}`
- **响应示例**:
  ```json
  {
    "id": 1,
    "name": "Java",
    "createTime": "2024-05-20T12:00:00",
    "updateTime": "2024-05-20T12:00:00"
  }
  ```
- **权限要求**: 无

## 5. 评论相关接口 (CommentController)

### 5.1 创建评论
- **请求方法**: POST
- **请求路径**: `/api/comments`
- **请求参数**:
  ```json
  {
    "articleId": 1,
    "content": "这是一条评论",
    "parentId": null
  }
  ```
- **响应示例**:
  ```json
  {
    "id": 1,
    "articleId": 1,
    "userId": 1,
    "content": "这是一条评论",
    "parentId": null,
    "likeCount": 0,
    "createTime": "2024-05-20T12:00:00",
    "updateTime": "2024-05-20T12:00:00"
  }
  ```
- **权限要求**: 登录用户

### 5.2 删除评论
- **请求方法**: DELETE
- **请求路径**: `/api/comments/{commentId}`
- **响应示例**:
  ```
  204 No Content
  ```
- **权限要求**: 评论作者或管理员

### 5.3 获取文章评论列表
- **请求方法**: GET
- **请求路径**: `/api/comments/article/{articleId}`
- **响应示例**:
  ```json
  [
    {
      "id": 1,
      "articleId": 1,
      "userId": 1,
      "content": "这是一条评论",
      "parentId": null,
      "likeCount": 0,
      "createTime": "2024-05-20T12:00:00",
      "updateTime": "2024-05-20T12:00:00"
    }
  ]
  ```
- **权限要求**: 无

### 5.4 获取用户评论列表
- **请求方法**: GET
- **请求路径**: `/api/comments/user/{userId}`
- **响应示例**:
  ```json
  [
    {
      "id": 1,
      "articleId": 1,
      "userId": 1,
      "content": "这是一条评论",
      "parentId": null,
      "likeCount": 0,
      "createTime": "2024-05-20T12:00:00",
      "updateTime": "2024-05-20T12:00:00"
    }
  ]
  ```
- **权限要求**: 登录用户，只能查看自己的评论或管理员可以查看所有评论

### 5.5 获取我的评论列表
- **请求方法**: GET
- **请求路径**: `/api/comments/my`
- **响应示例**:
  ```json
  [
    {
      "id": 1,
      "articleId": 1,
      "userId": 1,
      "content": "这是一条评论",
      "parentId": null,
      "likeCount": 0,
      "createTime": "2024-05-20T12:00:00",
      "updateTime": "2024-05-20T12:00:00"
    }
  ]
  ```
- **权限要求**: 登录用户

### 5.6 获取所有评论（分页）
- **请求方法**: GET
- **请求路径**: `/api/comments`
- **请求参数**:
  - current: 页码，默认1
  - size: 每页条数，默认10
- **响应示例**:
  ```json
  {
    "records": [
      {
        "id": 1,
        "articleId": 1,
        "userId": 1,
        "content": "这是一条评论",
        "parentId": null,
        "likeCount": 0,
        "createTime": "2024-05-20T12:00:00",
        "updateTime": "2024-05-20T12:00:00"
      }
    ],
    "total": 1,
    "size": 10,
    "current": 1
  }
  ```
- **权限要求**: 管理员

### 5.7 搜索评论
- **请求方法**: GET
- **请求路径**: `/api/comments/search`
- **请求参数**:
  - keyword: 搜索关键词
  - current: 页码，默认1
  - size: 每页条数，默认10
- **响应示例**:
  ```json
  {
    "records": [
      {
        "id": 1,
        "articleId": 1,
        "userId": 1,
        "content": "这是一条评论",
        "parentId": null,
        "likeCount": 0,
        "createTime": "2024-05-20T12:00:00",
        "updateTime": "2024-05-20T12:00:00"
      }
    ],
    "total": 1,
    "size": 10,
    "current": 1
  }
  ```
- **权限要求**: 管理员

## 6. 文件相关接口 (FileController)

### 6.1 上传文件
- **请求方法**: POST
- **请求路径**: `/api/files/upload`
- **请求参数**:
  - file: 要上传的文件（multipart/form-data）
- **响应示例**:
  ```json
  {
    "id": 1,
    "fileName": "test.jpg",
    "originalName": "test.jpg",
    "fileSize": 102400,
    "fileType": "image/jpeg",
    "filePath": "/uploads/test.jpg",
    "userId": 1,
    "createTime": "2024-05-20T12:00:00",
    "updateTime": "2024-05-20T12:00:00"
  }
  ```
- **权限要求**: 登录用户

### 6.2 下载文件
- **请求方法**: GET
- **请求路径**: `/api/files/download/{fileId}`
- **响应示例**:
  ```
  文件流
  ```
- **权限要求**: 登录用户，只能下载自己的文件或管理员可以下载所有文件

### 6.3 获取我的文件列表
- **请求方法**: GET
- **请求路径**: `/api/files/my`
- **响应示例**:
  ```json
  [
    {
      "id": 1,
      "fileName": "test.jpg",
      "originalName": "test.jpg",
      "fileSize": 102400,
      "fileType": "image/jpeg",
      "filePath": "/uploads/test.jpg",
      "userId": 1,
      "createTime": "2024-05-20T12:00:00",
      "updateTime": "2024-05-20T12:00:00"
    }
  ]
  ```
- **权限要求**: 登录用户

### 6.4 获取文件详情
- **请求方法**: GET
- **请求路径**: `/api/files/{fileId}`
- **响应示例**:
  ```json
  {
    "id": 1,
    "fileName": "test.jpg",
    "originalName": "test.jpg",
    "fileSize": 102400,
    "fileType": "image/jpeg",
    "filePath": "/uploads/test.jpg",
    "userId": 1,
    "createTime": "2024-05-20T12:00:00",
    "updateTime": "2024-05-20T12:00:00"
  }
  ```
- **权限要求**: 登录用户，只能查看自己的文件或管理员可以查看所有文件

### 6.5 删除文件
- **请求方法**: DELETE
- **请求路径**: `/api/files/{fileId}`
- **响应示例**:
  ```
  204 No Content
  ```
- **权限要求**: 登录用户

### 6.6 获取所有文件（管理员）
- **请求方法**: GET
- **请求路径**: `/api/files/admin/all`
- **响应示例**:
  ```json
  [
    {
      "id": 1,
      "fileName": "test.jpg",
      "originalName": "test.jpg",
      "fileSize": 102400,
      "fileType": "image/jpeg",
      "filePath": "/uploads/test.jpg",
      "userId": 1,
      "createTime": "2024-05-20T12:00:00",
      "updateTime": "2024-05-20T12:00:00"
    }
  ]
  ```
- **权限要求**: 管理员

## 7. 点赞相关接口 (LikeController)

### 7.1 点赞文章
- **请求方法**: POST
- **请求路径**: `/api/likes/article/{articleId}`
- **响应示例**:
  ```json
  {
    "id": 1,
    "targetId": 1,
    "targetType": 0,
    "userId": 1,
    "createTime": "2024-05-20T12:00:00",
    "updateTime": "2024-05-20T12:00:00"
  }
  ```
- **权限要求**: 登录用户

### 7.2 点赞评论
- **请求方法**: POST
- **请求路径**: `/api/likes/comment/{commentId}`
- **响应示例**:
  ```json
  {
    "id": 2,
    "targetId": 1,
    "targetType": 1,
    "userId": 1,
    "createTime": "2024-05-20T12:00:00",
    "updateTime": "2024-05-20T12:00:00"
  }
  ```
- **权限要求**: 登录用户

### 7.3 取消点赞文章
- **请求方法**: DELETE
- **请求路径**: `/api/likes/article/{articleId}`
- **响应示例**:
  ```
  204 No Content
  ```
- **权限要求**: 登录用户

### 7.4 取消点赞评论
- **请求方法**: DELETE
- **请求路径**: `/api/likes/comment/{commentId}`
- **响应示例**:
  ```
  204 No Content
  ```
- **权限要求**: 登录用户

### 7.5 获取文章点赞数
- **请求方法**: GET
- **请求路径**: `/api/likes/count/article/{articleId}`
- **响应示例**:
  ```json
  5
  ```
- **权限要求**: 无

### 7.6 获取评论点赞数
- **请求方法**: GET
- **请求路径**: `/api/likes/count/comment/{commentId}`
- **响应示例**:
  ```json
  2
  ```
- **权限要求**: 无

### 7.7 检查用户是否点赞文章
- **请求方法**: GET
- **请求路径**: `/api/likes/check/article/{articleId}`
- **响应示例**:
  ```json
  true
  ```
- **权限要求**: 登录用户

### 7.8 检查用户是否点赞评论
- **请求方法**: GET
- **请求路径**: `/api/likes/check/comment/{commentId}`
- **响应示例**:
  ```json
  false
  ```
- **权限要求**: 登录用户

### 7.9 获取我的点赞列表
- **请求方法**: GET
- **请求路径**: `/api/likes/my`
- **响应示例**:
  ```json
  [
    {
      "id": 1,
      "targetId": 1,
      "targetType": 0,
      "userId": 1,
      "createTime": "2024-05-20T12:00:00",
      "updateTime": "2024-05-20T12:00:00"
    }
  ]
  ```
- **权限要求**: 登录用户

## 8. 通用参数说明

### 8.1 分页参数
- page: 页码，默认1
- size: 每页条数，默认10

### 8.2 排序参数
- orderBy: 排序字段
- asc: 是否升序，默认false（降序）

### 8.3 认证
- 所有需要登录的接口都需要在请求头中添加Authorization字段：
  ```
  Authorization: Bearer {token}
  ```

### 8.4 响应格式
- 成功响应：
  ```json
  {
    "code": 200,
    "message": "操作成功",
    "data": {}
  }
  ```
- 错误响应：
  ```json
  {
    "code": 500,
    "message": "操作失败",
    "data": null
  }
  ```

## 9. 错误码说明

| 错误码 | 说明 |
|-------|------|
| 200 | 操作成功 |
| 400 | 参数错误 |
| 401 | 未授权 |
| 403 | 禁止访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |
