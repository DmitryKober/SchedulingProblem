package services.scheduler;

import domain.ga.TasksPool;

import java.util.List;

/**
 * User: dmitry
 * Date: 16.12.11
 * Time: 3:58
 */
public interface Scheduler {

    /**
     * Builds a schedule.
     */
    public void schedule();

    /**
     * Performs an iteration.
     * Returns a ready tasks list.
     */
    public List<Integer> performNextIteration();

    /**
     * Returns a list of ready tasks.
     */
    public TasksPool getReadyTasksPool();

    /**
     * Performs an iteration of the scheduling algorithm.
     * Operates with the specified task from the ready tasks pool.
     *
     * @param taskID the identifier of the task to be proceeded.
     *
     */
    public void performCustomIteration(Integer taskID);

}
