package com.sexychan.doogidoogi.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertiesConfig {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesConfig.class);
    private static final String APPLICATION_CONFIG_PATH = "config/application.properties";

    @Bean
    public PropertiesConfiguration applicationProperties(){
        return getProperties(APPLICATION_CONFIG_PATH);
    }

    private PropertiesConfiguration getProperties(String path){
        try {
           PropertiesConfiguration configuration =new PropertiesConfiguration(path);
           configuration.setAutoSave(true);
           configuration.setEncoding("UTF-8");
           configuration.setReloadingStrategy(new FileChangedReloadingStrategy());
           return configuration;
        } catch (ConfigurationException e) {
            logger.error("加载配置文件时错误!",e);
        }
        return null;
    }

}
