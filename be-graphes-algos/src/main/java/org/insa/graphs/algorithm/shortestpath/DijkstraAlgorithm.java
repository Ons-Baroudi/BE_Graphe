package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;


import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.algorithm.utils.ElementNotFoundException;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import java.util.HashMap;


public class DijkstraAlgorithm extends ShortestPathAlgorithm {


    Graph graph;/* graph sur lequel l'algorithme sera executé */
    BinaryHeap<Label> heap; /* binary heap permettant le tri des labels dans l'ordre croissant des couts de label */
                            //nous aide à trouver the shortest path 
    HashMap<Integer, Label> LabelMap; /* hashmap permettant d'associer l'id du noeud a son label */

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
        graph = data.getGraph();
        heap = new BinaryHeap<Label>();
        LabelMap = new HashMap<Integer, Label>(); 
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();

        /*initialisation en insérant l'origin dans la hashmap et dans le heap */
        int origin = data.getOrigin().getId();
        Label labelOrigin = initLabel(origin,-1,false, 0);
        LabelMap.put(origin, labelOrigin);   
        heap.insert(labelOrigin);   
        notifyOriginProcessed(graph.get(origin));  

        /* boucle permettant djikstra de s'éxecuter les deux conditions d'arrêt sont le tas est vide et tous les noeud sont marqués  */
        while(heap.size()!=0)
        {
            /* on vérifie si le label destination est dans la hashmap pour par la suite vérifier si il est marquer, si il l'est on arrête l'algo */
            if(LabelMap.containsKey(data.getDestination().getId()))
            {
                if(LabelMap.get(data.getDestination().getId()).getMarque())
                {
                    break;
                }
            }
            
            Label selectedNode = heap.findMin();/* séléction du sommet à marqué le cout min ou le premier du tas  */
            System.out.println("le côut du label courant1 "+selectedNode.getRealCost());
            System.out.println("le côut du label courant1 "+selectedNode .getTotalCost());
            heap.deleteMin();
            selectedNode.setMarque(true);
            notifyNodeMarked(graph.get(selectedNode.getorigine()));

            /*on parcourt tout les arcs sortants du sommet que l'on vient de sélectionner */
            for(Arc successor : graph.get(selectedNode.getorigine()).getSuccessors())
            {
                //on vérifie si l'arc est accssessible par voiture ou non ? 
                if(data.isAllowed(successor))
                {      
                    Node succesorNode = successor.getDestination();
                    Label successorLabel;

                    /* on vérifie si le noeud de destination de l'arc est dans la hashmap, si oui on le récupère, si non on le créer et l'ajout dans la hashmap*/
                    if(!LabelMap.containsKey(succesorNode.getId()))
                    {
                        successorLabel = initLabel(succesorNode.getId(),-1,false, Double.POSITIVE_INFINITY);
                        LabelMap.put(succesorNode.getId(), successorLabel);
                    }
                    else
                    {
                        successorLabel=LabelMap.get(succesorNode.getId());
                    }
                
                    /*si le noeud est déja marqué on passe on traite pas (spécifité de l'algorithme Dijkstra) */
                    if(!successorLabel.getMarque())
                    {/*on additionne le cout présent avec le cout du noeud séléctionné */
                        double tempCost = data.getCost(successor)+selectedNode.getRealCost();

                        /* on vérifie si il améliore ou non le cout actuel du noeud */
                        if(tempCost<successorLabel.getRealCost())
                        {
                            if(successorLabel.getRealCost()!=Double.POSITIVE_INFINITY)
                            {// on efface le sommet du tas 

                                try{heap.remove(successorLabel);
                                }catch(ElementNotFoundException element){}
                            //System.out.println("le côut du label courant "+successorLabel.getRealCost());
                            //System.out.println("le côut du label courant "+successorLabel.getTotalCost());
                            }
                            else
                            
                            {
                                notifyNodeReached(succesorNode);
                            }
                            /* on met à jour avec le nouveau cout et nouveau père */
                            successorLabel.setCost(tempCost);
                            successorLabel.setPere(selectedNode.getorigine());

                            
                            heap.insert(successorLabel);
                        }
                    }
                }
            }
        }
        notifyDestinationReached(data.getDestination());
        return getPathFromSPA(graph, LabelMap, data);
    }
    public Label initLabel(int nodeId, int fatherNode, boolean marque, double coutRealise)
    {
        return new Label(nodeId, fatherNode, marque, coutRealise);
    } 

    /* fonction permettant de récupérer le path à partir du Map obtenu  */
    public ShortestPathSolution getPathFromSPA(Graph graph,HashMap<Integer, Label> LabelMap, ShortestPathData data )
    {
        ShortestPathSolution solution = null;

        ArrayList<Arc> path_arc = new ArrayList<Arc>(); 
        int currentNode=data.getDestination().getId();

        /* on teste si la destination est dans la Map, si se trouve pas donc le chemin est inaccessible */
        if(!LabelMap.containsKey(currentNode))
        {
            return new ShortestPathSolution(data,Status.INFEASIBLE);
        }
        else
        {
            while(true)
            {
                Label currentLabel=LabelMap.get(currentNode);
                int Node_marked = currentLabel.getPere();
                if(Node_marked==-1)
                {
                    break;
                }
                Label dadLabel = LabelMap.get(Node_marked);

                for(Arc arc : graph.get(Node_marked).getSuccessors())
                {
                    if(arc.getDestination().getId()==currentNode && arc.getOrigin().getId()==Node_marked && Math.abs(data.getCost(arc)-(currentLabel.getRealCost()-dadLabel.getRealCost())) <= 0.01)
                    {
                        path_arc.add(arc);
                        break;
                    }
                }
                currentNode=Node_marked;
            }
            //le cas q'un chemin n'existe pas ou un seul arc existe 


            //si le size==0  le noeud d'origine est bien le seul chemin  
            if(path_arc.size()==0)
            {   
                return new ShortestPathSolution(data,Status.INFEASIBLE);
            }
            else
            {
               
                Collections.reverse(path_arc); /* comme nous sommes parti de la destination, il faut inverser l'ordre dans lequel nous avons ajouter les arcs */
                
                Path path_final = new Path(graph,path_arc);/* création du path */
                solution = new ShortestPathSolution(data,Status.OPTIMAL,path_final);
                return solution;
            }

        }
    }
}