package domain.input;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: dmitry
 * Date: 04.12.11
 * Time: 17:57
 */
public class GraphRepresentation implements Serializable {

    private List<ParentChildrenRelation> parentChildrenRelations;
    private List<ChildParentsRelation> childParentsRelations;

    /**
     * Constructor
     *
     */
    public GraphRepresentation() {
        parentChildrenRelations = new ArrayList<ParentChildrenRelation>();
        childParentsRelations = new ArrayList<ChildParentsRelation>();
    }

    public List<ParentChildrenRelation> getParentChildrenRelations() {
        return parentChildrenRelations;
    }

    public List<ChildParentsRelation> getChildParentsRelations() {
        return childParentsRelations;
    }

    /**
     * Returns descendants of a specified parent.
     */
    public List<Integer> getDescendants(Integer parentIndex) {
        return parentChildrenRelations.get(parentIndex).getDescendants();
    }

    public List<Integer> getParents(Integer childIndex) {
        return childParentsRelations.get(childIndex).getParents();
    }

    /**
     * Returns a list of tasks that are initially independent.
     */
    public List<Integer> getInitiallyIndependentTasks() {
        List<Integer> independentTasks = new ArrayList<Integer>();

        Iterator<ChildParentsRelation> iterator = childParentsRelations.iterator();
        while (iterator.hasNext()) {
            ChildParentsRelation nextNode = iterator.next();
            if (nextNode.getParentsNumber() == 0) {
                independentTasks.add(nextNode.getChild());
            }
        }

        return independentTasks;
    }

}
