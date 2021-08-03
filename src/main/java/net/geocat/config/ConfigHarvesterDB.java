/*
 *  =============================================================================
 *  ===  Copyright (C) 2021 Food and Agriculture Organization of the
 *  ===  United Nations (FAO-UN), United Nations World Food Programme (WFP)
 *  ===  and United Nations Environment Programme (UNEP)
 *  ===
 *  ===  This program is free software; you can redistribute it and/or modify
 *  ===  it under the terms of the GNU General Public License as published by
 *  ===  the Free Software Foundation; either version 2 of the License, or (at
 *  ===  your option) any later version.
 *  ===
 *  ===  This program is distributed in the hope that it will be useful, but
 *  ===  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  ===  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  ===  General Public License for more details.
 *  ===
 *  ===  You should have received a copy of the GNU General Public License
 *  ===  along with this program; if not, write to the Free Software
 *  ===  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 *  ===
 *  ===  Contact: Jeroen Ticheler - FAO - Viale delle Terme di Caracalla 2,
 *  ===  Rome - Italy. email: geonetwork@osgeo.org
 *  ===
 *  ===  Development of this program was financed by the European Union within
 *  ===  Service Contract NUMBER – 941143 – IPR – 2021 with subject matter
 *  ===  "Facilitating a sustainable evolution and maintenance of the INSPIRE
 *  ===  Geoportal", performed in the period 2021-2023.
 *  ===
 *  ===  Contact: JRC Unit B.6 Digital Economy, Via Enrico Fermi 2749,
 *  ===  21027 Ispra, Italy. email: JRC-INSPIRE-SUPPORT@ec.europa.eu
 *  ==============================================================================
 */

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
        basePackages = {"net.geocat.database.harvester.repos"}
)
public class ConfigHarvesterDB {
    @Autowired
    Environment env;

    @Bean(name = "harvesterdbDataSource")
    @ConfigurationProperties(prefix = "harvesterdb.datasource")
    public DataSource dataSource() {
        DataSource dataSource = DataSourceBuilder.create().build();
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
