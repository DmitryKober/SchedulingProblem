package unit_tests;

import dao.GraphDAO;
import domain.input.ChildParentsRelation;
import domain.input.GraphRepresentation;
import domain.input.ParentChildrenRelation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * User: dmitry
 * Date: 26.12.11
 * Time: 2:04
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= "/src/main/resources/TestContext.xml")
public class GraphDAOTest {
    @Autowired
    private GraphDAO graphDAO;

    @Test
    public void testGetGraph() {
        GraphRepresentation graph = graphDAO.getGraph();

        // child/parents relations
        List<ChildParentsRelation> childParentsRelations = graph.getChildParentsRelations();
        // zeroth child
        ChildParentsRelation zerothChild = childParentsRelations.get(0);
        assertEquals(0, zerothChild.getParentsNumber());
        // first child
        ChildParentsRelation firstChild = childParentsRelations.get(1);
        assertEquals(0, firstChild.getParentsNumber());
        // second child
        ChildParentsRelation secondChild = childParentsRelations.get(2);
        assertEquals(2, secondChild.getParentsNumber());
        Iterator<Integer> secondChildParentsIterator = secondChild.iterator();
        assertEquals(new Integer(0), secondChildParentsIterator.next());
        assertEquals(new Integer(1), secondChildParentsIterator.next());
        // third child
        ChildParentsRelation thirdChild = childParentsRelations.get(3);
        assertEquals(1, thirdChild.getParentsNumber());
        Iterator<Integer> thirdChildParentsIterator = thirdChild.iterator();
        assertEquals(new Integer(0), thirdChildParentsIterator.next());
        // forth child
        ChildParentsRelation forthChild = childParentsRelations.get(4);
        assertEquals(2, forthChild.getParentsNumber());
        Iterator<Integer> forthChildParentsIterator = forthChild.iterator();
        assertEquals(new Integer(2), forthChildParentsIterator.next());
        assertEquals(new Integer(3), forthChildParentsIterator.next());

        // parent/children relations
        List<ParentChildrenRelation> parentChildrenRelations = graph.getParentChildrenRelations();
        // zeroth parent
        ParentChildrenRelation zerothParent = parentChildrenRelations.get(0);
        assertEquals(2, zerothParent.getDescendantsNumber());
        Iterator<Integer> zerothParentChildrenIterator = zerothParent.iterator();
        assertEquals(2, zerothParentChildrenIterator.next().intValue());
        assertEquals(3, zerothParentChildrenIterator.next().intValue());
        // first parent
        ParentChildrenRelation firstParent = parentChildrenRelations.get(1);
        assertEquals(1, firstParent.getDescendantsNumber());
        Iterator<Integer> firstParentChildrenIterator = firstParent.iterator();
        assertEquals(2, firstParentChildrenIterator.next().intValue());
        // second parent
        ParentChildrenRelation secondParent = parentChildrenRelations.get(2);
        assertEquals(1, secondParent.getDescendantsNumber());
        Iterator<Integer> secondParentChildrenIterator = secondParent.iterator();
        assertEquals(4, secondParentChildrenIterator.next().intValue());
        // third parent
        ParentChildrenRelation thirdParent = parentChildrenRelations.get(3);
        assertEquals(1, thirdParent.getDescendantsNumber());
        Iterator<Integer> thirdParentChildrenIterator = thirdParent.iterator();
        assertEquals(4, thirdParentChildrenIterator.next().intValue());
        //forth parent
        ParentChildrenRelation forthParent = parentChildrenRelations.get(4);
        assertEquals(0, forthParent.getDescendantsNumber());
    }
}
