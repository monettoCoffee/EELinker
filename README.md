# EELinker Description
Unify WebApplication Solution
<br>EEL contains Router, ORM, and WebContainer.
<br>For learning purposes,
<br>it is a head-to-toe rewrite that allows the entire Web project to do simple configuration and launch main-function.
<br>All components are inherited.
<br>This makes it look like "JEE SSM".
<br>
<br>这是一个致力于提供一体化Web应用程序的解决方案。
<br>(Java)EELinker包括 路由层, 持久层, Web容器 三个模块。
<br>出于学习的目的, 这些都是底层从零构建的, 完全由我们自己实现。
<br>整个Web工程只需要做一些简单的配置, 然后启动主函数。
<br>它可能包含了你需要的所有组件。
<br>因此它可能看起来比较像现在普遍的Java EE集成项目。

# External Dependencies
If you use ORM-MySQL model, it's need mysql-connector-java.
<br>It's based on Java 8, Router need parameter "-parameters" to work.
<br>Nothing else, like servlet-api and tomcat.
<br>
<br>如果你需要使用MySQL持久层, 那么这将需要MySQL-Java驱动(事实上你可以替换掉它)。
<br>整个项目需要基于Java 8, 路由层需要加入启动参数 -parameters 才能进行参数映射。
<br>除此之外, 这将不需要任何外部依赖组件甚至包括Servlet(DOM4J将在不久移除)。
