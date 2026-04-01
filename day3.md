# 后端

## 整理swagger+knife4j接口

```xml fold:swagger依赖
<!-- swagger 接口文档 -->
<dependency>  
    <groupId>io.springfox</groupId>  
    <artifactId>springfox-swagger2</artifactId>  
    <version>2.9.2</version>  
</dependency>  
<dependency>  
    <groupId>io.springfox</groupId>  
    <artifactId>springfox-swagger-ui</artifactId>  
    <version>2.9.2</version>  
</dependency>
```

```xml fold:knife4j依赖
      <!-- knife4j 接口文档 -->  
<dependency>  
    <groupId>com.github.xiaoymin</groupId>  
    <artifactId>knife4j-spring-boot-starter</artifactId>  
    <version>2.0.7</version>  
</dependency>
```

配置文件 ``SwaggerConfig`` 

```java fold:swaggerConfig.java
package com.yupi.yupao.config;  
  
import org.springframework.context.annotation.Bean;  
import org.springframework.context.annotation.Configuration;  
import org.springframework.context.annotation.Profile;  
import springfox.documentation.builders.ApiInfoBuilder;  
import springfox.documentation.builders.PathSelectors;  
import springfox.documentation.builders.RequestHandlerSelectors;  
import springfox.documentation.service.ApiInfo;  
import springfox.documentation.service.Contact;  
import springfox.documentation.spi.DocumentationType;  
import springfox.documentation.spring.web.plugins.Docket;  
import springfox.documentation.swagger2.annotations.EnableSwagger2;  
  
/**  
 * @author: 小yu  
 * @ClassName: yupao-backend  
 * @Description: 自定义 Swagger 接口文档的配置  
 */  
@Configuration // 配置类  
@EnableSwagger2 // 开启 swagger2 的自动配置  
@Profile({"dev","test"})  
public class SwaggerConfig {  
    @Bean  
    public Docket docket() {  
        // 创建一个 swagger 的 bean 实例  
        return new Docket(DocumentationType.SWAGGER_2)  
                .apiInfo(apiInfo())  
                .pathMapping("/api")  
                // 配置接口信息  
                .select() // 设置扫描接口  
                // 配置如何扫描接口  
                .apis(RequestHandlerSelectors  
                                //.any() // 扫描全部的接口，默认  
                                //.none() // 全部不扫描  
                                .basePackage("com.yupi.yupao.controller") // 扫描指定包下的接口，最为常用  
                        //.withClassAnnotation(RestController.class) // 扫描带有指定注解的类下所有接口  
                        //.withMethodAnnotation(PostMapping.class) // 扫描带有只当注解的方法接口  
  
                )  
                .paths(PathSelectors  
                                .any() // 满足条件的路径，该断言总为true  
                        //.none() // 不满足条件的路径，该断言总为false（可用于生成环境屏蔽 swagger）  
                        //.ant("/user/**") // 满足字符串表达式路径  
                        //.regex("") // 符合正则的路径  
                )  
  
                .build();  
    }  
      
    // 基本信息设置  
    private ApiInfo apiInfo() {  
        Contact contact = new Contact(  
                "xiaoyu", // 作者姓名  
                "xiaoyu.cn", // 作者网址  
                "2275159752@qq.com"); // 作者邮箱  
        return new ApiInfoBuilder()  
                .title("鱼泡伙伴匹配系统-接口文档") // 标题  
                .description("try to do better") // 描述  
                .termsOfServiceUrl("https://www.baidu.com") // 跳转连接  
                .version("1.0") // 版本  
                .license("Swagger-的使用(详细教程)")  
                .licenseUrl("https://blog.csdn.net/xhmico/article/details/125353535")  
                .contact(contact)  
                .build();  
    }  
  
}
```
调试接口-swagger-ui界面
	http://localhost:8080/api/swagger-ui.html
	 接口文档页面
	http://localhost:8080/api/doc.html
## 读取excel文件

