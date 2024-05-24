package org.insa.graphs.algorithm.utils;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;

public class TestAlgoDjikstra extends TestAlgorithme
{
    public ShortestPathAlgorithm createInstance(ShortestPathData data)
    {
        return new DijkstraAlgorithm(data);
    }    
}