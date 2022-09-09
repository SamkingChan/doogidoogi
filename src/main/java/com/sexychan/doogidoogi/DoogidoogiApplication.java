package com.sexychan.doogidoogi;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@SpringBootApplication
@Configuration
@ComponentScan({"com.sexychan.doogidoogi"})
public class DoogidoogiApplication {

    public static void main(String[] args) {
//        SpringApplication.run(DoogidoogiApplication.class, args);
        SpringApplicationBuilder builder = new SpringApplicationBuilder(DoogidoogiApplication.class);
        builder.headless(false).run(args);
    }

}
