# 克制时刻（KeZhiShiKe）

一个极简 Android 原生打卡 App：记录每一次成功打断刷视频/娱乐冲动的时刻。支持桌面小组件一键打卡，数据先保存在本机，并预留跨设备同步接口。

## 功能
- 首页：累计次数、今日次数、最近记录
- 一键打卡：记录时间戳，显示简短反馈
- Android 桌面小组件：无需打开 App 即可打卡；展示今日和累计次数
- 本地存储：SharedPreferences，离线可用
- 同步预留：`SyncProvider` 接口与 `NoOpSyncProvider`，目前不会上传数据

## 环境
- Android Studio Ladybug 或更新版本
- JDK 17
- Android SDK 35
- Gradle 8.9（Android Studio 可自动下载/配置）
- Android Gradle Plugin 8.7.3 / Kotlin 2.0.21

## 运行
1. 解压 ZIP。
2. Android Studio → Open，选择 `KeZhiShiKe` 文件夹。
3. 等待 Gradle Sync 完成。首次构建需联网下载 Gradle/Android Gradle Plugin。
4. 使用 Android 8.0+ 真机或模拟器，开启 USB 调试后点 Run。
5. 添加桌面小组件：长按桌面空白处 → 小组件/Widgets → 找到「克制时刻」→ 拖到桌面。

## 小组件
- 点击「我克服了」按钮，会通过 BroadcastReceiver 记录一次，并刷新小组件。
- 不同 Android 启动器对小组件添加入口略有差异。
- 小组件不是系统级的“拦截器”；它不监控其他 App，也不会自动弹出。

## 数据与同步
当前所有记录仅保存在本机应用私有 SharedPreferences 中，不需要账号，也不会上传。
`app/src/main/java/com/kezhi/shike/SyncProvider.kt` 定义了同步接口。真正跨设备同步还需要后端服务、账号/身份验证、冲突合并策略和隐私说明；本项目不伪装成已经支持云同步。

## 数据格式
记录为 epoch milliseconds（UTC 时间戳）；展示时使用设备本地时区。偏好设置键包括：
- `events`: 逗号分隔的时间戳列表
- `total`: 累计打卡数（从 events 计算）
- `last_success`: 最近一次打卡时间

## 排错
- 若 Gradle 提示 JDK 版本错误：Android Studio → Settings → Build Tools → Gradle → Gradle JDK，选择 JDK 17。
- 若组件未出现：先运行一次 App，再回桌面添加组件。
- 如果点击组件没刷新：确认启动器允许刷新小组件；重新添加小组件后再试。
