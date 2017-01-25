package ch.heig.amt.g4mify.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author ldavid
 * @created 11/21/16
 */
@Component
public class EntityJsonDeserializer extends JsonDeserializer<JsonEntity> {

    private static final Logger LOG = Logger.getLogger(EntityJsonDeserializer.class.getSimpleName());

    private EntityManager entityManager;

    @Override
    public Class<?> handledType() {
        return JsonEntity.class;
    }

    @Override
    public JsonEntity deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        LOG.info("Deserializer called");
        return null;
    }
}
