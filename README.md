# 六花桌宠（Android 悬浮窗）

Q 版小鸟游六花悬浮在手机屏幕上，可拖动、可点击触发「邪王真眼」姿势。

---

## 你需要做什么（全程手机操作，约 10 分钟）

### 第 0 步：准备

- 手机浏览器（Chrome / Edge / 系统浏览器都行）
- 一个 GitHub 账号（没有就现场注册，免费）

### 第 1 步：注册 / 登录 GitHub

1. 手机浏览器打开 <https://github.com>
2. 点 **Sign up**，用邮箱注册（用户名自己起，密码强度够就行）
3. 验证邮箱后登录

### 第 2 步：新建仓库

1. 登录后，右上角 **+** 号 → **New repository**
2. Repository name 填：`rikka-pet`
3. 选 **Public**（私有仓库 Actions 免费额度也够用，但 Public 不容易卡权限）
4. 勾选 **Add a README file**（随便勾一下，后面会被覆盖）
5. 点 **Create repository**

### 第 3 步：上传我给你的工程文件

把我交付的 `rikka-pet-android.zip` 解压到手机（用系统自带文件管理器，或「ZArchiver」之类的 App）。

然后在 GitHub 仓库页面：

1. 点 **Add file** → **Upload files**
2. 把解压出来的**所有文件和文件夹**选中拖进去（手机上是点选择，多选）
   - 注意要把这些都传上去：
     - `app/` 整个文件夹
     - `.github/` 整个文件夹（隐藏文件夹，文件管理器里要开「显示隐藏文件」）
     - `build.gradle`、`settings.gradle`、`gradle.properties`
     - 我可能附的 `README.md`
3. 等上传进度条走完
4. 点下方绿色 **Commit changes**

> ⚠️ 关键点：GitHub 网页上传**保留文件夹结构**。你一定要传成这样：
> ```
> rikka-pet/
> ├── app/
> │   └── src/main/...
> ├── .github/workflows/build.yml
> ├── build.gradle
> ├── settings.gradle
> └── gradle.properties
> ```
> 如果 `.github` 没传上去，Actions 不会自动跑。

### 第 4 步：让 GitHub Actions 自动编译

1. 上传完成后，仓库页面顶部切到 **Actions** 标签
2. 如果看到「Warning: this workflow contains unapproved actions」之类的提示，点 **I understand my workflows, go ahead and enable them**
3. 左侧选 **Build Rikka Pet APK**
4. 右边点 **Run workflow** → 绿色 **Run workflow** 按钮
5. 等约 3~5 分钟（第一次要下载 Android SDK，会慢一点）
6. 看到绿色对勾 ✅ 就是编译成功了

### 第 5 步：下载 APK 并安装

1. 点进那次成功的 workflow 运行记录
2. 拉到最下面 **Artifacts** 区块
3. 点 **rikka-pet-debug-apk**，会下载一个 zip
4. 解压这个 zip，里面就是 `app-debug.apk`
5. 点 APK 文件安装
   - 第一次装会提示「允许安装未知来源应用」，按提示去设置里给浏览器/文件管理器开权限
   - 这个 APK 没有签名证书，手机可能提示「未经验证」，选**仍然安装**即可

### 第 6 步：召唤六花

1. 打开「六花桌宠」App
2. 点 **召唤六花** 按钮
3. 第一次会跳转到「悬浮窗权限」设置页，给这个 App 开权限
4. 返回 App，再点一次召唤
5. 六花就出现在屏幕上了——拖动她换位置，点一下她会喊招式

---

## 故障排查

| 现象 | 怎么办 |
|---|---|
| Actions 一直转圈超过 10 分钟 | 大概率是网络问题，删了 workflow 重新 Run 一次 |
| Actions 红色 ❌ | 点进运行记录，看红色那一步的日志，截图发我 |
| APK 装不上 | 检查 Android 版本是否 ≥ 8.0（minSdk 26） |
| 打开 App 后点按钮没反应 | 去系统设置 → 应用 → 六花桌宠 → 权限，手动给「悬浮窗」 |
| 六花出现了但是是一个方块 | 说明图片资源没传对，检查 `app/src/main/res/drawable/pet_*.png` 是否都在 |

---

## 后续想加功能（随时说）

- 多几个动作帧（走路、坐下、吃零食）
- 台词气泡（点她说话）
- 桌面小菜单（关闭、换大小）
- 换服装 / 换角色

代码结构非常简单，全在 `app/src/main/java/com/doubao/rikkapet/` 两个文件里，改起来很快。
