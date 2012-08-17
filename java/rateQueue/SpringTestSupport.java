import junit.framework.TestCase;
import org.springframework.context.support.AbstractApplicationContext;

public abstract class SpringTestSupport extends TestCase {

    protected AbstractApplicationContext context;

    protected void setUp() throws Exception {
        context = createApplicationContext();
    }

    protected abstract AbstractApplicationContext createApplicationContext();

    protected void tearDown() throws Exception {
        if (context != null) {
            context.destroy();
        }
    }

    protected Object getBean(String name) {
        Object bean = context.getBean(name);
        if (bean == null) {
            fail("Should have found bean named '" + name + "' in the Spring ApplicationContext");
        }
        return bean;
    }
}
