package domain.ga;

import services.clone.CloneService;

import java.util.*;

/**
 * User: dmitry
 * Date: 04.12.11
 * Time: 21:12
 */
public class TasksPool {

    private List<Integer> tasksPool;

    /**
     * Constructor.
     */
    public TasksPool() {
        tasksPool = new ArrayList<Integer>();
    }

    /**
     * Adds the specified task to the tasks set
     * if there is not such a task already
     * @param task the task to add
     * @return <b>true</b> if the task was added successfully, <b>false</b> otherwise
     */
    public boolean push(Integer task) {
        if (!tasksPool.contains(task)) {
            tasksPool.add(task);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Add a bunch of task to the pool.
     * @return <b>true</b> if the collection of tasks was added successfully, <b>false</b> otherwise
     */
    public boolean addCollection(Collection<Integer> collection) {
        Iterator<Integer> iterator = collection.iterator();
        boolean resultStatus = true;
        while (iterator.hasNext()) {
            resultStatus = tasksPool.contains(iterator.next());
            if (resultStatus) {
                return false;
            }
        }

        tasksPool.addAll(collection);
        return true;
    }

    /**
     * Returns the next ready task (the pool's head) if the pool is not empty,
     * otherwise null.
     */
    public Integer getHead() {
        if (!tasksPool.isEmpty()) {
            return tasksPool.get(0);
        }

        return null;
    }

    /**
     * Pops a specified task from the pool
     * @return true if the removal was successful.
     */
    public boolean pop(Integer identifier) {
        assert tasksPool.contains(identifier) : "There is no such a task in the tasks pool";
        return tasksPool.remove(identifier);
    }

    /**
     * Pops the first task from the pool
     * @param shake if true the ready tasks list will be shuffled before popping the next task.
     * @return the removed task's identifier
     */
    public Integer popNext(boolean shake) {
        assert tasksPool.size() > 0 : "To delete a task from the pool it must contain at least one task";
        if (shake) {
            Collections.shuffle(tasksPool);
        }
        return tasksPool.remove(0);
    }

    /**
     * Determines if the pool contains a task with the specified identifier
     * @return <b>true</b> if there is one, <b>false</b> otherwise
     */
    public boolean contains(Integer identifier) {
        return tasksPool.contains(identifier);
    }

    /**
     * Determines if the tasks set is empty
     */
    public boolean isEmpty() {
        return tasksPool.isEmpty();
    }

    /**
     *  Returns the iterator.
     */
    public Iterator<Integer> iterator() {
        return tasksPool.iterator();
    }

    /**
     * Indicates whether the pool contains elements.
     */
    public boolean hasNext() {
        return !tasksPool.isEmpty();
    }

    /**
     * Returns the amount of ready tasks.
     */
    public int size() {
        return tasksPool.size();
    }

    /**
     * Returns a clone of the pool.
     */
    public List<Integer> clone() {
        return (List<Integer>) CloneService.clone(tasksPool);
    }
}
