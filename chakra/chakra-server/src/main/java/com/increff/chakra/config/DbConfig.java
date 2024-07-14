package com.increff.chakra.config;

import com.increff.commons.sql.DbPoolUtil;
import com.nextscm.commons.spring.server.SnakeCaseNamingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class DbConfig {

    @Value("${jdbc.driverClassName}")
    private String jdbcDriver;
    @Value("${jdbc.url}")
    private String jdbcUrl;
    @Value("${jdbc.username}")
    private String jdbcUsername;
    @Value("${jdbc.password}")
    private String jdbcPassword;
    @Value("${hibernate.dialect}")
    private String hibernateDialect;
    @Value("${hibernate.show_sql}")
    private String hibernateShowSql;
    @Value("${hibernate.hbm2ddl}")
    private String hibernateHbm2ddlAuto;
    @Value("${hibernate.min.connection:5}")
    private Integer initialSize;
    @Value("${hibernate.max.connection:10}")
    private Integer maxTotal;
    @Value("${hibernate.max.idle:8}")
    private Integer maxIdle;
    private static final String hibernateJdbcBatchSize = "300";

    @Bean(name = "dataSource")
    public DataSource getDataSource() {
        return DbPoolUtil.initDataSource(jdbcDriver, jdbcUrl, jdbcUsername, jdbcPassword, maxIdle, maxTotal);
    }

    @Bean(name = "entityManagerFactory")
    @Autowired
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource);
        bean.setPackagesToScan(SpringConstants.PACKAGE_DB);
        HibernateJpaVendorAdapter jpaAdapter = new HibernateJpaVendorAdapter();
        bean.setJpaVendorAdapter(jpaAdapter);
        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect", hibernateDialect);
        jpaProperties.put("hibernate.show_sql", hibernateShowSql);
        jpaProperties.put("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
        jpaProperties.put("hibernate.jdbc.batch_size", hibernateJdbcBatchSize);
        jpaProperties.put("hibernate.cache.use_second_level_cache", false);
        //jpaProperties.put("hibernate.jdbc.time_zone", TimeZone.getTimeZone(time));
        jpaProperties.put("hibernate.physical_naming_strategy", new SnakeCaseNamingStrategy(""));
        jpaProperties.put("hibernate.id.generator.stored_last_used", false);
        jpaProperties.put("hibernate.model.generator_name_as_sequence_name", false);
        bean.setJpaProperties(jpaProperties);
        return bean;
    }

    @Bean(name = "transactionManager")
    @Autowired
    public JpaTransactionManager getTransactionManager(LocalContainerEntityManagerFactoryBean emf) {
        JpaTransactionManager bean = new JpaTransactionManager();
        bean.setEntityManagerFactory(emf.getObject());
        return bean;
    }
}
