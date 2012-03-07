package services.factory;

import dao.GraphDAO;
import domain.input.GraphRepresentation;
import services.clone.CloneService;

/**
 * User: dmitry
 * Date: 25.12.11
 * Time: 16:42
 */
public class GraphRepresentationFactory {

    //--------------------------- VARIABLES --------------------------------------------
    private GraphRepresentation baseGraph;
    private GraphDAO graphDAO;

    public void setGraphDAO (GraphDAO graphDAO) {
        this.graphDAO = graphDAO;
    }
    //----------------------------------------------------------------------------------

    /**
     * Returns the base graph.
     * Must not be modified!
     */
    public GraphRepresentation getBaseGraph() {
        if (baseGraph == null) {
            baseGraph = graphDAO.getGraph();
        }
        return baseGraph;
    }

    /**
     * Clones the base graph that was build by the GraphRepresentationBuilder.
     */
    public GraphRepresentation getGraphClone() {
        // make a clone of graph
        GraphRepresentation clone = (GraphRepresentation) CloneService.clone(getBaseGraph());

        return clone;
    }
}
