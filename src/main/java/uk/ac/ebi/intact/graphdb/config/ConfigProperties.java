package uk.ac.ebi.intact.graphdb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by ntoro on 02/08/2017.
 */
@Configuration
@ConfigurationProperties
@PropertySource("classpath:configprops.properties")
public class ConfigProperties {
}
