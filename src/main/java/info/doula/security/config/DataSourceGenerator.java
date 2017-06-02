package info.doula.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Created by hossaindoula<hossain.doula@itconquest.com> on 2017-05-24.
 */

@Component
@PropertySource({ "classpath:application.properties" })
public class DataSourceGenerator {
//https://www.xvideos.com/video20087731/ann_marie_la_sante_fucks_a_grandpa
    //https://www.xvideos.com/video27019005/crafty_old_grandpa_gives_adorable_teen_best_orgasm_of_her_life
    @Autowired
    private Environment env;

    public DataSource generate(){
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(env.getProperty("spring.datasource.url"));
        dataSourceBuilder.driverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSourceBuilder.username(env.getProperty("spring.datasource.username"));
        dataSourceBuilder.password(env.getProperty("spring.datasource.password"));
        return dataSourceBuilder.build();
    }
}
