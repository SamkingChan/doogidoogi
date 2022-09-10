package com.sexychan.doogidoogi;

import com.sexychan.doogidoogi.utils.SwingGUI;
import com.sexychan.doogidoogi.utils.view.ConfigFrame;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;


@SpringBootApplication
@Configuration
public class DoogidoogiApplication {

    public static void main(String[] args) {
//        SpringApplication.run(DoogidoogiApplication.class, args);
        SpringApplicationBuilder builder = new SpringApplicationBuilder(DoogidoogiApplication.class);
        builder.headless(false).run(args);
    }
}
