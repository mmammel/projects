import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.reflect.Field;
import java.lang.annotation.Annotation;

public abstract class AbstractMappable {

  protected Map<String,Object> getMap() {
    Annotation a = null;
    MapKey tempKey = null;
    String key = null;

    Map<String,Object> retVal = new HashMap<String,Object>();

    List<Field> fields = new ArrayList<Field>();

    try {
      Class clazz = this.getClass();

      while( clazz != Object.class ) {
        fields.addAll( Arrays.asList( clazz.getDeclaredFields()) );
        clazz = clazz.getSuperclass();
      }

      for( Field f : fields ) {
        if( f.isAnnotationPresent( MapKey.class ) ) {
          f.setAccessible(true);
          a = f.getAnnotation(MapKey.class);
          tempKey = (MapKey)a;
          key = tempKey.value();
          retVal.put( key, f.get(this) );
        }
      }
    } catch( Exception e ) {
      System.out.println( "Access error: " + e.toString() );
      retVal = null;
    }
    return retVal;
  }

  protected Map<String,Object> getMap(Map<String,Object> addons) {
    Map<String,Object> retVal = this.getMap();
    if( addons != null ) {
      retVal.putAll( addons );
    }

    return retVal;
  }

}
