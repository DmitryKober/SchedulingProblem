package services.algorithms.initial;

import domain.ga.Individual;
import domain.ga.TasksPool;
import services.scheduler.ReadyTasksScheduler;
import services.scheduler.Scheduler;

import java.io.Serializable;

/**
 * User: dmitry
 * Date: 04.03.12
 * Time: 22:03
 */
public class IndividualWrapper implements Serializable {

    private Individual individual;
    private TasksPool readyTasksSnapshot;
    private int currentReadyTask;
    private Scheduler scheduler;

    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

    public TasksPool getReadyTasksSnapshot() {
        if (readyTasksSnapshot == null) {
            readyTasksSnapshot = new TasksPool();
        }

        return readyTasksSnapshot;
    }

    public void setReadyTasksSnapshot(TasksPool readyTasksSnapshot) {
        this.readyTasksSnapshot = readyTasksSnapshot;
    }

    public int getCurrentReadyTask() {
        return currentReadyTask;
    }

    public void setCurrentReadyTask(int currentReadyTask) {
        this.currentReadyTask = currentReadyTask;
    }

    public Scheduler getScheduler() {
        if (scheduler == null) {
            // default scheduler
            scheduler = new ReadyTasksScheduler();
        }
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }
}
