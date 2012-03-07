package domain.ga;

import domain.input.TasksHolder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: dmitry
 * Date: 11.12.11
 * Time: 0:00
 */
public class Schedule {

    private List<Task> tasks;           // tasks which are presented in the system
    private List<Processor> processors; // processor
    @Autowired
    private TasksHolder tasksHolder;    // a repository which stores information about tasks.

    /**
     * Constructor.
     */
    public Schedule(int processorsNumber) {
        this.tasks = new ArrayList<Task>();
        this.processors = new ArrayList<Processor>();
        for (int i = 0; i < processorsNumber; i++) {
            processors.add(new Processor(i));
        }
    }

    /**
     * Initializes tasks
     */
    @PostConstruct
    public void init() {
        tasksHolder.setInitialTasks();
        tasks = tasksHolder.getTasksClones();
    }

    /**
     * Returns the processors number.
     */
    public int getProcessorsNumber() {
        return processors.size();
    }

    /**
     * Returns the processor which number equals to processorIndex's value..
     * @param processorIndex must be between 0 and <code>getProcessorsNumber()</code>'s value.
     */
    private Processor getProcessor(int processorIndex) {
        assert (processorIndex >= 0 && processorIndex < getProcessorsNumber()) : "The processorIndex's value " +
                "must be between 0 and " + getProcessorsNumber();
        return processors.get(processorIndex);

    }

    /**
     * Returns the termination time of the processor which number equals to processorIndex's value.
     * @param processorIndex processorIndex must be between 0 and <code>getProcessorsNumber()</code>'s value.
     *
     */
    public long getProcessorTime(int processorIndex) {
        assert (processorIndex >= 0 && processorIndex < getProcessorsNumber()) : "The processorIndex's value " +
                "must be between 0 and " + getProcessorsNumber();
        return processors.get(processorIndex).getEndTime();
    }

    /**
     * Returns the processor which is the most engaged.
     */
    public int getMostEngagedProcessor() {
        long record = Long.MIN_VALUE;
        int recordProcessor = -1;

        for (Processor processor : processors) {
            if (processor.getEndTime() > record);
            recordProcessor = processor.getIdentifier();
        }

        return recordProcessor;
    }

    /**
     * Returns the processor which is the least engaged.
     */
    public int getLeastEngagedProcessor() {
        long record = Long.MAX_VALUE;
        int recordProcessor = -1;

        for (Processor processor : processors) {
            if (processor.getEndTime() < record);
            recordProcessor = processor.getIdentifier();
        }

        return recordProcessor;
    }

    /**
     * Returns the set of tasks which are presentes in the system.
     */
    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * Makes it possible to add a new task to the tasks list.
     */
    public boolean addTask(Task task) {
        boolean additionSucceeded = false;
        assert task.getIdentifier().equals(Task.DEFAULT_VALUE) : "The tasks list must contain only real tasks, not idles.";

        if (!tasks.contains(task)) {
            additionSucceeded = tasks.add(task);
        }

        return additionSucceeded;
    }

    /**
     * Returns a task with a specified identifier, or null if there is no
     * such a task.
     */
    public Task getTask (Integer identifier) {
        Iterator<Task> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            Task nextTask = iterator.next();
            if (nextTask.getIdentifier().equals(identifier)) {
                return nextTask;
            }
        }

        return null;
    }

    /**
     * Returns a list of processors which termination times are not grater than <code>time</code>
     */
    public List<Integer> getAppropriateProcessors(long time) {
        List<Integer> appropriateProcessors = new ArrayList<Integer>();

        for (Processor processor : processors) {
            if (processor.getEndTime() >= time) {
                appropriateProcessors.add(processor.getIdentifier());
            }
        }

        return appropriateProcessors;
    }

    /**
     * Puts a specified task to a processor which index is <code>processorIndex</code>
     *
     * @param processorIndex processorIndex must be between 0 and <code>getProcessorsNumber()</code>'s value.
     * @param startTime must be greater than or equal to the specified processor's termination time.
     */
    public void addTaskToTheProcessor(int processorIndex, Task task, long startTime) {
        Processor processor = getProcessor(processorIndex);
        long processorTime = getProcessorTime(processorIndex);

        assert (startTime >= processorTime) : "startTime must be  greater than or equal to the specified processor's " +
                "termination time";

        if (startTime > processorTime) {
            // an idle is needed
            Task idle = new Task(Task.DEFAULT_VALUE, startTime-processorTime);
            processor.addTask(processor.getTasksNumber(), idle);
        }

        processor.addTask(processor.getTasksNumber(), task);
    }

    /**
     * Returns the task which processing time is the shortest.
     */
    public Task getTheShortestTask() {
        long recordTime = Long.MAX_VALUE;
        Task recordTask = null;
        for (Task task : tasks) {
            long processingTime = task.getProcessingTime();
            if (processingTime < recordTime) {
                recordTime = processingTime;
                recordTask = task;
            }
        }

        return recordTask;
    }

    /**
     * Returns the task which processing time is the longest.
     */
    public Task getTheLongestTask() {
        long recordTime = Long.MIN_VALUE;
        Task recordTask = null;
        for (Task task : tasks) {
            long processingTime = task.getProcessingTime();
            if (processingTime > recordTime) {
                recordTime = processingTime;
                recordTask = task;
            }
        }

        return recordTask;
    }

    /**
     * Returns a safe iterator over tasks on the specified processor.
     * @param processorIndex The processor's identifier. Must be between 0 and <code>processors.size()</code>
     */
    public Iterator<Task> getProcessorTasksIterator(int processorIndex) {
        assert (processorIndex >= 0 && processorIndex < processors.size()) : "The processor's identifier must be " +
                "between 0 and processors.size()";
        Processor processor = this.getProcessor(processorIndex);
        return processor.iterator();
    }

    /**
     * Returns a safe iterator over processors.
     */
    public Iterator<Processor> getProcessorsIterator() {
        return (new ProcessorsIterator());
    }

    /**
     * Inner class.
     *
     * The class is created to forbid the remove operation.
     */
    private class ProcessorsIterator implements Iterator<Processor> {

        private Iterator<Processor> iterator = null;

        private ProcessorsIterator() {
            iterator = processors.iterator();
        }

        public boolean hasNext() {
            return iterator.hasNext();
        }

        public Processor next() {
            return iterator.next();
        }

        // this implementation doesn't allow removing elements.
        public void remove() {}
    }
}
