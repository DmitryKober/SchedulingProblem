package dao;

import domain.ga.Task;

import java.util.List;

/**
 * User: dmitry
 * Date: 16.12.11
 * Time: 3:35
 */
public interface TasksDAO {

    public List<Task> getTasks();

}
