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
        double cout_total;
        if (data.getMode()==Mode.LENGTH){ //si on est basé dans la distance géographique 
            cout_total=Point.distance(succesorNode.getPoint(),data.getDestination().getPoint());//cout_total = diffèrence entre le point de départ et le point de destination 
                //on appelle même la distance euclidienne 
        }else{
            /*si c'est le temps qui nous interesse on divise cette longeur par la vitesse formule simple temps = longeur/vitesse  */
            cout_total= succesorNode.getPoint().distanceTo(data.getDestination().getPoint())/data.getMaximumSpeed();
        }
        return new LabelStar(nodeId, pere, marked, coutRealise, cout_total);
        
    }

}
