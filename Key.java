public class Key {
    private int valueFirstPrior;
    private int valueSecondPrior ;

    public Key(int value1, int value2){
        this.valueFirstPrior=value1 ;
        this.valueSecondPrior=value2 ;
    }

    public int isBigger(Key key){ //O(1)
        if (this.valueFirstPrior > key.getValueFirstPrior() )
            return 1 ;
        else if (this.valueFirstPrior < key.getValueFirstPrior())
            return -1 ;
        else {
            if (this.valueSecondPrior < key.valueSecondPrior)
                return 1 ;
            else if (this.valueSecondPrior > key.valueSecondPrior)
                return -1 ;
            else //equals
                return 0 ;
        }
    }

    public int getValueFirstPrior() {
        return valueFirstPrior;
    }

    public void setValueFirstPrior(int valueFirstPrior) {
        this.valueFirstPrior = valueFirstPrior;
    }

    public int getValueSecondPrior() {
        return valueSecondPrior;
    }

    public void setValueSecondPrior(int valueSecondPrior) {
        this.valueSecondPrior = valueSecondPrior;
    }
}
