package cn.open52.dreamblogv1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.open52.dreamblogv1.mapper")
public class DreamBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(DreamBlogApplication.class, args);
    }

}