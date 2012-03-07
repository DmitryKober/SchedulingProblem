package domain.input;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: dmitry
 * Date: 26.12.11
 * Time: 0:44
 */

/**
 * Represents the partial order between tasks
 */
public class ParentChildrenRelation implements Serializable {

    private Integer parent;            // parent node 's index
    private List<Integer> descendants; // tasks that depend on the parent

    /**
     * Constructor
     */
    public ParentChildrenRelation(Integer parent) {
        assert parent != null : "A parent must not be null.";
        this.parent = parent;
        descendants = new ArrayList<Integer>();
    }

    public Integer getParent() {
        return parent;
    }

    public int getDescendantsNumber() {
        return descendants.size();
    }

    public List<Integer> getDescendants() {
        return descendants;
    }

    /**
     * Adds a new descendant to a descendants list.
     *
     * @param descendant Must be unique throughout the descendants list. Must not be equal to the parent.
     */
    public void addDescendant(Integer descendant) {
        assert !descendants.contains(descendant) : "Duplicate descendant.";
        assert !descendant.equals(parent) : "The partial order graph must not contain loops.";

        descendants.add(descendant);
    }

    /**
     * Returns an iterator for the descendants list.
     */
    public Iterator<Integer> iterator() {
        return descendants.iterator();
    }
}
