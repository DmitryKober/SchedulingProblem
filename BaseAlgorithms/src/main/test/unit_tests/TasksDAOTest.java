package unit_tests;

import dao.TasksDAO;
import domain.ga.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Iterator;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * User: dmitry
 * Date: 02.01.12
 * Time: 21:19
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/src/main/resources/TestContext.xml")
public class TasksDAOTest {

    private TasksDAO tasksDAO;

    public void setTasksDAO(TasksDAO tasksDAO) {
        this.tasksDAO = tasksDAO;
    }

    @Test
    public void getTasks() {
        List<Task> tasks = tasksDAO.getTasks();
        assertEquals(5, tasks.size());

        Iterator<Task> iterator = tasks.iterator();
        Task zerothTask = iterator.next();
        assertEquals(new Integer(0), zerothTask.getIdentifier());
        assertEquals(100, zerothTask.getProcessingTime());

        Task firstTask = iterator.next();
        assertEquals(new Integer(1), firstTask.getIdentifier());
        assertEquals(150, firstTask.getProcessingTime());

        Task secondTask = iterator.next();
        assertEquals(new Integer(2), secondTask.getIdentifier());
        assertEquals(100, secondTask.getProcessingTime());

        Task thirdTask = iterator.next();
        assertEquals(new Integer(3), thirdTask.getIdentifier());
        assertEquals(100, thirdTask.getProcessingTime());

        Task forthTask = iterator.next();
        assertEquals(new Integer(4), forthTask.getIdentifier());
        assertEquals(100, forthTask.getProcessingTime());
    }
}
