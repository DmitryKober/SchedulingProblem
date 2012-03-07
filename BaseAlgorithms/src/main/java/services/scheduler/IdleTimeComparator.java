package services.scheduler;

import java.util.Comparator;

public class IdleTimeComparator implements Comparator {

    public int compare(Object o1, Object o2) {
        IdleTime idleTime1 = (IdleTime) o1;
        IdleTime idleTime2 = (IdleTime) o2;

        long idleTime1Time = idleTime1.getIdleTime();
        long idleTime2Time = idleTime2.getIdleTime();

        if (idleTime1Time > idleTime2Time) {
            return 1;
        } else if (idleTime1Time < idleTime2Time) {
            return -1;
        } else {
            return 0;
        }
    }
}
