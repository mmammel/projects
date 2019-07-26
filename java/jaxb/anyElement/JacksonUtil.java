
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
import java.io.IOException;

public class JacksonUtil {

  public static String getJsonString(Object object, boolean includeRoot)
      throws JsonGenerationException, JsonMappingException, IOException {
    ObjectMapper objectMapper = new ObjectMapper();

    objectMapper.configure(Feature.WRAP_ROOT_VALUE, includeRoot);
    AnnotationIntrospector primary = new JaxbAnnotationIntrospector();
    AnnotationIntrospector secondary = new JacksonAnnotationIntrospector();
    AnnotationIntrospector pair = new AnnotationIntrospector.Pair(primary, secondary);
    objectMapper.setAnnotationIntrospector(pair);

    objectMapper.configure(Feature.DEFAULT_VIEW_INCLUSION, false);
    objectMapper.configure(Feature.AUTO_DETECT_FIELDS, false);
    objectMapper.configure(Feature.AUTO_DETECT_GETTERS, false);
    objectMapper.configure(Feature.AUTO_DETECT_IS_GETTERS, false);
    objectMapper.configure(Feature.FAIL_ON_EMPTY_BEANS, false);
    return objectMapper.writeValueAsString(object);
  }

  public static ObjectMapper getMapper(boolean includeRoot)
      throws JsonGenerationException, JsonMappingException, IOException {
    ObjectMapper objectMapper = new ObjectMapper();

    objectMapper.configure(Feature.WRAP_ROOT_VALUE, includeRoot);
    AnnotationIntrospector primary = new JaxbAnnotationIntrospector();
    AnnotationIntrospector secondary = new JacksonAnnotationIntrospector();
    AnnotationIntrospector pair = new AnnotationIntrospector.Pair(primary, secondary);
    objectMapper.setAnnotationIntrospector(pair);

    objectMapper.configure(Feature.DEFAULT_VIEW_INCLUSION, false);
    objectMapper.configure(Feature.AUTO_DETECT_FIELDS, false);
    objectMapper.configure(Feature.AUTO_DETECT_GETTERS, false);
    objectMapper.configure(Feature.AUTO_DETECT_IS_GETTERS, false);
    objectMapper.configure(Feature.FAIL_ON_EMPTY_BEANS, false);
    return objectMapper;
  }
}
