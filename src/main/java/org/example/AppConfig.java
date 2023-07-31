package org.example;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.loader.Servlet5Loader;
import io.pebbletemplates.spring.servlet.PebbleViewResolver;
import jakarta.jms.ConnectionFactory;
import jakarta.servlet.ServletContext;
import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.example.service.ChatHandler;
import org.example.service.MessagingService;
import org.example.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.io.File;
import org.example.entity.Message;
@Configuration
@ComponentScan
//@EnableWebMvc // 启用Spring MVC
@EnableTransactionManagement
@PropertySource({"classpath:jdbc.properties", "classpath:jms.properties","classpath:task.properties"})
@EnableWebSocket
@EnableScheduling
@EnableJms
public class AppConfig {

    @Value("${jdbc.driverClassName}")
    String driver;

    @Value("${jdbc.url}")
    String url;
    @Value("${jdbc.username}")
    String username;
    @Value("${jdbc.password}")
    String password;

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        TaskService taskService =  context.getBean(TaskService.class);
        taskService.cronDailyReport();


    }

//    public static void main(String[] args) throws LifecycleException {
//        Tomcat tomcat = new Tomcat();
//        tomcat.setPort(Integer.getInteger("port", 8080));
//        tomcat.getConnector();
//        Context ctx = tomcat.addWebapp("", new File("src/main/webapp").getAbsolutePath());
//        WebResourceRoot resources = new StandardRoot(ctx);
//        resources.addPreResources(
//                new DirResourceSet(resources, "/WEB-INF/classes", new File("target/classes").getAbsolutePath(), "/"));
//        ctx.setResources(resources);
//        tomcat.start();
//        tomcat.getServer().await();
//
//    }

    @Bean
    DataSource createDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;

    }

    @Bean
    JdbcTemplate createJdbcTemplate(@Autowired DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    PlatformTransactionManager createPlatformTransactionManager(@Autowired DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    WebMvcConfigurer createWebMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/static/**").addResourceLocations("/static/");

            }

        };
    }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Bean
    WebSocketConfigurer createWebSocketConfigurer( @Autowired ChatHandler chatHandler){
            return new WebSocketConfigurer() {
                @Override
                public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
                    registry.addHandler(chatHandler, "/chat");
                }
            };
    }

    @Bean
    ConnectionFactory createJMSConnectionFactory(
            @Value("${jms.uri}") String uri,
            @Value("${jms.username}") String username,
            @Value("${jms.password}") String password)
    {
        return new ActiveMQJMSConnectionFactory(uri, username, password);
    }
    @Bean
    JmsTemplate createJmsTemplate(@Autowired ConnectionFactory connectionFactory) {
        return new JmsTemplate(connectionFactory);
    }

    @Bean("jmsListenerContainerFactory")
    DefaultJmsListenerContainerFactory createJmsListenerContainerFactory(@Autowired ConnectionFactory connectionFactory) {
        var factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper;
    }



}