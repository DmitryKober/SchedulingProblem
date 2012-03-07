package custom_schedule;

/**
 * User: dmitry
 * Date: 03.01.12
 * Time: 20:34
 */
public interface CustomScheduleBuilder {

    // creates tasks and idealSchedule
    public void buildSchedule(long time, int tasksNumber);

    // builds a partial order graph based on
    // idealSchedule, that was created via previous method.
    public void buildGraph(byte relationsPercentage);

}
