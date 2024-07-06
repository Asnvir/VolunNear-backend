package com.volunnear;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentLogger implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(EnvironmentLogger.class);
    private final Environment env;

    public EnvironmentLogger(Environment env) {
        this.env = env;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Logging all environment variables:");

        // Log active profiles
        for (String profile : env.getActiveProfiles()) {
            logger.info("Active profile: {}", profile);
        }

        // Log system environment variables
        System.getenv().forEach((key, value) -> logger.info("System Env var: {}={}", key, value));

        // Log property sources
        if (env instanceof StandardEnvironment standardEnvironment) {
            for (PropertySource<?> propertySource : standardEnvironment.getPropertySources()) {
                logger.info("PropertySource: {}", propertySource.getName());
                if (propertySource.containsProperty("APP_DB_URL")) {
                    logger.info("APP_DB_URL={}", propertySource.getProperty("APP_DB_URL"));
                }
                if (propertySource.containsProperty("EXTERNAL_APP_PORT")) {
                    logger.info("EXTERNAL_APP_PORT={}", propertySource.getProperty("EXTERNAL_APP_PORT"));
                }
                // Log other specific properties as needed
            }
        }
    }
}
