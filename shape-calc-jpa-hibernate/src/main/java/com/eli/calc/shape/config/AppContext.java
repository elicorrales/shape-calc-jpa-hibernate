package com.eli.calc.shape.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.eli.calc.shape.service.CalculatedResults;
import com.eli.calc.shape.service.PendingRequests;
import com.eli.calc.shape.service.impl.CalculatedResultsImpl;
import com.eli.calc.shape.service.impl.PendingRequestsImpl;

@Configuration
@ComponentScan(basePackages="com.eli.calc.shape")
@PropertySource("classpath:application.properties")
public class AppContext {

    @Autowired
    private Environment env; // to have access to application.properties

    @Bean(name="executor")
    ExecutorService getExecutor() {

        return Executors.newFixedThreadPool(
            Integer.parseInt(env.getRequiredProperty("executor.threadpool.size"))
        );
    }

}