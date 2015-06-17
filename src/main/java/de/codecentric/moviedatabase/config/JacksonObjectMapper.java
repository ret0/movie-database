package de.codecentric.moviedatabase.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.PostConstruct;


public class JacksonObjectMapper extends ObjectMapper {


    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        //configure(DeserializationConfig. Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

}
