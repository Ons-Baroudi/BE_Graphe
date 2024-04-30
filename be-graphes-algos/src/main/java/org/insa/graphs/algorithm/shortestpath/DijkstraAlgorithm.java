package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;


import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import java.util.HashMap;


public class DijkstraAlgorithm extends ShortestPathAlgorithm {


    Graph graph;/* graph sur lequel l'algorithme sera executé */
    BinaryHeap<Label> heap; /* binary heap permettant le tri des labels dans l'ordre croissant des couts de label */
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

        /* boucle permettant djikstra de s'éxecuter les deux conditions d'arrêt sont que le tas ne soit pas vide et que la destination ne soit pas marqué */
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
            
            Label selectedNode = heap.findMin();/* sélection du sommet à marqué (le plus petit du tas) */
            heap.deleteMin();
            selectedNode.setMarque(true);
            notifyNodeMarked(graph.get(selectedNode.getSommet()));

            /*on parcourt tout les arcs sortants du sommet que l'on vient de sélectionner */
            for(Arc successor : graph.get(selectedNode.getSommet()).getSuccessors())
            {
                /* on vérifie si l'arc est autorisé, par exemple si on est en mode roads only, les trottoirs ne sont pas autoriés */
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
                
                    /*on vérifie que le noeud voisin n'est pas marqué car si il l'est nous n'avons pas besoin de le traiter */
                    if(!successorLabel.getMarque())
                    {
                        /*on calcul le nouveau cout pour atteindre le sommet voisin */
                        double tempCost = data.getCost(successor)+selectedNode.getRealCost();

                        /* on vérifie si il améliore ou non le cout actuel du noeud */
                        if(tempCost<successorLabel.getRealCost())
                        {
                            /* condition pour vérifier que le label est dans le tas */
                            if(successorLabel.getRealCost()!=Double.POSITIVE_INFINITY)
                            {
                                heap.remove(successorLabel);
                            }
                            else
                            {
                                notifyNodeReached(succesorNode);
                            }
                            /* on met à jour avec le nouveaux cout et nouveau père */
                            successorLabel.setCost(tempCost);
                            successorLabel.setPere(selectedNode.getSommet());

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

    /* fonction permettant de récupérer le path à partir de la hashmap final */
    public ShortestPathSolution getPathFromSPA(Graph graph,HashMap<Integer, Label> LabelMap, ShortestPathData data )
    {
        ShortestPathSolution solution = null;

        ArrayList<Arc> path_arc = new ArrayList<Arc>(); 
        int currentNode=data.getDestination().getId();

        /* on vérifie que la destination est dans la hashmap, si elle l'est pas cela signifie que le chemin est infaisable */
        if(!LabelMap.containsKey(currentNode))
        {
            return new ShortestPathSolution(data,Status.INFEASIBLE);
        }
        else
        {
            /* boucle tant que le père courant à un père */
            while(true)
            {
                Label currentLabel=LabelMap.get(currentNode);
                int dadNode = currentLabel.getPere();
                if(dadNode==-1)
                {
                    break;
                }
                Label dadLabel = LabelMap.get(dadNode);

                /* on parcourt tout les arcs du père afin de recuperer l'arc qu'on utilise pour le path */
                for(Arc arc : graph.get(dadNode).getSuccessors())
                {
                    /* on vérifie que le père, le fils, cout de l'arc est le bon */
                    if(arc.getDestination().getId()==currentNode && arc.getOrigin().getId()==dadNode && Math.abs(data.getCost(arc)-(currentLabel.getRealCost()-dadLabel.getRealCost())) <= 0.01)
                    {
                        path_arc.add(arc);
                        break;
                    }
                }
                /* on met le currentnode a dadnode afin de remonter les pères*/
                currentNode=dadNode;
            }

            /* si la taille de la liste est 0 cela signifie que l'origin et la destination est le même noeud */
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