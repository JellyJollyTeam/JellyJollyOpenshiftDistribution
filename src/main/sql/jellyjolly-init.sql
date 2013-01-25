INSERT INTO `jellyjolly_schema`.`jj_blog_info`
(`blog_title`,`blog_subtitle`)
VALUES ('我的博客','使用JellyJolly')
;

INSERT INTO `jellyjolly_schema`.`jj_categories`
(`category_id`,`name`)
VALUES (1,'默认')
;

INSERT INTO `jellyjolly_schema`.`jj_blog_posts`
(`author_user_id`,`category_id`,`post_date`,`post_title`,`post_content`)
VALUES (0,1,NOW(),'欢迎使用JellyJolly博客引擎','<p><span style=\"line-height: 1.6em;\">首先恭喜您已安装成功，如果这是您第一次使用JellyJolly，我会在下面为您介绍如何使用JellyJolly。</span></p>\r\n\r\n<h2>第一步</h2>\r\n\r\n<p style=\"margin-left: 40px;\">如果你还没建立您的账户以及初始化博客，请访问<a href=\"/install\">初始化脚本</a>。</p>\r\n\r\n<h2>了解JellyJolly有哪些功能</h2>\r\n\r\n<ol style=\"margin-left: 40px;\">\r\n	<li><a href=\"/\">主页</a> &nbsp; &nbsp; &nbsp; &nbsp;最新的博文列表</li>\r\n	<li>分类 &nbsp; &nbsp; &nbsp; &nbsp;将博文按分类显示</li>\r\n	<li>页面 &nbsp; &nbsp; &nbsp; &nbsp;可以写些个人介绍</li>\r\n	<li>链接 &nbsp; &nbsp; &nbsp; &nbsp;提供外部站点访问的快捷通道</li>\r\n	<li><a href=\"/login\">控制台</a> &nbsp; &nbsp;管理博客</li>\r\n	<li><a href=\"/rss\">RSS</a> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 您的访客可以通过订阅的方式及时获取您的更新</li>\r\n</ol>\r\n\r\n<p style=\"margin-left: 40px;\">还有更多的功能来等您发现:)</p>\r\n\r\n<h1>如何发表博文？</h1>\r\n\r\n<ol style=\"margin-left: 40px;\">\r\n	<li>点击主页左侧的功能栏中的<a href=\"/login\">登录</a></li>\r\n	<li>点击控制台的文章栏中&ldquo;写文章&rdquo;</li>\r\n</ol>\r\n\r\n<h1>还有问题？</h1>\r\n\r\n<p style=\"margin-left: 40px;\">访问我们的<a href=\"https://github.com/JellyJollyTeam/JellyJollyOpenshiftDistribution\">项目主页</a>，或者通过<a href=\"mailto:predator.ray@gmail.com?subject=%E5%85%B3%E4%BA%8EJellyJolly%E5%8D%9A%E5%AE%A2%E5%BC%95%E6%93%8E%E7%9A%84%E9%97%AE%E9%A2%98\">邮件</a>联系我们。</p>\r\n\r\n<p>&nbsp;</p>\r\n')
;

