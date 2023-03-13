# Weather

### 介绍

书籍《第一行代码 Android 第三版》 天气预报APP  SunnyWeather的**升级版**。该项目主要增加了24小时预报功能和地点管理功能（可对常用的地点进行添加或者对地点管理中的已添加的地点进行删除）。APP页面如下：

<img src=".\screenshots\1.jpg" style="width:25%;" /><img src=".\screenshots\2.jpg" style="width:25%;" /><img src=".\screenshots\3.jpg" style="width:25%;" />
<img src=".\screenshots\4.jpg" style="width:25%;" /><img src=".\screenshots\5.jpg" style="width:25%;" /><img src=".\screenshots\6.jpg" style="width:25%;" />

对之前SunnyWeather项目的改进主要有以下几点：

- 显示天气信息页面右上角新增添加按钮，用于将该页面地点添加到地点管理中；
- 天气信息页面新增24小时预报功能；
- 将5天预报修改为7天预报，并在日期前加入星期；
- 新增地点管理功能，使用Room框架存储保存的地点信息。

使用说明：

- 天气信息页面左滑或者点击左上角图标进入地点管理功能；
- 天气信息页面点击右上角加号图标将该地点保存到地点管理中；
- 地点管理页面点击搜索地址进入搜索地址页面，在地址栏输入地址搜索地点，点击地点跳转天气信息页面；
- 地点管理页面点击地点切换城市，长按地点选择是否删除该地点。
