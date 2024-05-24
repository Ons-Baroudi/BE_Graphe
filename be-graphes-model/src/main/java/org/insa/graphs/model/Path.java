package org.insa.graphs.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * Class representing a path between nodes in a graph.
 * </p>
 * 
 * <p>
 * A path is represented as a list of {@link Arc} with an origin and not a list
 * of {@link Node} due to the multi-graph nature (multiple arcs between two
 * nodes) of the considered graphs.
 * </p>
 *
 */

/**
 * InnerPath
 */
public class Path {

    /**
     * Create a new path that goes through the given list of nodes (in order),
     * choosing the fastest route if multiple are available.
     * 
     * @param graph Graph containing the nodes in the list.
     * @param nodes List of nodes to build the path.
     * 
     * @return A path that goes through the given list of nodes.
     * 
     * @throws IllegalArgumentException If the list of nodes is not valid, i.e. two
     *         consecutive nodes in the list are not connected in the graph.
     * 
     */
    public static Path createFastestPathFromNodes(Graph graph, List<Node> nodes) {
        if (nodes.isEmpty()) {
            return new Path(graph);
        } else if (nodes.size() == 1) {
            return new Path(graph, nodes.get(0));
        }
    
        List<Arc> fastestArcs = new ArrayList<>(); //on cree une liste de fastest Arcs
        Node current = nodes.get(0); // Initialise avec le premier noeud
    
        for (int i = 1; i < nodes.size(); i++) {
            Node next = nodes.get(i); // Le noeud suivant à connecter
            Arc fastestArc = null;
            double minTravelTime = Double.MAX_VALUE;
    
            for (Arc arc : current.getSuccessors()) { // Parcourir les arcs du noeud actuel
                if (arc.getDestination().equals(next) && arc.getMinimumTravelTime() < minTravelTime) {
                    fastestArc = arc; // Trouver l'arc le plus rapide vers 'next'
                    minTravelTime = arc.getMinimumTravelTime();
                }
            }
            if (fastestArc == null) {
                throw new IllegalArgumentException("No valid path exists between " + current + " and " + next); //l'arc n'existe pas 
            }
    
            fastestArcs.add(fastestArc); // Ajouter l'arc le plus rapide à la liste
            current = next; // Passer au noeud suivant
        }
    
        return new Path(graph, fastestArcs);
    }
        
    /**
     * Create a new path that goes through the given list of nodes (in order),
     * choosing the shortest route if multiple are available.
     * 
     * @param graph Graph containing the nodes in the list.
     * @param nodes List of nodes to build the path.
     * 
     * @return A path that goes through the given list of nodes.
     * 
     * @throws IllegalArgumentException If the list of nodes is not valid, i.e. two
     *         consecutive nodes in the list are not connected in the graph.
     * 
     */
    public static Path createShortestPathFromNodes(Graph graph, List<Node> nodes) {
        if (nodes.isEmpty()) {
            return new Path(graph);
        } else if (nodes.size() == 1) {                     /*on cherche si le chemin est vide ou a que un seul noeud donc pas de problème tranquilo le chemin ne change pas*/
            return new Path(graph, nodes.get(0));
        }
    //SINON : on cree une liste de plus courts arcs 
        List<Arc> shortestArcs = new ArrayList<>();
        Node current = nodes.get(0); // Commence avec le premier noeud de la liste "comme étant notre premier pointeur de la liste qui va prendre le suivant à la fin"
    
        for (int i = 1; i < nodes.size(); i++) { //on parcours tous les voisins
            Node next = nodes.get(i); // Prochain noeud de la liste
            Arc shortestArc = null;
            double minLength = Double.MAX_VALUE; //on donne la valeur maximale de la length (idem algo min )

            for (Arc arc : current.getSuccessors()) { // Parcours tous les arcs sortants de 'current'
                if (arc.getDestination().equals(next) && arc.getLength() < minLength) { 
                    shortestArc = arc; // Trouve l'arc le plus court vers 'next'
                    minLength = arc.getLength();
                }
            }
    
            if (shortestArc == null) {  
                throw new IllegalArgumentException("No valid path from " + current + " to " + next);
            }
    
            shortestArcs.add(shortestArc); // Ajoute l'arc le plus court à la liste des arcs
            current = next; // Déplace 'current' au noeud 'next'
        }
    
        return new Path(graph, shortestArcs);
    }
    

    /**
     * Concatenate the given paths.
     * 
     * @param paths Array of paths to concatenate.
     * 
     * @return Concatenated path.
     * 
     * @throws IllegalArgumentException if the paths cannot be concatenated (IDs of
     *         map do not match, or the end of a path is not the beginning of the
     *         next).
     */
    public static Path concatenate(Path... paths) throws IllegalArgumentException {
        if (paths.length == 0) {
            throw new IllegalArgumentException("Cannot concatenate an empty list of paths.");
        }
        final String mapId = paths[0].getGraph().getMapId();
        for (int i = 1; i < paths.length; ++i) {
            if (!paths[i].getGraph().getMapId().equals(mapId)) {
                throw new IllegalArgumentException(
                        "Cannot concatenate paths from different graphs.");
            }
        }
        ArrayList<Arc> arcs = new ArrayList<>();
        for (Path path: paths) {
            arcs.addAll(path.getArcs());
        }
        Path path = new Path(paths[0].getGraph(), arcs);
        if (!path.isValid()) {
            throw new IllegalArgumentException(
                    "Cannot concatenate paths that do not form a single path.");
        }
        return path;
    }

