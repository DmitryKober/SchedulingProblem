package domain.input;

/**
 * User: dmitry
 * Date: 16.12.11
 * Time: 0:45
 */

import dao.TasksDAO;
import domain.ga.Task;
import org.springframework.beans.factory.annotation.Autowired;
import services.clone.CloneService;
import services.factory.GraphRepresentationFactory;

import java.util.Iterator;
import java.util.List;

/**
 * This class holds all tasks which were received from the input
 *
 * The nature of this class is Factory which creates clones of tasks for each new individual
 *
 */

public class TasksHolder {

    //--------------------------- VARIABLES --------------------------------------------
    @Autowired
    private GraphRepresentationFactory graphRepresentationFactory;
    private List<Task> tasks;
    private TasksDAO tasksDAO;

    public void setTasksDAO(TasksDAO tasksDAO) {
        this.tasksDAO = tasksDAO;
    }
    //----------------------------------------------------------------------------------

    public void setInitialTasks() {
        if (tasks == null) {
            tasks = tasksDAO.getTasks();
            setParentBounds();
        }
    }

    /**
     * Evaluates the initial parent bound for every task in the system.
     */
    private void setParentBounds() {
        GraphRepresentation baseGraph = graphRepresentationFactory.getBaseGraph();

        Iterator<Task> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            Task nextTask = iterator.next();
            List<Integer> nextTaskParents = baseGraph.getParents(nextTask.getIdentifier());
            nextTask.setParentBound(nextTaskParents.size());
        }
    }

    /**
     * Returns a task with a specified identifier, or null if there is no
     * such a task.
     */
    private Task getTask(Integer identifier) {
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
     * Checks whether there is already such a task
     */
    private boolean containsTask(Task task) {
        Iterator<Task> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            Task nextTask = iterator.next();
            if (nextTask.getIdentifier().equals(task.getIdentifier())) {
                return true;
            }
        }

        return true;
    }

    /**
     * Returns a new set of tasks - clones of the existing prototypes.
     *
     * This method should be called at the initialization phase of an each individual.
     */
    public List<Task> getTasksClones() {
        return (List<Task>) CloneService.clone(tasks);
    }


}
