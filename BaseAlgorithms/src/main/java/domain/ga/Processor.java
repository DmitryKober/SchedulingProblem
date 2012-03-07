package domain.ga;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: dmitry
 * Date: 27.11.11
 * Time: 18:43
 */
public class Processor {

    private int identifier;        // identifier
    private long endTime;          // indicates the processor's termination time
    private List<Task> tasksList;  // the tasks list
    // optimization issues
    List<Integer> idlesRegister;   // contains indexes of idles on the processor.

    {
        endTime = 0;
        tasksList = new ArrayList<Task>();
        idlesRegister = new ArrayList<Integer>();
    }

    /**
     * Constructor
     */
    Processor(int identifier) {
        this.identifier = identifier;
    }

    public int getIdentifier() {
        return identifier;
    }

    long getEndTime() {
        return endTime;
    }

    int getTasksNumber() {
        return tasksList.size();
    }

    List<Task> getTasksList() {
        return tasksList;
    }

    List<Integer> getIdlesRegister() {
        return idlesRegister;
    }

    int getIdlesNumber() {
        return idlesRegister.size();
    }

    /**
     * Adds the specified idle to the idleRegister.
     *
     * @param idleIndex a position of the idle on the processor.
     * @param task an idle.
     */
    private void addIdleIndex(int idleIndex, Task task) {
        Integer idleValue = new Integer(idleIndex);
        assert (idleIndex >= 0 && idleIndex < tasksList.size()) : "The idleIndex parameter " +
                "must be an integer between 0 and (taskList.size()-1)";

        idlesRegister.add(idleIndex);
    }

    /**
     * Removes the specified idle from the idleRegister.
     */
    private void removeIdleIndex(int position)  {
        Integer idleIndex = new Integer(position);
        assert idlesRegister.contains(idleIndex) : "Nothing to delete from the idleRegister";

        idlesRegister.remove(idleIndex);
    }

    /**
     * Adds a new task to the processor.
     *
     * If the task is not an idle, then it can be removed only from the tail of the list.
     *
     * @param task it could be a real task or an idle.
     * @param position
     */
    void addTask(int position, Task task) {
        if (position != (tasksList.size()) && !task.getIdentifier().equals(Task.DEFAULT_VALUE)) {
            throw new AdditionIndexException(position);
        } else {
            long actBegTime;
            if (position == 0) {
                actBegTime = 0;
            } else if (position == (tasksList.size())) {
                actBegTime = endTime;
            } else {
                Task prevTask = tasksList.get(position-1);
                actBegTime = prevTask.getActBeginningTime() + prevTask.getProcessingTime();
            }

            if (task.getIdentifier().equals(Task.DEFAULT_VALUE)) {
                addIdleIndex(tasksList.size(), task);
                task.setEarlyBeginningTime(actBegTime);
            }

            tasksList.add(task);
            task.setActBeginningTime(actBegTime);
            endTime += task.getProcessingTime();
        }
    }

    /**
     * Removes a task from the specified position.
     *
     * If the task is not an idle, then it can be removed only from the tail of the list.
     */
    void removeTask(int position) throws RemovalIndexException {
        Task taskToRem = tasksList.get(position);
        Integer taskToRemID = taskToRem.getIdentifier();
        if (position != (tasksList.size()-1) && !taskToRemID.equals(Task.DEFAULT_VALUE)) {
            throw new RemovalIndexException(position);
        } else {
            if (taskToRemID.equals(Task.DEFAULT_VALUE)) {
                removeIdleIndex(position);
            } else {
                taskToRem.setActBeginningTime(Task.DEFAULT_VALUE);
            }
            tasksList.remove(position);
            endTime -= taskToRem.getProcessingTime();
        }
    }

    /**
     * Removes the 'tail' of the tasksList, starting with the <b><fromPosition/b>
     * @param fromPosition task's position.
     */
    void removeSubsequentTasks (int fromPosition) {
        assert (fromPosition >= 0 && fromPosition < tasksList.size()) : "The fromPosition parameter must be" +
                "an integer between 0 and (tasksList.size()-1)";

        int tasksToRemove = tasksList.size() - fromPosition;
        for (int i = 0; i < tasksToRemove; i++) {
            removeTask(fromPosition + i);
        }
    }

    /**
     * Clears the tasksList and idlesRegister.
     */
    void removeAllTasks () {
        tasksList.removeAll(tasksList);
        idlesRegister.removeAll(idlesRegister);
        endTime = 0;
    }

    /**
     * Replaces one of the idles with the given task if there is one, which
     * satisfies the specified time bounds.
     * @return <b>true</b> if one of the idles is replaced, otherwise, <b><false/b>
     */
     boolean removeAcceptableIdle (long leftBound, long rightBound, Task task) {
        for (int i = 0; i < idlesRegister.size(); i++) {
            Task idle = tasksList.get(idlesRegister.get(i));
            long idleProcessingTime = idle.getProcessingTime();
            long idleActBeginningTime = idle.getActBeginningTime();
            long idleEndTime = idleActBeginningTime + idleProcessingTime;
            long taskProcessingTime = task.getProcessingTime();

            if (idleActBeginningTime >= leftBound
                    && idleEndTime <= rightBound
                    && idleProcessingTime >= taskProcessingTime) {
                removeTask(i);
                addTask(i, task);
                task.setActBeginningTime(idleActBeginningTime);

                if (taskProcessingTime < idleProcessingTime) {
                    long newIdleActBegTime = idleActBeginningTime + taskProcessingTime;
                    Task newIdle = new Task(Task.DEFAULT_VALUE, idleEndTime - newIdleActBegTime);
                    newIdle.setEarlyBeginningTime(newIdleActBegTime);
                    newIdle.setActBeginningTime(newIdleActBegTime);
                    addTask(i, newIdle);
                }

                return true;
            }
        }

        return false;
    }

    /**
     * Returns a safe iterator over tasks on the processor.
     */
    public Iterator<Task> iterator() {
        return (new ProcessorTasksIterator());
    }

    /**
     * This exception is thrown in case of trying to remove a non-idle task
     * from the position, other than (taskList.size()-1)
     */
    private class RemovalIndexException extends RuntimeException {
        private String message;

        private RemovalIndexException(int position) {
            message = "Trying to remove a non-idle task from the " + position + "th position";
        }

        private String cause() {
            return message;
        }
    }


    /**
     * This exception is thrown in case of trying to add a non-idle task
     * to the position, other than (taskList.size()-1)
     */
    private class AdditionIndexException extends RuntimeException {
        private String message;

        private AdditionIndexException(int position) {
            message = "Trying to add a non-idle task to the " + position + "th position";
        }

        private String cause() {
            return message;
        }
    }

    /**
     * Inner class
     *
     * This class is created to forbid the remove operation.
     */
    private class ProcessorTasksIterator implements Iterator<Task> {

        private Iterator<Task> iterator;

        private ProcessorTasksIterator() {
            iterator = tasksList.iterator();
        }

        public boolean hasNext() {
            return iterator.hasNext();
        }

        public Task next() {
            return iterator.next();
        }

        // this implementation doesn't allow removing elements.
        public void remove() {}
    }


}
