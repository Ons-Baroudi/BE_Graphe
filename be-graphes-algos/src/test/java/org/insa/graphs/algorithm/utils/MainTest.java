package org.insa.graphs.algorithm.utils;

import java.io.IOException;


public class MainTest {
    
    // ANSI escape code constants for colors
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";
    public static void main(String[] args) {
        for (int i=0;i<100;i++){
        // on crée une instance de TestAlgoAstar
        TestAlgoAstar testAstar = new TestAlgoAstar();
        try {
            testAstar.initAll(); // Initialiser le graphe pour TestAlgoAstar
            System.out.println("Executing testAstar.testAstarComparedToDjikstra()");
            testAstar.testAstarComparedToDjikstra();
            System.out.println(ANSI_GREEN + "Test testAstarComparedToDjikstra passed." + ANSI_RESET);

            System.out.println("Executing testAstar.testSolvingTime()");
            testAstar.testSolvingTime();
            System.out.println(ANSI_GREEN + "Test testSolvingTime passed." + ANSI_RESET);
        } catch (IOException e) {
            System.err.println(ANSI_RED + "IOException during TestAlgoAstar tests: " + e.getMessage() + ANSI_RESET);
        } catch (AssertionError e) {
            System.err.println(ANSI_RED + "AssertionError during TestAlgoAstar tests: " + e.getMessage() + ANSI_RESET);
        }

        // on crée une instance de TestAlgoDjikstra
        TestAlgoDjikstra testDjikstra = new TestAlgoDjikstra();
        try {
            testDjikstra.initAll(); // Initialiser le graphe pour TestAlgoDjikstra
            System.out.println("Executing testDjikstra.testPathInfeasible()");
            testDjikstra.testPathInfeasable();
            System.out.println(ANSI_GREEN + "Test testPathInfeasible passed." + ANSI_RESET);

            System.out.println("Executing testDjikstra.testPathZero()");
            testDjikstra.testPathZero();
            System.out.println(ANSI_GREEN + "Test testPathZero passed." + ANSI_RESET);

            System.out.println("Executing testDjikstra.testIsValid()");
            testDjikstra.testIsValid();
            System.out.println(ANSI_GREEN + "Test testIsValid passed." + ANSI_RESET);

            System.out.println("Executing testDjikstra.testPathCourt()");
            testDjikstra.testcomparerBellman();
            System.out.println(ANSI_GREEN + "Test testPathCourt passed." + ANSI_RESET);

            System.out.println("Executing testDjikstra.testShortestPath()");
            testDjikstra.testShortestPath();
            System.out.println(ANSI_GREEN + "Test testShortestPath passed." + ANSI_RESET);

            System.out.println("Executing testDjikstra.testFastestPath()");
            testDjikstra.testFastestPath();
            System.out.println(ANSI_GREEN + "Test testFastestPath passed." + ANSI_RESET);

            System.out.println("Executing testDjikstra.testShortestplusrapide()");
            testDjikstra.testShortestplusrapide();
            System.out.println(ANSI_GREEN + "Test testShortestplusrapide passed." + ANSI_RESET);
        } catch (IOException e) {
            System.err.println(ANSI_RED + "IOException during TestAlgoDjikstra tests: " + e.getMessage() + ANSI_RESET);
        } catch (AssertionError e) {
            System.err.println(ANSI_RED + "AssertionError during TestAlgoDjikstra tests: " + e.getMessage() + ANSI_RESET);
        }
    }
}
}


