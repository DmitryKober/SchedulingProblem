package services.scheduler;

import annotations.LogAction;
import annotations.ProfileAction;
import domain.ga.Task;
import services.history.HistoryProcessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: dmitry
 * Date: 17.12.11
 * Time: 3:09
 */
public class ReadyTasksScheduler extends AbstractReadyTasksScheduler {

    /**
     * Constructor
     */
    public ReadyTasksScheduler() {
        super();
    }

    /**
     * Tha main Schedule method.
     */
    @LogAction(comments="A schedule is building at one go")
    @ProfileAction
    public void schedule() {
        assert getIndividual() != null : "Can't build a schedule on the null individual";

        while (readyTasksPool.hasNext()) {
            performNextIteration();
        }
    }

    // TODO: Performance issue: may be proved be returning the actual ready tasks list not the clone.
    /**
     * Performs an iteration of the scheduling algorithm.
     * Operates with the next task from the ready tasks pool.
     *
     */
    @LogAction(comments="performing a schedule building iteration: operates with the next ready task")
    public List<Integer> performNextIteration() {
        assert getIndividual() != null : "Can't build a schedule on the null individual";

        // get a next ready task (shake mode is off. See the TasksPool class)
        Task task = schedule.getTask(readyTasksPool.popNext(false));

        // choose the processor the next task will be placed on
        int processorIndex = chooseProcessorForTask(task);

        // place that task to the processor
        schedule.addTaskToTheProcessor(processorIndex, task, task.getEarlyBeginningTime());

        // evaluate the parent count
        List<Integer> descendantTasks = evaluateDescendantsParentCount(task,
                schedule.getProcessorTime(processorIndex));

        // writing a history
        HistoryProcessor.addEvent(
                individual.getHistory(),
                task.getIdentifier(),
                processorIndex,
                readyTasksPool.clone(),
                descendantTasks);

        return readyTasksPool.clone();
    }

    /**
     * Performs an iteration of the scheduling algorithm.
     * Operates with the specified task from the ready tasks pool.
     *
     * @param taskID the identifier of the task to be proceeded.
     *
     */
    @LogAction(comments="performing a schedule building iteration: operates with the specified task")
    public void performCustomIteration(Integer taskID) {

        assert getIndividual() != null : "Can't build a schedule on the null individual";
        assert taskID != null && readyTasksPool.contains(taskID): "The taskID identifier must present " +
                "in the ready tasks pool";

        // get a next ready task
        readyTasksPool.pop(taskID);
        Task task = schedule.getTask(taskID);

        // choose the processor the next task will be placed on
        int processorIndex = chooseProcessorForTask(task);

        // place that task to the processor
        schedule.addTaskToTheProcessor(processorIndex, task, task.getEarlyBeginningTime());

        // evaluate the parent count
        List<Integer> descendantTasks = evaluateDescendantsParentCount(task,
                schedule.getProcessorTime(processorIndex));

        // writing a history
        HistoryProcessor.addEvent(
                individual.getHistory(),
                task.getIdentifier(),
                processorIndex,
                readyTasksPool.clone(),
                descendantTasks);
    }

    /**
     * Actualizes the parent bound for descendants of the specified task.
     * @param task The task that is scheduled.
     * @param earlyBegTime The processor's termination time the specified task is placed at.
     * @return a list of tasks' identifiers which parentBound is changed.
     */
    private List<Integer> evaluateDescendantsParentCount(Task task, long earlyBegTime) {
        List<Integer> descendantIndexes = graphRepresentation.getDescendants(task.getIdentifier());
        for (Integer descendantIndex : descendantIndexes) {
            Task descendant = schedule.getTask(descendantIndex);
            int parentBound = descendant.getParentBound();
            if (parentBound == 0) {
                continue;
            } else if (parentBound == 1) {
                descendant.setParentBound(0);
                descendant.setEarlyBeginningTime(earlyBegTime);
                readyTasksPool.push(descendantIndex);
            } else {
                descendant.setParentBound(parentBound-1);
            }
        }

        return descendantIndexes;
    }

    /**
     * Returns a number of a processor the task should be processed on.
     */
    private int chooseProcessorForTask(Task task) {
        long earlyBeginningTime = task.getEarlyBeginningTime();
        List<Integer> appropriateProcessors = schedule.getAppropriateProcessors(earlyBeginningTime);

        // get an idle time of each processor
        // for the task
        List<IdleTime> idleTimes = new ArrayList<IdleTime>();
        for (Integer nextProcessor : appropriateProcessors) {
            long processorEndTime = schedule.getProcessorTime(nextProcessor);
            long idleTime = earlyBeginningTime - processorEndTime;
            idleTimes.add(new IdleTime(nextProcessor, Math.abs(idleTime)));
        }

        Collections.sort(idleTimes, new IdleTimeComparator());
        int processorWithMinimalIdle = idleTimes.get(0).getProcessorIndex();

        return processorWithMinimalIdle;
    }

}
