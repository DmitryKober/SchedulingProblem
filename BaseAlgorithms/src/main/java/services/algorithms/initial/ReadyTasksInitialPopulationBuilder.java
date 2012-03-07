package services.algorithms.initial;

import dao.GraphDAO;
import domain.ga.Individual;
import domain.ga.TasksPool;
import domain.input.GraphRepresentation;
import domain.input.Requirements;
import org.springframework.beans.factory.annotation.Autowired;
import services.clone.CloneService;
import services.scheduler.ReadyTasksScheduler;
import services.scheduler.Scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * User: dmitry
 * Date: 12.02.12
 * Time: 19:49
 */
public class ReadyTasksInitialPopulationBuilder implements InitialPopulationBuilder {

    @Autowired
    private GraphDAO graphDAO;
    private List<IndividualWrapper> parentBuffer;
    private List<IndividualWrapper> childrenBuffer;
    private List<IndividualWrapper> tempBuffer;
    private int nextChildIndividualID;
    private Random random;

    /**
     * Constructor.
     */
    public ReadyTasksInitialPopulationBuilder() {
        parentBuffer = new ArrayList<IndividualWrapper>();
        childrenBuffer = new ArrayList<IndividualWrapper>();
        nextChildIndividualID = 0;
        random = new Random(System.currentTimeMillis());
    }

    /**
     * Builds an initial population according to specified requirements.
     */
    public void build(Requirements requirements) {
        // individuals' processors amount
        final int individualsProcessorsNumber = requirements.getProcessorsAmount();

        // required population size
        int requiredPopulationSize = requirements.getInitialPopulationSize();
        int currentPopulationSize = 0;

        GraphRepresentation graph = graphDAO.getGraph();

        // fill the first buffer with individuals build from the initially ready tasks
        // TODO: change this! Initially independent tasks are evaluated while the individuals creation
        List<Integer> initiallyIndependentTasks = graph.getInitiallyIndependentTasks();
        for (int i = 0; i < initiallyIndependentTasks.size(); i++) {
            if (currentPopulationSize < requiredPopulationSize) {
                // wrapper creation
                IndividualWrapper nextIndividualWrapper = new IndividualWrapper();

                // set individual
                Individual nextIndividual = new Individual(nextChildIndividualID++, individualsProcessorsNumber);
                nextIndividualWrapper.setIndividual(nextIndividual);

                // set scheduler
                Scheduler scheduler = new ReadyTasksScheduler();
                nextIndividualWrapper.setScheduler(scheduler);

                // set ready tasks snapshot
                TasksPool readyTasksPool = scheduler.getReadyTasksPool();
                nextIndividualWrapper.setReadyTasksSnapshot(readyTasksPool);

                // set current ready task index (the next task which must be processed to create a new individual)
                nextIndividualWrapper.setCurrentReadyTask(0);

                // add the wrapper to the first buffer
                parentBuffer.add(nextIndividualWrapper);

                currentPopulationSize++;
            }
        }

        // TODO : refactoring is needed: extract the method
        // fill the second buffer with the individuals build from the ones from the first buffer
        // by proceeding a one ready task
        for (int i = 0; i < parentBuffer.size(); i++) {
            IndividualWrapper parentWrapper = parentBuffer.get(i);
            TasksPool readyTasksSnapshot = parentWrapper.getReadyTasksSnapshot();
            int tasksPoolSize = readyTasksSnapshot.size();

            Integer currentReadyTask = parentWrapper.getCurrentReadyTask();
            if (!(currentReadyTask == tasksPoolSize-1)) {
                parentWrapper.setCurrentReadyTask(currentReadyTask++);
                IndividualWrapper childWrapper = (IndividualWrapper) CloneService.clone(parentWrapper);
                childWrapper.getScheduler().performCustomIteration(currentReadyTask);
                childWrapper.getReadyTasksSnapshot().pop(currentReadyTask);
                childWrapper.setCurrentReadyTask(0);
                childrenBuffer.add(childWrapper);
            } else { // there is no more different variants to build individuals.
                parentBuffer.remove(parentWrapper);
            }
        }

        // while the child individuals number is lower than required
        // randomly choose a parent individual (if it presents in the parent buffer then in can produce new children)
        // build a new child based on it.
        while (currentPopulationSize < requiredPopulationSize) {
            int parentIndividualIndex = random.nextInt(parentBuffer.size());
            IndividualWrapper parentIndividual = parentBuffer.get(parentIndividualIndex);
        }

    }

    /**
     * Swaps the specified buffers.
     */
    private void swapBuffers(List<IndividualWrapper> firstBuffer, List<IndividualWrapper> secondBuffer) {
        tempBuffer = firstBuffer;
        firstBuffer = secondBuffer;
        secondBuffer = tempBuffer;
    }

}
