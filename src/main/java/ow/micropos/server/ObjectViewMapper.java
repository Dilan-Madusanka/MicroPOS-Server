package ow.micropos.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObjectViewMapper {

    @Autowired
    ObjectMapper mapper;

    public ObjectViewMapper() {}

    public String asString(Object object, Class<?> view) {
        try {
            return mapper.writerWithView(view).writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

}
