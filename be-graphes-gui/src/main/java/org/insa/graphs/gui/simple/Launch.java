package org.insa.graphs.gui.simple;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.gui.drawing.Drawing;
import org.insa.graphs.gui.drawing.components.BasicDrawing;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.model.io.PathReader;

public class Launch {

    /**
     * Create a new Drawing inside a JFrame an return it.
     * 
     * @return The created drawing.
     * 
     * @throws Exception if something wrong happens when creating the graph.
     */
    public static Drawing createDrawing() throws Exception {
        BasicDrawing basicDrawing = new BasicDrawing();
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("BE Graphes - Launch");
                frame.setLayout(new BorderLayout());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                frame.setSize(new Dimension(600, 800));
                frame.setContentPane(basicDrawing);
                frame.validate();
            }
        });
        return basicDrawing;
    }

    public static void main(String[] args) throws Exception {

        // Visit these directory to see the list of available files on Commetud.

        //final String mapName = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/carre-dense.mapgr";
        //final String pathName = "/home/hassouna/Bureau/3_eme_année_MIC/BE_Graphe/path_0x851_35299_7678.path";
        
        final String mapName = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/belgium.mapgr";
        final String pathName = "/home/hassouna/Bureau/3_eme_année_MIC/BE_Graphe/path_be_297546_196709.path";

        
        try (// Create a graph reader.
        GraphReader reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))))) {
            // TODO: Read the graph.
            final Graph graph = reader.read();

            // Create the drawing:
            final Drawing drawing = createDrawing();

            // TODO: Draw the graph on the drawing.

            drawing.drawGraph(graph);
            ShortestPathData data = new ShortestPathData(graph, graph.get(1200), graph.get(1600),ArcInspectorFactory.getAllFilters().get(0));
            DijkstraAlgorithm algo =new DijkstraAlgorithm(data);
            ShortestPathSolution Dijkstra =algo.run();
            //BellmanFordAlgorithm alog1 =new BellmanFordAlgorithm(data);
            //ShortestPathSolution Bellmanford =alog1.run();
            AStarAlgorithm algo2 =new AStarAlgorithm(data);
            ShortestPathSolution Astar =algo2.run();

            // TODO: Create a PathReader.
            final PathReader pathReader = new BinaryPathReader(new DataInputStream(new BufferedInputStream(new FileInputStream(pathName))));
            // TODO: Read the path.
            final Path path = Dijkstra.getPath();
            //final Path path1 =Bellmanford.getPath();
            final Path path2=Astar.getPath(); 

            System.out.println("la longeur du chemin djikstra "+path.getLength());
            System.out.println("le temps minimum Djikstra : " + path.getMinimumTravelTime());
            System.out.println("le temps du chemin Djikstra: "+path.getTravelTime(65.0));
            System.out.println("temps minimum Astar: " +path2.getMinimumTravelTime());
            System.out.println("le temps du trajet Astar"+path2.getTravelTime(65.0));
            //System.out.println(path1.getLength());
            System.out.println("la longeur du chemin Astar"+path2.getLength());

            // TODO: Draw the path.
            //drawing.drawPath(path);
            //drawing.drawPath(path1);
            drawing.drawPath(path2);
        }
    }

}
