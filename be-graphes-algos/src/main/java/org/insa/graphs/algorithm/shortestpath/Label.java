package org.insa.graphs.algorithm.shortestpath;

public class Label implements Comparable<Label> {
    private int origine;
    private int pere;
    private boolean marked;
    private double coutmade;

    public Label(int origine, int pere, boolean marked, double coutmade)
    {
        this.origine=origine; //sommet actuel
        this.pere=pere;
        this.marked=marked;
        this.coutmade=coutmade;
    }

    public int getorigine() //sommet_courant 
    {
        return this.origine;
    }
    public int getPere()
    {
        return this.pere;
    }
    public void setPere(int pere)
    {
        this.pere=pere;
    }
    public boolean getMarque()
    {
        return this.marked;
    }
    public  double getRealCost()
    {
        return coutmade;
    }
    public  double getTotalCost()
    {
        return coutmade;
    }
    public void setCost(double coutmade)
    {
        this.coutmade=coutmade;
    }
    public void setMarque(boolean marked)
    {
        this.marked=marked;
    }
    
    public int compareTo(Label l)
    {
        return Double.compare(this.getTotalCost(), l.getTotalCost());
    }
}


