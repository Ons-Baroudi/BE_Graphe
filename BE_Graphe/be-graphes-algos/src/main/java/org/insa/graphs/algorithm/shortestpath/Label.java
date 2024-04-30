package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;

public class Label implements Comparable<Label> {
    protected float cost;
    private boolean marked; //sert au marquage du noeud 
    private boolean DansTas; //pour savoir si le noeud est dans le tas ;
    private Node pere; 
    private Node noeud;
    

    public Label(Node noeud){
        this.noeud=noeud;
        this.marked =false;
        this.cost=Float.POSITIVE_INFINITY;
        this.pere=null;
        this.DansTas=false;
    }
    public Node getNode (){
        return this.noeud;
    }
    public Node getpere(){
        return this.pere;
    }

    public float getCost(){
        return this.cost;
    }

    public float gettotalCost(){
        return this.cost;
    }
    
    public boolean getmarked(){
        return this.marked;
    }

    public boolean getDansTas(){
        return this.DansTas;
    }
    public void initMarkage(){
        this.marked=true;
    }
    public void initcost(float cout){
        this.cost=cout;
    }
    public void initpere(Node papa){
        this.pere=papa;
    }
    public void initDansTas(){
        this.DansTas=true;
    }


    @Override
    public int compareTo(Label other) {
        // Done
        int result ;
        if (this.gettotalCost()<other.gettotalCost()){
            result=-1;
        }
        else if (this.gettotalCost()==other.gettotalCost()){
            result=0;
        }
        else {
            result =1;
        }
        return result;
    }
    
}
