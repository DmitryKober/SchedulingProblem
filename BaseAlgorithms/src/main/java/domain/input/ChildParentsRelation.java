package domain.input;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: dmitry
 * Date: 02.01.12
 * Time: 15:52
 */
public class ChildParentsRelation implements Serializable {

    private Integer child;            // child node 's index
    private List<Integer> parents; // tasks that depend on the child

    /**
     * Constructor
     */
    public ChildParentsRelation(Integer child) {
        assert child != null : "A child must not be null.";
        this.child = child;
        parents = new ArrayList<Integer>();
    }

    public Integer getChild() {
        return child;
    }

    public int getParentsNumber() {
        return parents.size();
    }

    public List<Integer> getParents() {
        return parents;
    }

    /**
     * Adds a new node to a parents list.
     *
     * @param parent Must be unique throughout the parents list. Must not be equal to the child.
     */
    public void addParent(Integer parent) {
        assert !parents.contains(parent) : "Duplicate parent.";
        assert !parent.equals(child) : "The partial order graph must not contain loops.";

        parents.add(parent);
    }

    /**
     * Returns an iterator for the parents list.
     */
    public Iterator<Integer> iterator() {
        return parents.iterator();
    }
}
