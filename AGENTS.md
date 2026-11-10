# AGENTS.md

本文件为 AI 编码助手（及项目贡献者）提供本仓库的编码规范与自动提交规则。
**工作前先阅读本文件**，所有代码变更必须遵守以下约定。

## 1. 项目概览

小说分享网站（毕业设计），单仓库多模块：

| 目录 | 说明 | 技术栈 |
| --- | --- | --- |
| `novel/` | 后端服务，端口 8080，接口统一挂载在 `/api` 下 | Java 21、Spring Boot 3.4、Maven、MyBatis-Plus、Lombok、Redis、Hutool、Spring AI |
| `shareNovel-vue/` | 用户端前端 | Vue 3.5、TypeScript、Vite 6、Element Plus、axios、vue-router、Less、pnpm |
| `shareNovelManager-vue/` | 管理端前端 | 同上 |
| `docker/` | 部署编排（mysql / redis / java / nginx） | docker-compose |

- 数据库：MySQL `shareNovel`，账号 `root` / `123456`；Redis 密码 `123456`。
- 后端配置文件为 `novel/src/main/resources/application-*.yml`，含 `dev` / `test` / `prod` 三个 profile。
- 前端通过 Vite dev server 代理 `/api` → `http://localhost:8080`。
- 详细接口文档见 `API.md`。

## 2. 通用编码原则

- **最小改动**：只改与任务直接相关的代码，不顺手重构、不擅自重命名文件或变量。
- **不引入新依赖**，除非任务确实需要；优先使用项目已有依赖与工具类（如 Hutool、`utils/` 下的封装）。
- **不写复述代码的注释**；只在意图、约束、取舍不直观时写注释。
- **不提交任何密钥、密码、token**；配置中的既有本地开发密码（`123456`）保持原样，不要扩散到新文件。
- 修改公共接口、配置或数据库结构时，同步更新 `API.md`、`README.md` 等相关文档。
- 保护用户工作：不覆盖、不回退非本次任务产生的改动。

## 3. 后端规范（`novel/`）

### 3.1 分层与包结构

代码位于 `com.share.novel` 包下，严格按职责分层：

- `base/` — 实体类（Entity）
- `config/` — 配置类（拦截器、跨域等）
- `controller/` — 控制器，只接收/校验参数并调用 Service，**不写业务逻辑**
- `mapper/` — MyBatis-Plus Mapper 接口
- `service/` + `service/impl/` — 业务逻辑
- `utils/` — 通用工具（`Result`、`RedisUtil`、`ControllerUtil` 等）

### 3.2 代码风格

- 使用 Lombok（`@Data` 等）减少样板代码；请求 DTO 可定义为 Controller 内的静态内部类。
- 依赖注入沿用项目现有的字段注入方式（`@Autowired`）。
- Controller 统一 `@RequestMapping("/api")`，返回类型统一为 `Result<?>`。
- 统一响应结构：`state`（`1` 成功，`0` 失败）、`message`、`data`；不要绕开 `Result` 直接返回裸数据。
- 需要鉴权的写操作沿用 `controllerUtil.sameFunc(...)` 模式；成功后按需用 `redisUtil.deleteByPattern(...)` 清理相关缓存。
- SQL 通过 MyBatis-Plus 构造，不手写字符串拼接 SQL；分页使用 MyBatis-Plus 分页插件。
- 时间字段使用 `java.time`（如 `LocalDateTime`），不用 `java.util.Date` 新增字段。

### 3.3 验证命令

```bash
cd novel && ./mvnw -q compile        # 编译（Windows 用 mvnw.cmd）
cd novel && ./mvnw -q test           # 测试（如可用）
```

后端变更**至少通过编译**才允许提交。

## 4. 前端规范（`shareNovel-vue/`、`shareNovelManager-vue/`）

### 4.1 目录结构

- `src/views/` — 页面级组件（`*View.vue`）
- `src/components/` — 可复用组件
- `src/request/` — 按领域拆分的 API 模块（`novel.ts`、`user.ts` 等）
- `src/router/` — 路由
- `src/baseType/`、`src/enmu/` — 类型与枚举

### 4.2 代码风格

