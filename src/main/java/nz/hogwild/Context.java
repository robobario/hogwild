package nz.hogwild;

import nz.hogwild.service.SessionStore;
import nz.hogwild.service.StoryService;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "nz.hogwild.web")
public class Context extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/");
    }

    @Resource
    private Environment env;

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(restDataSource());
        sessionFactory.setPackagesToScan("nz.hogwild.model");
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }

    @Bean
    public DataSource restDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(getEnv("jdbc.driverClassName"));
        dataSource.setUrl(getEnv("jdbc.url"));
        dataSource.setUsername(getEnv("jdbc.user"));
        String key = "jdbc.pass";
        dataSource.setPassword(getEnv(key));

        return dataSource;
    }

    @Bean
    public StoryService storyService(){
        return new StoryService();
    }

    @Bean
    public SessionStore sessionStore(){
        return SessionStore.sessionStore();
    }

    private String getEnv(String key) {
        String property = env.getProperty(key);
        if(property == null || property.isEmpty()){
            throw new RuntimeException("required prop " + key + " missing");
        }
        return property;
    }

    @Bean
    @Resource
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);

        return txManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    Properties hibernateProperties() {
        return new Properties() {
            {
                String key = "hibernate.hbm2ddl.auto";
                setProperty("hibernate.hbm2ddl.auto", getEnv(key));
                setProperty("hibernate.dialect", getEnv("hibernate.dialect"));
                setProperty("hibernate.globally_quoted_identifiers", "true");
            }
        };
    }

}