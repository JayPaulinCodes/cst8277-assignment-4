/**
 * File:  ConfigureJacksonObjectMapper.java
 * Course materials (23S) CST 8277
 *
 * @author Teddy Yap
 * @author Mike Norman
 * @date 2020 10
 * 
 * Note:  Students do NOT need to change anything in this class.
 */
package acmecollege.rest;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import acmecollege.utility.HttpErrorAsJSONServlet;

@Provider
public class ConfigureJacksonObjectMapper implements ContextResolver<ObjectMapper> {
    
    private final ObjectMapper objectMapper;

    public ConfigureJacksonObjectMapper() {
        this.objectMapper = createObjectMapper();
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return objectMapper;
    }

    //Configure JDK 8's new DateTime objects to use proper ISO-8601 time format
    protected ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            // Lenient parsing of JSON - if a field has a typo, don't fall to pieces
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            
        HttpErrorAsJSONServlet.setObjectMapper(mapper);
        return mapper;
    }
}