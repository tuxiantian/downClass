这是一个增量发布的工具。  

使用前需在sys.properties中配置项目编译输出的class文件所在路径，以及打包class文件存放的目录,DEVELOP_TOOL变量是配置开发工具。
将要发布的文件的路径记录到file.txt中，执行Main.bat就能将类的class文件打包到classOut目录下。
eclipse拷贝文件路径的方法：在所在文件窗口，alt+enter,Resource-Path：文件路径。
idea拷贝文件路径的方法：在所在文件窗口，文件名上右键-Copy Path或使用快捷键Ctrl+Shift+C。

tool/是可以直接使用的。  
src/是该工具的源码，关于文件的操作，该工具提供了很多值得借鉴的写法。具体参考GetClassFile.java。  