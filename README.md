# Weather

书籍《第一行代码 Android 第三版》 天气预报APP  SunnyWeather的**升级版**。

### 示例图片

<p align="left">
   <img src=".\images\1.jpg" style="width:200px;" /><img src=".\images\2.jpg" style="width:200px;" /><img src=".\images\3.jpg" style="width:200px;" /><img src=".\images\4.jpg" style="width:200px;" />
</p>


### 下载

#### Apk下载链接：[Apk下载链接](https://github.com/linhao1998/Weather/releases/download/1.1.0/app-release.apk)

### 新增功能

- 提供更加完整的天气信息，包括：
  - 新增体感温度，风向，风力和湿度实时天气信息；
  - 新增24小时预报功能；
  - 将5天预报修改为7天预报。

- 新增地点管理功能，可以对常用地点进行保存；
- 对深色主题进行适配。

### 新增技术栈

- 使用自定义View绘制实时天气信息View；
- 使用Room框架对存储的地点信息进行管理。

### 项目架构示意图

项目使用MVVM架构模式实现。

<img src=".\images\weather_architecture.png" style="width:75%"/>