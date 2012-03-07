package unit_tests;

import domain.ga.Individual;
import domain.ga.Schedule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import services.scheduler.Scheduler;

/**
 * User: dmitry
 * Date: 02.01.12
 * Time: 20:51
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("../resources/TestContext.xml")
public class InitialPopulationSchedulerTest {

    @Autowired
    private Individual individual;
    @Autowired
    private Scheduler scheduler;

    @Test
    public void scheduleTest() {
        final Schedule schedule = individual.getSchedule();

        scheduler.schedule();
    }
}
