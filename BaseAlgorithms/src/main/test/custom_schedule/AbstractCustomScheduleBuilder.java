package custom_schedule;

import dao.GraphDAO;
import dao.TasksDAO;
import domain.ga.Schedule;

/**
 * User: dmitry
 * Date: 03.01.12
 * Time: 20:40
 */
public abstract class AbstractCustomScheduleBuilder implements CustomScheduleBuilder, TasksDAO, GraphDAO {

    protected long defaultTime = 1000000;
    protected int defaultTasksNumber = 500;

    protected Schedule idealSchedule;
    protected int processorsNumber;
    protected long time;

    /**
     * Constructor.
     */
    public AbstractCustomScheduleBuilder(int processorsNumber) {
        idealSchedule = new Schedule(processorsNumber);
        this.processorsNumber = processorsNumber;
    }

    /**
     * Constructor.
     */
    public AbstractCustomScheduleBuilder(int processorsNumber, long customTime, int customTasksNumber) {
        idealSchedule = new Schedule(processorsNumber);
        this.processorsNumber = processorsNumber;
        this.defaultTime = customTime;
        this.defaultTasksNumber = customTasksNumber;
    }

    public Schedule getIdealSchedule() {
        return idealSchedule;
    }

    // Must be implement in the descendants.
    public abstract void buildSchedule(long time, int tasksNumber);
    public abstract void buildGraph(byte relationsPercentage);
}
