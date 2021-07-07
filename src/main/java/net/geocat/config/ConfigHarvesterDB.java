package net.geocat.config;


 import com.zaxxer.hikari.HikariDataSource;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.beans.factory.annotation.Qualifier;
 import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
 import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
 import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
 import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
 import org.springframework.core.env.Environment;
 import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;



@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "harvesterdbEntityManagerFactory",
        transactionManagerRef = "harvesterdbTransactionManager",
        basePackages = { "net.geocat.database.harvester.repos" }
)
public class ConfigHarvesterDB {
    @Autowired
    Environment env;

    @Bean(name = "harvesterdbDataSource")
    @ConfigurationProperties(prefix = "harvesterdb.datasource")
    public DataSource dataSource() {
        DataSource dataSource= DataSourceBuilder.create().build();
        if (dataSource instanceof HikariDataSource) {
            HikariDataSource hds = (HikariDataSource) dataSource;
            hds.setDriverClassName(env.getProperty("harvesterdb.datasource.driver-class-name"));
            hds.setJdbcUrl(env.getProperty("harvesterdb.datasource.url"));
            hds.setUsername(env.getProperty("harvesterdb.datasource.username"));
            hds.setPassword(env.getProperty("harvesterdb.datasource.password"));
        }
        return dataSource;
    }

    @Bean(name = "harvesterdbEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean
    harvesterdbEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("harvesterdbDataSource") DataSource dataSource,
            JpaProperties jpaProperties
    ) {
        return
                builder
                        .dataSource(dataSource)
                        .packages("net.geocat.database.harvester.entities")
                        .persistenceUnit("harvester")
                        .properties(jpaProperties.getProperties())
                        .build();
    }
    @Bean(name = "harvesterdbTransactionManager")
    public PlatformTransactionManager harvesterdbTransactionManager(
            @Qualifier("harvesterdbEntityManagerFactory") EntityManagerFactory
                    harvesterdbEntityManagerFactory
    ) {
        return new JpaTransactionManager(harvesterdbEntityManagerFactory);
    }

}
