package unit_tests;

import junit.framework.TestCase;
import services.scheduler.IdleTime;
import services.scheduler.IdleTimeComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: dmitry
 * Date: 25.12.11
 * Time: 0:20
 */
public class IdleTimeComparatorTest extends TestCase {

    public void testCompare() {
        IdleTime idleTime1 = new IdleTime(1, 100);
        IdleTime idleTime2 = new IdleTime(2, 200);

        List<IdleTime> idleTimes = new ArrayList<IdleTime>();
        idleTimes.add(idleTime2);
        idleTimes.add(idleTime1);

        Collections.sort(idleTimes, new IdleTimeComparator());
        assertEquals(idleTimes.get(0).getProcessorIndex(), 1);
    }
}
