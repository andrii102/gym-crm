package com.dre.gymapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "com.dre.gymapp")
@PropertySource("classpath:application.properties")
@EnableTransactionManagement(proxyTargetClass = true)
public class AppConfig {
    private final Environment env;

    public AppConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(env.getProperty("db.driver"));
        ds.setUrl(env.getProperty("db.url"));
        ds.setUsername(env.getProperty("db.username"));
        ds.setPassword(env.getProperty("db.password"));
        return ds;
    }

    public Properties jpaProperties(){
        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect", env.getProperty("db.dialect"));
        jpaProperties.put("hibernate.show_sql", env.getProperty("db.show_sql"));
        jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("db.hbm2ddl.auto"));
        jpaProperties.put("hibernate.hbm2ddl.import_files", env.getProperty("db.hbm2ddl.import_files"));
        jpaProperties.put("hibernate.hbm2ddl.import_files_sql_extractor",
                "org.hibernate.tool.schema.internal.script.MultiLineSqlScriptExtractor");
        return jpaProperties;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource());
        emf.setPackagesToScan("com.dre.gymapp.model");
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emf.setJpaProperties(jpaProperties());
        return emf;
    }

    @Bean
    public JpaTransactionManager transactionManager(){
        return new JpaTransactionManager(entityManagerFactory().getObject());
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("GYM-CRM API")
                        .version("1.0.0")
                        .description("API documentation for GYM CRM")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}

