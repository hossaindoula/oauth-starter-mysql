package info.doula.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by tasnim on 5/28/2017.
 */

@Component
public class ApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext _appContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        _appContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return _appContext;
    }
}