    // Graph containing this path.
    private final Graph graph;

    // Origin of the path
    private final Node origin;

    // List of arcs in this path.
    private final List<Arc> arcs;

    /**
     * Create an empty path corresponding to the given graph.
     * 
     * @param graph Graph containing the path.
     */
    public Path(Graph graph) {
        this.graph = graph;
        this.origin = null;
        this.arcs = new ArrayList<>();
    }

    /**
     * Create a new path containing a single node.
     * 
     * @param graph Graph containing the path.
     * @param node Single node of the path.
     */
    public Path(Graph graph, Node node) {
        this.graph = graph;
        this.origin = node;
        this.arcs = new ArrayList<>();
    }

    /**
     * Create a new path with the given list of arcs.
     * 
     * @param graph Graph containing the path.
     * @param arcs Arcs to construct the path.
     */
    public Path(Graph graph, List<Arc> arcs) {
        this.graph = graph;
        this.arcs = arcs;
        this.origin = arcs.size() > 0 ? arcs.get(0).getOrigin() : null;
    }

    /**
     * @return Graph containing the path.
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * @return First node of the path.
     */
    public Node getOrigin() {
        return origin;
    }

    /**
     * @return Last node of the path.
     */
    public Node getDestination() {
        return arcs.get(arcs.size() - 1).getDestination();
    }

    /**
     * @return List of arcs in the path.
     */
    public List<Arc> getArcs() {
        return Collections.unmodifiableList(arcs);
    }

    /**
     * Check if this path is empty (it does not contain any node).
     * 
     * @return true if this path is empty, false otherwise.
     */
    public boolean isEmpty() {
        return this.origin == null;
    }

    /**
     * Get the number of <b>nodes</b> in this path.
     * 
     * @return Number of nodes in this path.
     */
    public int size() {
        return isEmpty() ? 0 : 1 + this.arcs.size();
    }

    /**
     * Check if this path is valid.
     * 
     * A path is valid if any of the following is true:
     * <ul>
     * <li>it is empty;</li>
     * <li>it contains a single node (without arcs);</li>
     * <li>the first arc has for origin the origin of the path and, for two
     * consecutive arcs, the destination of the first one is the origin of the
     * second one.</li>
     * </ul>
     * 
     * @return true if the path is valid, false otherwise.
     * 
     */
    /*on vérifie si le chemin est valide, c'est à dire; il doit être non vide, contient au moins un noeud
     * ou tout simplement chaque noeud d'origine est connecté au noeud destinataire
     * on a deja les fonctions getOrigin(), arcs.size(), isEmpty(), getDestination()
     */
    public boolean isValid() {
        // done
        if (this.isEmpty()){ //pour tester si le chemin est vide => valide 
            return true; 
        }
        else if(this.size()==1 || this.arcs.isEmpty()){
            return true; //=> chemin valide 
        }
        else{
            Node origine=this.getOrigin(); //on prend l'origine puis on parcours les arcs si on retourne à l'origine donc le chemin n'est plus valide 
            for(Arc arc : this.arcs){
                if(!origine.equals(arc.getOrigin())){
                    return false;
                }
                origine =arc.getDestination();
            }
            
        }
        return true;
    }

    /**
     * Compute the length of this path (in meters).
     * 
     * @return Total length of the path (in meters).
     */
    /*calcule la longueur totale du chemin 
    en sommant les longueurs de tous les arcs; on utilise une boucle for qui va parcourir tous les arcs du graphe */

    public float getLength() {
        // done:
        float longeurtotale=0.0f;
        for (Arc myArc:this.arcs){
            longeurtotale+=myArc.getLength();
        }
        return longeurtotale;
    }

    /**
     * Compute the time required to travel this path if moving at the given speed.
     * 
     * @param speed Speed to compute the travel time.
     * 
     * @return Time (in seconds) required to travel this path at the given speed (in
     *         kilometers-per-hour).
     * 
     */
    /*gettraveltime on l'a utilisé pour unn seul arc, maintenat on va faire sa somme sur pls arcs */
     /*on va caluler la durée du trjat à une vitesse speed */
    public double getTravelTime(double speed) {
        // done:
        double temps =0.0;
        double vitessereel=speed*(1000.0/3600.0);//vitesse en temps reel 
        float longeur =getLength();
        temps=longeur/vitessereel;
        return temps;
    }

    /**
     * Compute the time to travel this path if moving at the maximum allowed speed
     * on every arc.
     * 
     * @return Minimum travel time to travel this path (in seconds).
     * 
    /* on va calculer la durée de trajet la plus courte */
    public double getMinimumTravelTime() {
        // Done:
        double temps =0; 
        for (Arc myArc : this.arcs){
            temps +=myArc.getMinimumTravelTime(); 
        }
        return temps;
    }

}
