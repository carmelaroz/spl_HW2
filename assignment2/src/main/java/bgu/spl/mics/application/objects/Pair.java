package bgu.spl.mics.application.objects;

public class Pair <V,K> {
    private V firstValue;
    private K secondValue;
    public Pair(V firstValue, K secondValue){
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }
    public V getFirstValue(){
        return firstValue;
    }
    public K getSecondValue(){
        return secondValue;
    }
}
