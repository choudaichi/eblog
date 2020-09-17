package com.koodo.eblog.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

@Component
public class SpringUtil implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(SpringUtil.class);

    private static ApplicationContext applicationContext = null;
    private static SpringUtil Instance = null;

    public static SpringUtil init() {
        if (Instance == null) {
            synchronized (SpringUtil.class) {
                if (Instance == null) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Instance = new SpringUtil();
                }
            }
        }
        return Instance;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = applicationContext;
    }

    public synchronized static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    /**
     * 发布消息
     */
    public static void publish(ApplicationEvent event) {
        applicationContext.publishEvent(event);
        logger.info("发布消息完毕");
    }
}