依赖注入：easy excel
([EasyExcel官网](https://easyexcel.opensource.alibaba.com/))

```xml fold:easyExcel依赖
<!-- easy Excel -->  
<dependency>  
    <groupId>com.alibaba</groupId>  
    <artifactId>easyexcel</artifactId>  
    <version>3.1.0</version>  
</dependency>
```

根据表头来定义具体的类``XingQiuTableUserInfo``
```java fold:XingQiuTableUserInfo
//
@Data  
public class XingQiuTableUserInfo {  
    /**  
     * id     */    @ExcelProperty("成员编号")  
    private String planetCode;  
  
    /**  
     * 用户昵称  
     */  
    @ExcelProperty("成员昵称")  
    private String username;  
  
}
```

事件监听 按着官方文档给的简单的示例
```java fold:TableListener
/**  
 * */// 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去  
@Slf4j  
public class TableListener implements ReadListener<XingQiuTableUserInfo> {  
  
    /**  
     * 这个每一条数据解析都会来调用  
     *  
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}  
     * @param context  
     */  
    @Override  
    public void invoke(XingQiuTableUserInfo data, AnalysisContext context) {  
        System.out.println(data);  
    }  
  
    /**  
     * 所有数据解析完成了 都会来调用  
     *  
     * @param context  
     */  
    @Override  
    public void doAfterAllAnalysed(AnalysisContext context) {  
        System.out.println("数据解析完成");  
    }  
  
}
```

读取excel 两种方法，这里采用监听器方法
```java fold:ImportExcel
public class ImportExcel {  
    /**  
     * 读取excel  
     * @param args  
     */  
    public static void main(String[] args) {  
        // 写法1：JDK8+ ,不用额外写一个DemoDataListener  
        // since: 3.0.0-beta1        // 文件名先写死  
        String fileName = "D:\\projects\\testExcel.xlsx";  
        readByListener(fileName);  
    }  
  
    /**  
     * 监听器读取  
     * @param fileName  
     */  
    public static void readByListener(String fileName){  
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭  
        // 这里每次会读取100条数据 然后返回过来 直接调用使用数据就行  
        EasyExcel.read(fileName, XingQiuTableUserInfo.class, new TableListener()).sheet().doRead();  
    }  
  
    /**  
     * 同步读  
     * 同步地返回，会一次性读取所有数据，  
     * @param fileName  
     */  
    public static void synchronousRead(String fileName){  
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 同步读取会自动finish  
        List<XingQiuTableUserInfo> list = EasyExcel.read(fileName).head(XingQiuTableUserInfo.class).sheet().doReadSync();  
        for (XingQiuTableUserInfo xingQiuTableUserInfo : list) {  
            System.out.println(xingQiuTableUserInfo);  
        }  
    }  
  
}
```

写入数据库
```java fold:ImportXingQiuUser 

//写入数据库 但暂时没有做完
public class ImportXingQiuUser {  
    public static void main(String[] args) {  
        // 写法1：JDK8+ ,不用额外写一个DemoDataListener  
        // since: 3.0.0-beta1        // 文件名先写死  
        String fileName = "D:\\projects\\testExcel.xlsx";  
        List<XingQiuTableUserInfo> userInfoList = EasyExcel.read(fileName, XingQiuTableUserInfo.class, new TableListener()).sheet().doReadSync();  
        System.out.println("总数 = " + userInfoList.size());  
        /**  
         * 使用stream将取出的数据按昵称过滤  
         *  username 非空的元素按 username 分组  
         */  
        Map<String, List<XingQiuTableUserInfo>> listMap = userInfoList.stream()  
                .filter(userInfo -> StringUtils.isNotEmpty(userInfo.getUsername()))  
                .collect(Collectors.groupingBy(XingQiuTableUserInfo::getUsername));  
        for(Map.Entry<String,List<XingQiuTableUserInfo>> stringListEntry : listMap.entrySet()){  
            if (stringListEntry.getValue().size()>1){  
                System.out.println("username = "+stringListEntry.getKey());  
                System.out.println("有重复的");  
            }  
        }  
        System.out.println("不重复的昵称数 = "+listMap.keySet().size());  
    }  
}
```


[obsidian代码块插件文档](https://pkmer.cn/Pkmer-Docs/10-obsidian/obsidian%E5%A4%96%E8%A7%82/obsidian%E7%9A%84css%E4%BB%A3%E7%A0%81%E7%89%87%E6%AE%B5/)

