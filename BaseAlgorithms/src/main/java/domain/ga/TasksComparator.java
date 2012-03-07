package domain.ga;

import java.util.Comparator;

/**
 * User: dmitry
 * Date: 08.01.12
 * Time: 19:35
 */
public class TasksComparator implements Comparator<Task> {
    /**
     * Compares two tasks according to their early beginning times.
     */
    public int compare(Task task1, Task task2) {
        long task1EarlyBegTime = task1.getEarlyBeginningTime();
        long task2earlyBegTime = task2.getEarlyBeginningTime();

        if (task1EarlyBegTime > task2earlyBegTime) {
            return 1;
        } else if (task1EarlyBegTime < task2earlyBegTime) {
            return -1;
        } else {
            return 0;
        }

    }

}
