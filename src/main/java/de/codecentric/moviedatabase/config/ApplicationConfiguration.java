package de.codecentric.moviedatabase.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.nio.charset.Charset;
import java.util.List;

@Configuration
@EnableWebMvc
@Import({ViewConfiguration.class, ControllerConfiguration.class})
public class ApplicationConfiguration extends WebMvcConfigurerAdapter {

    // Maps resources path to webapp/resources
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(jsonHttpMessageConverter());
        converters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
    }

    @Bean
    public JacksonObjectMapper jacksonObjectMapper() {
        return new JacksonObjectMapper();
    }

    @Bean
    public MappingJackson2HttpMessageConverter jsonHttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(jacksonObjectMapper());
        return converter;
    }
}
