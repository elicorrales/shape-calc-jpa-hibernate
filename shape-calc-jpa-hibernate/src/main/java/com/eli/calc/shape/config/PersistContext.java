package com.eli.calc.shape.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages={"com.eli.calc.shape.persistence.repository"})
@Lazy
public class PersistContext {

    @Autowired
    private Environment env; // to have access to application.properties

	@Bean
	public DataSource dataSource() {
		HikariConfig dataSourceConfig = new HikariConfig();
		dataSourceConfig.setDriverClassName(env.getRequiredProperty("jdbc.driverClass"));
		dataSourceConfig.setJdbcUrl(env.getRequiredProperty("jdbc.url"));
		dataSourceConfig.setUsername(env.getRequiredProperty("jdbc.username"));
		dataSourceConfig.setPassword(env.getRequiredProperty("jdbc.password"));
		
		return new HikariDataSource(dataSourceConfig);
	}

	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		
		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		
		//jpaVendorAdapter.setDatabase(Database.MYSQL);
		//jpaVendorAdapter.setGenerateDdl(true);
		//jpaVendorAdapter.setShowSql(true);

		return jpaVendorAdapter;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

		LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();

		entityManagerFactory.setDataSource(dataSource());
		entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter());
		entityManagerFactory.setPackagesToScan("com.eli.calc.shape.domain");

		Properties jpaProperties = new Properties();
		jpaProperties.setProperty("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));
		jpaProperties.setProperty("hibernate.hbm2ddl.auto", env.getRequiredProperty("hibernate.hbm2ddl.auto"));
		jpaProperties.setProperty("hibernate.ejb.naming_strategy", env.getRequiredProperty("hibernate.ejb.naming_strategy"));
		jpaProperties.setProperty("hibernate.show_sql", env.getRequiredProperty("hibernate.show_sql"));
		jpaProperties.setProperty("hibernate.format_sql", env.getRequiredProperty("hibernate.format_sql"));
		jpaProperties.setProperty("org.hibernate.FlushMode", "commit");

		entityManagerFactory.setJpaProperties(jpaProperties);
		
		return entityManagerFactory;
	}

	@Bean
	public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
		return jpaTransactionManager;
	}


}
