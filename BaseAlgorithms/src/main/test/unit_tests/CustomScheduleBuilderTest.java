package unit_tests;

import custom_schedule.CustomScheduleBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * User: dmitry
 * Date: 04.01.12
 * Time: 0:32
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= "/src/main/resources/TestContext.xml")
public class CustomScheduleBuilderTest {

    @Autowired
    private CustomScheduleBuilder scheduleBuilder;

    @Test
    public void buildScheduleTest() {
        scheduleBuilder.buildSchedule(500, 5);
    }

}
