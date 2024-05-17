package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.model.*;
import org.insa.graphs.model.Point;
public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
        
    }
    public Label initLabel(int nodeId,int pere ,boolean marked,double coutRealise){
        ShortestPathData data =getInputData();
        Node succesorNode=graph.get(nodeId);
        double heuristique;
        if (data.getMode()==Mode.LENGTH){
            heuristique=Point.distance(succesorNode.getPoint(),data.getDestination().getPoint());//heuristique = diffèrence entre le point de départ et le point de destination 
        }else {
            heuristique= succesorNode.getPoint().distanceTo(data.getDestination().getPoint())/data.getMaximumSpeed();
        }
        return new LabelStar(nodeId, pere, marked, coutRealise, heuristique);
        
    }

}
