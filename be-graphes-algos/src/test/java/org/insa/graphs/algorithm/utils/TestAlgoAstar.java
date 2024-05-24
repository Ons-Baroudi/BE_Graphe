package org.insa.graphs.algorithm.utils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.Point;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;


public class TestAlgoAstar extends TestAlgorithme {

    @Override
    public ShortestPathAlgorithm createInstance(ShortestPathData data) {
        return new AStarAlgorithm(data);
    }

    private int getRandomNodeId(Graph graph) {
        return (int) (Math.random() * graph.size());
    }

    public void testAstarComparedToDjikstra() {
        ShortestPathData data = new ShortestPathData(graph, graph.get(getRandomNodeId(graph)), graph.get(getRandomNodeId(graph)), ArcInspectorFactory.getAllFilters().get(0));
        algo = createInstance(data);
        ShortestPathSolution astar = algo.run();
        Path path = astar.getPath();

        if (path == null) {
            System.out.println("No path found by A* algorithm.");
            return;
        }

        assertEquals(true, path.isValid());

        ShortestPathAlgorithm b = new DijkstraAlgorithm(data);
        ShortestPathSolution djikstra = b.run();
        Path path_djikstra = djikstra.getPath();

        if (path_djikstra == null) {
            System.out.println("No path found by Dijkstra algorithm.");
            return;
        }

        assertEquals(path.getLength(), path_djikstra.getLength(), 0.1);
    }

    public void testSolvingTime() throws IOException {
        final String mapName = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/carre-dense.mapgr";
        @SuppressWarnings("resource")
        final GraphReader reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
        Graph graph2 = reader.read();

        for (int i = 0; i < 4; i++) {
            int origin = getRandomNodeId(graph2);
            int destination = getRandomNodeId(graph2);

            ShortestPathData data = new ShortestPathData(graph2, graph2.get(origin), graph2.get(destination), ArcInspectorFactory.getAllFilters().get(0));
            algo = createInstance(data);
            ShortestPathSolution astar = algo.run();
            ShortestPathAlgorithm b = new DijkstraAlgorithm(data);
            ShortestPathSolution djikstra = b.run();

            System.out.println("Distance between " + origin + " and " + destination + ": " + Point.distance(graph2.get(origin).getPoint(), graph2.get(destination).getPoint()));

            if (astar.getSolvingTime() == null || djikstra.getSolvingTime() == null) {
                System.out.println("One of the algorithms did not find a path.");
                continue;
            }

            System.out.println("A* solving time: " + astar.getSolvingTime() + ", Dijkstra solving time: " + djikstra.getSolvingTime());
            assertTrue(astar.getSolvingTime().compareTo(djikstra.getSolvingTime()) <= 0);
        }
    }
}