- 使用 Vue 3 Composition API + `<script setup lang="ts">` + TypeScript，不写 Options API 新页面。
- 所有 HTTP 请求**必须**经由 `src/request/index.ts` 导出的 axios 实例（统一 `baseURL: '/api'` 与错误处理），不在组件里直接 `axios.get(...)`。
- 新增接口时，在 `src/request/` 对应领域模块中添加函数；判断成功用 `result.state === '1'`。
- 路径别名 `@` 指向 `src/`，导入使用 `@/...` 而非深层相对路径。
- Element Plus 组件与图标已配置自动导入（`unplugin-auto-import`、`unplugin-vue-components`），直接使用，**不要手写 import 语句**；`auto-imports.d.ts`、`components.d.ts` 为生成文件，不手工编辑。
- 样式使用 Less（`<style scoped lang="less">`），优先 scoped 避免全局污染；主题变量集中在 `src/theme.css`。
- 用户提示信息统一用 `ElMessage`；环境变量通过 `import.meta.env.VITE_*` 读取。

### 4.3 验证命令

```bash
cd shareNovel-vue && pnpm type-check        # 类型检查
cd shareNovelManager-vue && pnpm type-check
cd shareNovel-vue && pnpm build             # 构建（如需要）
```

前端变更**至少通过 `type-check`** 才允许提交。

## 5. 自动提交规范

### 5.1 提交时机

AI 助手在完成一个**逻辑完整、可独立验证**的改动单元后自动提交，例如：

- 完成一个功能点 / 修复一个缺陷，且对应模块通过验证（后端 `compile`，前端 `type-check`）。
- 一次提交只包含同一改动单元涉及的文件，**不混入无关文件**。

以下情况**禁止提交**：

- 验证未通过或无法验证。
- 工作不完整（半成品、调试代码、被注释掉的代码块）。
- 工作区存在非本次任务的未提交改动，且无法干净地只暂存本次改动。
- 涉及密钥、账号密码等敏感信息。

### 5.2 提交方式

- 使用 `git add <具体文件...>` 精确暂存，不用 `git add -A` / `git add .`。
- 不提交生成产物：`dist/`、`target/`、`node_modules/`、`*.jar`（根目录 `.gitignore` 已包含，注意排查）。
- **不执行** `git commit --amend`、`git rebase`、`git push --force` 等改写历史的操作。
- 不创建新分支，除非用户明确要求。
- 提交失败（如 hook 报错）时，报告原因，修复后重试；不绕过 hook（`--no-verify`）。

### 5.3 提交信息格式

遵循 Conventional Commits 前缀 + 良好 Git 风格：

```
<type>: <主题>

[可选正文]
```

**前缀（type）**：

| 前缀 | 用途 |
| --- | --- |
| `feat` | 新功能 |
| `fix` | 缺陷修复 |
| `refactor` | 重构（不改变外部行为） |
| `docs` | 文档变更 |
| `style` | 格式调整（不影响代码含义） |
| `perf` | 性能优化 |
| `test` | 测试相关 |
| `chore` | 构建、依赖、杂项 |

**主题（subject）**：

- 不超过 50 个字符。
- 使用祈使句（如"添加小说搜索接口"、"修复登录态丢失"），中文或英文均可，与本次改动语言的代码库保持一致。
- 结尾不加标点。

**正文（body）**：

- 仅在提供**有用信息**时写（如改动原因、影响范围、迁移说明），不重复主题内容。
- 与主题之间空一行，每行不超过 72 字符。

**示例**：

```
feat: 添加小说按标签分类接口

fix: 修复重置密码后旧 token 未失效的问题

docs: 更新 API.md 的评论审核接口说明
```

## 6. 常用命令速查

```bash
# 本地依赖（Docker）
docker run --restart=always -d --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 mysql:latest
docker run --restart=always -p 6379:6379 --name myredis -d redis:latest --requirepass 123456

# 后端
cd novel && ./mvnw spring-boot:run

# 前端
cd shareNovel-vue && pnpm install && pnpm dev          # 用户端
cd shareNovelManager-vue && pnpm install && pnpm dev   # 管理端

# 整体部署
cd docker && docker compose up -d
```
