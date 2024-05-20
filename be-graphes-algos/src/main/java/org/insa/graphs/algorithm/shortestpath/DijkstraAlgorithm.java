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

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    // Graphe sur lequel l'algorithme sera exécuté
    Graph graph;

    // Tas binaire pour trier les labels dans l'ordre croissant des coûts
    BinaryHeap<Label> heap;

    // Liste pour associer les IDs des nœuds à leurs labels
    ArrayList<Label> labelList;

    // Constructeur pour initialiser l'algorithme avec les données données
    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
        graph=data.getGraph();
        heap=new BinaryHeap<Label>();

        // Initialiser la liste des labels avec des valeurs nulles
        labelList=new ArrayList<>(Collections.nCopies(graph.size(), null));
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data=getInputData();

        // Initialisation en insérant l'origine dans la liste et dans le tas
        int origin = data.getOrigin().getId();
        Label labelOrigin=initLabel(origin, -1, false, 0);
        labelList.set(origin, labelOrigin);
        heap.insert(labelOrigin);
        notifyOriginProcessed(graph.get(origin));

        // Boucle principale de l'algorithme de Dijkstra
        // Continue jusqu'à ce que le tas soit vide ou que tous les nœuds soient marqués
        while (heap.size() !=0) {
            // Vérifie si le label de destination est marqué
            int destinationId = data.getDestination().getId();
            if (labelList.get(destinationId) !=null && labelList.get(destinationId).getMarque()) {
                break; // Si le nœud de destination est marqué, arrêter l'algorithme
            }

            // Sélection du sommet à marquer (le coût minimal ou le premier du tas)
            Label selectedNode=heap.findMin();
            heap.deleteMin();
            selectedNode.setMarque(true);
            notifyNodeMarked(graph.get(selectedNode.getorigine()));

            // Parcours de tous les arcs sortants du sommet sélectionné
            for (Arc successor : graph.get(selectedNode.getorigine()).getSuccessors()) {
                // Vérifie si l'arc est accessible
                if (data.isAllowed(successor)) {
                    Node successorNode = successor.getDestination();
                    Label successorLabel;

                    // Vérifie si le nœud de destination est dans la liste, sinon on le crée
                    if (labelList.get(successorNode.getId())==null) {
                        successorLabel=initLabel(successorNode.getId(), -1, false, Double.POSITIVE_INFINITY);
                        labelList.set(successorNode.getId(), successorLabel);
                    } else {
                        successorLabel=labelList.get(successorNode.getId());
                    }

                    // Traitement du nœud si non marqué
                    if (!successorLabel.getMarque()) {
                        double tempCost=data.getCost(successor) + selectedNode.getRealCost();

                        // Mise à jour du coût et réinsertion dans le tas si amélioration
                        if (tempCost<successorLabel.getRealCost()) {
                            //on teste si le cout est /= à une valeur infinie
                            if (successorLabel.getRealCost() != Double.POSITIVE_INFINITY) {
                                try {
                                    heap.remove(successorLabel);
                                } catch (ElementNotFoundException element) {}
                            } else {
                                notifyNodeReached(successorNode);
                            }
                            successorLabel.setCost(tempCost);
                            successorLabel.setPere(selectedNode.getorigine());
                            heap.insert(successorLabel);
                        }
                    }
                }
            }
        }

        notifyDestinationReached(data.getDestination());
        return getPathFromSPA(graph, labelList, data); 
    }

    // Méthode pour initialiser un label
    public Label initLabel(int nodeId, int fatherNode, boolean marque, double coutRealise) {
        return new Label(nodeId, fatherNode, marque, coutRealise);
    }

    // Fonction pour récupérer le chemin à partir de la liste des labels
    public ShortestPathSolution getPathFromSPA(Graph graph, ArrayList<Label> labelList, ShortestPathData data) {
        ShortestPathSolution solution = null;

        ArrayList<Arc> path_arc = new ArrayList<Arc>();
        int currentNode = data.getDestination().getId();

        // Teste si la destination est dans la liste, sinon le chemin est inaccessible
        if (labelList.get(currentNode) == null) {
            return new ShortestPathSolution(data, Status.INFEASIBLE);
        } else {
            while (true) {
                Label currentLabel = labelList.get(currentNode);
                int nodeMarked = currentLabel.getPere();
                if (nodeMarked == -1) {
                    break;
                }
                Label dadLabel = labelList.get(nodeMarked);

                // Parcourt des arcs pour trouver le chemin
                for (Arc arc : graph.get(nodeMarked).getSuccessors()) {
                    if (arc.getDestination().getId() == currentNode && arc.getOrigin().getId() == nodeMarked
                            && Math.abs(data.getCost(arc) - (currentLabel.getRealCost() - dadLabel.getRealCost())) <= 0.01) {
                        path_arc.add(arc);
                        break;
                    }
                }
                currentNode = nodeMarked;
            }

            // Cas où un chemin n'existe pas ou un seul arc existe
            if (path_arc.size() == 0) {
                return new ShortestPathSolution(data, Status.INFEASIBLE);
            } else {
                Collections.reverse(path_arc); // Inversion de l'ordre des arcs
                Path path_final = new Path(graph, path_arc); // Création du chemin
                solution = new ShortestPathSolution(data, Status.OPTIMAL, path_final);
                return solution;
            }
        }
    }
}

