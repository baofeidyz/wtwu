# WTWU

Watch TV With You
和你一起看电视

通过油猴脚本监听H5的video播放器相关事件，使用websocket协议做信息同步，使得异地两个人或者多个人可以同步一起看视频，且相互之间的进度条是近乎一致的。

# 使用方法

1. 你需要找一台服务器，把这个代码跑起来
2. 客户端是需要支持油猴插件的，比如chrome浏览器
3. 安装我写的脚本，地址是：https://greasyfork.org/zh-CN/scripts/421174-watch-tv-with-you
4. 修改脚本中`websockerServerUrl`地址
5. 和你的好朋友一起看电视吧

# 需要注意

1. 后台代码写得非常简单，甚至接口都没有加密和用户限制
2. 油猴脚本没有加URL检测，所以理论上，客户端打开的页面不同，还是会同步video的相关事件
3. 因为大部分视频网站都是采用的HTTPS协议，所以websocket也需要SSL