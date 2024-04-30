package org.insa.graphs.algorithm.shortestpath;

public class Label implements Comparable<Label> {
    private int sommet;
    private int pere;
    private boolean marque;
    private double coutRealise;

    public Label(int sommet, int pere, boolean marque, double coutRealise)
    {
        this.sommet=sommet;
        this.pere=pere;
        this.marque=marque;
        this.coutRealise=coutRealise;
    }

    public int compareTo(Label l)
    {
        return Double.compare(this.getTotalCost(), l.getTotalCost());
    }

    public int getSommet()
    {
        return this.sommet;
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
        return this.marque;
    }
    public  double getRealCost()
    {
        return coutRealise;
    }
    public  double getTotalCost()
    {
        return coutRealise;
    }
    public void setCost(double coutRealise)
    {
        this.coutRealise=coutRealise;
    }
    public void setMarque(boolean marque)
    {
        this.marque=marque;
    }
}


