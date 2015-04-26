
package grocery;

import grocery.models.Fruit;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;



/**
 * Useful test methods 
 *
 * @author   Tomek Szpinda
 */
public class JSONUtil
{
    @SuppressWarnings("unchecked")
    public static <T> T deserializeJSON(String json, TypeReference<List<Fruit>> typeReference) 
    {
        try
        {
            return (T) getObjectMapper().readValue(json, typeReference);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Unable to conver json string to object", e);
        }
    }

    
    private static ObjectMapper getObjectMapper()
    {
        ObjectMapper mapper = new ObjectMapper();
        configureObjectMapper(mapper);
        return mapper;
    }

    private static void configureObjectMapper(ObjectMapper mapper)
    {
        mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }
}
