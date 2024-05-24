package org.insa.graphs.algorithm.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

import org.insa.graphs.model.Node;
import org.insa.graphs.model.Arc;

import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import java.io.IOException;
import java.util.ArrayList;


public abstract class TestAlgorithme {
    protected static Graph graph;
    ShortestPathAlgorithm algo;

    public abstract ShortestPathAlgorithm createInstance(ShortestPathData data);


    public void initAll() throws IOException {
        final String mapName = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr";
        
        @SuppressWarnings("resource")
        final GraphReader reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
        graph = reader.read();
    }

    private int getRandomNodeId(Graph graph) {
        return (int) (Math.random() * graph.size());
    }

    public void testPathInfeasable() throws IOException {
        final String mapName = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/bretagne.mapgr";
        @SuppressWarnings("resource")
        final GraphReader reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
        Graph graph2 = reader.read();
        int origin, destination;
        boolean pathFound = true;

        // on cherche 10 fois pour trouver un rand qui marche 
        for (int attempt = 0; attempt < 10; attempt++) {
            origin = getRandomNodeId(graph2);
            destination = getRandomNodeId(graph2);

            ShortestPathData data = new ShortestPathData(graph2, graph2.get(origin), graph2.get(destination), ArcInspectorFactory.getAllFilters().get(0));
            algo = createInstance(data);
            ShortestPathSolution shortestpath = algo.run();

            if (shortestpath.getStatus() == Status.INFEASIBLE) {
                pathFound = false;
                break;
            }
        }

        assertEquals(false, pathFound);
    }

    
    public void testPathZero() {
        int nodeId = getRandomNodeId(graph);
        ShortestPathData data = new ShortestPathData(graph, graph.get(nodeId), graph.get(nodeId), ArcInspectorFactory.getAllFilters().get(0));
        algo = createInstance(data);
        ShortestPathSolution shortestpath = algo.run();
        assertEquals(Status.INFEASIBLE, shortestpath.getStatus());
    }

    
    public void testIsValid() {
        int origin, destination;
        origin = getRandomNodeId(graph);
        destination = getRandomNodeId(graph);

        ShortestPathData data = new ShortestPathData(graph, graph.get(origin), graph.get(destination), ArcInspectorFactory.getAllFilters().get(0));
        algo = createInstance(data);
        ShortestPathSolution shortestpath = algo.run();
        Path path = shortestpath.getPath();
        if (path != null) {
            assertEquals(true, path.isValid());
        }
    }

    public void testcomparerBellman() {
        int origin, destination;
        origin = getRandomNodeId(graph);
        destination = getRandomNodeId(graph);

        ShortestPathData data = new ShortestPathData(graph, graph.get(origin), graph.get(destination), ArcInspectorFactory.getAllFilters().get(0));
        algo = createInstance(data);
        ShortestPathSolution shortestpath = algo.run();
        Path path = shortestpath.getPath();
        if (path != null) {
            assertEquals(true, path.isValid());
            algo = new BellmanFordAlgorithm(data);
            ShortestPathSolution bellman = algo.run();
            Path path_bellman = bellman.getPath();
            assertEquals(path.getLength(), path_bellman.getLength(), 0.1);
        }
    }
//teste d'optimalité
    public void testShortestPath() {
        int origin, destination;
        origin = getRandomNodeId(graph);
        destination = getRandomNodeId(graph);

        ShortestPathData data = new ShortestPathData(graph, graph.get(origin), graph.get(destination), ArcInspectorFactory.getAllFilters().get(0));
        algo = createInstance(data);
        ShortestPathSolution shortestpath = algo.run();
        Path path = shortestpath.getPath();
        if (path != null) {
            assertEquals(true, path.isValid());
            ArrayList<Node> nodes = new ArrayList<Node>(); //on récupère tous les noeuds de la solution
            nodes.add(path.getOrigin());
            for (Arc arc : path.getArcs()) {
                nodes.add(arc.getDestination());
            }
            Path path_path = Path.createShortestPathFromNodes(graph, nodes); //on insere dans un nouveau path et on compare
            assertEquals(path_path.getLength(), path.getLength(), 0.1);
        }
    }
//idem pour fastest path
    public void testFastestPath() {
        int origin, destination;
        origin = getRandomNodeId(graph);
        destination = getRandomNodeId(graph);

        ShortestPathData data = new ShortestPathData(graph, graph.get(origin), graph.get(destination), ArcInspectorFactory.getAllFilters().get(2));
        algo = createInstance(data);
        ShortestPathSolution shortestpath = algo.run();
        Path path = shortestpath.getPath();
        if (path != null) {
            assertEquals(true, path.isValid());
            ArrayList<Node> nodes = new ArrayList<Node>();
            nodes.add(path.getOrigin());
            for (Arc arc : path.getArcs()) {
                nodes.add(arc.getDestination());
            }
            Path path_path = Path.createFastestPathFromNodes(graph, nodes);
            assertEquals(path_path.getMinimumTravelTime(), path.getMinimumTravelTime(), 0.1);
        }
    }

    public void testShortestplusrapide() {
        int origin, destination;
        origin = getRandomNodeId(graph);
        destination = getRandomNodeId(graph);

        ShortestPathData data = new ShortestPathData(graph, graph.get(origin), graph.get(destination), ArcInspectorFactory.getAllFilters().get(0));
        algo = createInstance(data);
        ShortestPathSolution shortestpath = algo.run();
        Path path = shortestpath.getPath();
        if (path != null) {
            assertEquals(true, path.isValid());
            data = new ShortestPathData(graph, graph.get(origin), graph.get(destination), ArcInspectorFactory.getAllFilters().get(2));
            ShortestPathAlgorithm b = createInstance(data);
            ShortestPathSolution fastest = b.run();
            Path path_fastest = fastest.getPath();
            if (path_fastest != null) {
                assertTrue(path_fastest.getMinimumTravelTime() <= path.getMinimumTravelTime());
                assertTrue(path_fastest.getLength() >= path.getLength());
            }
        }
    }
}
