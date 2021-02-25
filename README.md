# db-exchange
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mengzuozhu/db-exchange-core/badge.svg?style=flat)](https://mvnrepository.com/artifact/com.github.mengzuozhu/db-exchange-core)  

**数据库分批读写交互模板**

## 功能

- 封装数据分批流式读写模板
- 封装mysql, es和mongo数据库的读写模板

## 模块

- `db-exchange-core`：核心读写交互模板
- `db-exchange-mysql`：封装mysql数据库读写
- `db-exchange-es`：封装es数据库读写
- `db-exchange-mongo`：封装mongo数据库读写
- `db-exchange-spring-jdbc`：封装Spring JDBC读写

## 快速开始

1. 添加`db-exchange-mysql` Maven引用

   ```xml
           <dependency>
               <groupId>com.github.mengzuozhu</groupId>
               <artifactId>db-exchange-mysql</artifactId>
               <version>1.0.1</version>
           </dependency>
   ```

2. mysql读取示例

   ```java
   public class ExchangeDemo {
   
       public static void main(String[] args) {
           String url = "jdbc:mysql://localhost:3306/test?characterEncoding=utf8&serverTimezone=Asia/Shanghai";
           DataSource dataSource = MysqlUtil.getDataSource(url, "root", "your mysql password");
           MysqlQuery query = MysqlQuery.builder()
                   .dataSource(dataSource)
                   .sql("SELECT * FROM user")
                   .batchSize(100)
                   .build();
           DataReader<Map<String, Object>> mysqlDataReader = new MysqlDataReader<>(query, new ResultSetToMap());
           ListDataCollector<Map<String, Object>> listDataCollector = new ListDataCollector<>();
           ExchangeTemplate.from(mysqlDataReader)
                   .execute(listDataCollector)
                   .await();
           // do something
           List<Map<String, Object>> result = listDataCollector.getResult();
       }
   }
   ```

