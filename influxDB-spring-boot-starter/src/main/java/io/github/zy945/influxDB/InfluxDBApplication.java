package io.github.zy945.influxDB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 伍六七
 * @date 2023/7/1 22:32
 */
@SpringBootApplication
public class InfluxDBApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(InfluxDBApplication.class);
        springApplication.run(args);
    }
}
