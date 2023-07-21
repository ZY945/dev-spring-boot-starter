package io.github.zy945.dingtalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 伍六七
 * @date 2023/7/9 12:27
 */
@SpringBootApplication
public class DingTalkApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(DingTalkApplication.class);
        springApplication.run(args);
    }
}
