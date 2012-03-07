package domain.ga;

import java.util.List;

/**
 * User: dmitry
 * Date: 05.02.12
 * Time: 20:50
 */
public class HistoryEvent {

    private Integer task;   // a task's identifier
    private int processor;  // a processor's id the task is placed on.
    private List<Integer> readyTasks; // a list of tasks which are ready
                                      // after the task was placed on the processor
    private List<Integer> parentBoundChanges; // a list of tasks which parentBound changed after the task
                                              // was placed on the processor

    /**
     * Constructor
     */
    public HistoryEvent(Integer task,
                 int processor,
                 List<Integer> readyTasks,
                 List<Integer> parentBoundChanges) {
        this.task = task;
        this.processor = processor;
        this.readyTasks = readyTasks;
        this.parentBoundChanges = parentBoundChanges;
    }

    public Integer getTask() {
        return task;
    }

    public void setTask(Integer task) {
        this.task = task;
    }

    public int getProcessor() {
        return processor;
    }

    public void setProcessor(int processor) {
        this.processor = processor;
    }

    public List<Integer> getReadyTasks() {
        return readyTasks;
    }

    public void setReadyTasks(List<Integer> readyTasks) {
        this.readyTasks = readyTasks;
    }

    public List<Integer> getParentBoundChanges() {
        return parentBoundChanges;
    }

    public void setParentBoundChanges(List<Integer> parentBoundChanges) {
        this.parentBoundChanges = parentBoundChanges;
    }
}
