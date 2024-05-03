package org.insa.graphs.algorithm.shortestpath;

public class LabelStar extends Label{
    double cout_estime;
    public LabelStar(int sommet_courant,int pere,boolean marked,double coutmade,double cout_estime){
        super(sommet_courant,pere,marked,coutmade);
        this.cout_estime=cout_estime;
    }
    public double getcoutestime(){
        return cout_estime;
    }
    public void setcoutestime(double cout_estime){
        this.cout_estime=cout_estime;
    }
    public double getTotalCost(){
        return super.getTotalCost()+this.cout_estime;
        
    }
}
