package dao;

import domain.ga.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: dmitry
 * Date: 02.01.12
 * Time: 21:06
 */
public class TasksDAOImpl implements TasksDAO {

    private String fileLocation = null;
    private File aFile = null;

    /**
     * Constructor
     */
	public TasksDAOImpl(String fileLocation) {
		this.fileLocation = fileLocation;
        this.aFile = new File(fileLocation);
	}


    /**
     * Returns a set of Task objects
     */
    public List<Task> getTasks() {
        List<Task> resultTasksSet = new ArrayList<Task>();
        BufferedReader input = openFile();

        String nextLine = null;
        try {
            nextLine = input.readLine();
            while (nextLine != null) {
                String[] dataItems = nextLine.split(",");
                Integer identifier = Integer.parseInt(dataItems[0]);
                Long processingTime = Long.parseLong(dataItems[1]);
                Task newTask = new Task(identifier, processingTime);
                resultTasksSet.add(newTask);

                nextLine = input.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        closeFile(input);
        return resultTasksSet;
    }

    /**
     * Prepares the file aFile to read from.
     */
    private BufferedReader openFile() {
        BufferedReader input = null;

        try {
            input = new BufferedReader(new FileReader(aFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return input;
    }

    /**
     * Closes the stream,
     */
    private void closeFile(BufferedReader br) {
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
