package dao;

import domain.input.ChildParentsRelation;
import domain.input.GraphRepresentation;
import domain.input.ParentChildrenRelation;

import java.io.*;
import java.util.List;

/**
 * User: dmitry
 * Date: 26.12.11
 * Time: 0:35
 */
public class GraphDAOImpl implements GraphDAO {

    private String fileLocation = null; // adjacency matrix file path
    private File aFile = null;

    /**
     * Constructor
     */
	public GraphDAOImpl(String fileLocation) {
		this.fileLocation = fileLocation;
        this.aFile = new File(fileLocation);
	}

    /**
     * Returns a graph of a partial order.
     */
    public GraphRepresentation getGraph() {
        GraphRepresentation graph = new GraphRepresentation();
        List<ChildParentsRelation> childParentsRelations = graph.getChildParentsRelations();
        List<ParentChildrenRelation> parentChildrenRelations = graph.getParentChildrenRelations();

        BufferedReader input = openFile();

        String nextLine = null;
        try {
            nextLine = input.readLine();
            boolean isFirstRow = true;
            int lineNumber = 0;
            while (nextLine != null) {
                String[] dataItems = nextLine.split(",");

                // creates empty child/parents relations
                if (isFirstRow) {
                    isFirstRow = false;
                    for (int i = 0; i < dataItems.length; i++) {
                        childParentsRelations.add(new ChildParentsRelation(i));
                    }
                }

                // building child/parents and parent/children relations
                ParentChildrenRelation parentChildrenRelation = new ParentChildrenRelation(lineNumber);
                for (int i = 0; i < dataItems.length; i++) {
                    Integer nextInt = Integer.parseInt(dataItems[i]);
                    if (nextInt == 1) {
                        childParentsRelations.get(i).addParent(lineNumber);
                        parentChildrenRelation.addDescendant(i);
                    }
                }
                parentChildrenRelations.add(parentChildrenRelation);

                lineNumber++;
                nextLine = input.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        closeFile(input);
        return graph;
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
