package com.sewell.qqbot.config;

import com.sewell.qqbot.util.SpringBeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringExtConfiguration {

    /**
     * Application context aware application context aware.
     *
     * @return the application context aware
     */
    @Bean
    public ApplicationContextAware applicationContextAware() {
        return new BotApplicationContextAware();
    }

    /**
     * The type Soul application context aware.
     */
    public static class BotApplicationContextAware implements ApplicationContextAware {

        @Override
        public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
            SpringBeanUtils.getInstance().setCfgContext((ConfigurableApplicationContext) applicationContext);
        }
    }
}