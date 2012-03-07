package services.scheduler;

import domain.ga.Individual;
import domain.ga.Schedule;
import domain.ga.TasksPool;
import domain.input.GraphRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import services.factory.GraphRepresentationFactory;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * User: DKober
 * Date: 24.12.11
 * Time: 14:02
 */
public abstract class AbstractReadyTasksScheduler implements Scheduler {

    protected TasksPool readyTasksPool;                // ready tasks
    protected GraphRepresentation graphRepresentation; // a partial order
    protected Schedule schedule;
    protected Individual individual;
    @Autowired
    private GraphRepresentationFactory graphRepresentationFactory;

    /**
     * Constructor
     */
    public AbstractReadyTasksScheduler() {
        this.schedule = individual.getSchedule();
    }

    @PostConstruct
    protected void init() {
        graphRepresentation = graphRepresentationFactory.getGraphClone();
        // identify tasks which are initially ready
        List<Integer> initiallyIndependentTasks = graphRepresentation.getInitiallyIndependentTasks();
        // add them to the ready tasks pool
        readyTasksPool.addCollection(initiallyIndependentTasks);
    }

    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

    public TasksPool getReadyTasksPool() {
        return readyTasksPool;
    }

    public void setReadyTasksPool(TasksPool readyTasksPool) {
        this.readyTasksPool = readyTasksPool;
    }
}
