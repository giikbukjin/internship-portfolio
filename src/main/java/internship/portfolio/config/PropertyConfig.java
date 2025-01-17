package internship.portfolio.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
        // env.properties 파일 소스 등록
        @PropertySource("classpath:properties/env.properties")
})
public class PropertyConfig {

}