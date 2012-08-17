
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

/**
 * User: mammelma
 * Date: Jul 23, 2010
 * Time: 1:01:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class RateQueueTest extends SpringTestSupport {
    ThreadHelper threadHelper = new ThreadHelper();

    public AbstractApplicationContext createApplicationContext()
    {
        String config = "testRateQueueContext.xml";
        return new ClassPathXmlApplicationContext(config);
    }

    public void testProfiles1()
    {
        RateQueue queue = (RateQueue)this.getBean("quickQueue");
        EventProfileTaskRunner profileRunner = new EventProfileTaskRunner(eventProfiles,queue,1000L);
        profileRunner.runProfiles("Sparse","Bell","Solid","Solid","Bell","Dense");
        System.out.println(queue.getHistoryString());
    }

    private static String [][] eventProfiles = {
            { "Dense",
              "****---*-*-*****-**-*****-**-*--**-*-*-**-*--*-*-**-*-*****-*-*--*********************" },
            { "Sparse",
              "*--------------*------*------*---*-----*-------*------*--*----*------**-----*------*--*" },
            { "Tailoff",
              "******-*****-******-**-**-*-**-*-*-*---*----*------*----*---------*----------*--------*-" },
            { "Bell",
              "*-----------*----*----*---*--*-*--*-*-*--*-*-**********-***-***-*-*-*--*---*----*------*----*--" },
            { "Solid",
              "***********************************************************************************************" },
            { "Rampup",
              "-------------------*-----------*----------*-------*--*------*----*-*-*-*-----*-*-*-*-*-***********" }
    };
}
