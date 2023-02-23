public class TwoThreeTree {
    private Node root;
    private Node topScorer ;


    public TwoThreeTree(){
        init();
    }
    public void init() {
        Node x = new Node(Integer.MAX_VALUE ,Integer.MAX_VALUE);
        Node left = new Node(Integer.MIN_VALUE,Integer.MIN_VALUE);
        Node middle = new Node(Integer.MAX_VALUE,Integer.MAX_VALUE);
        x.setLeft(left);
        x.setMiddle(middle);
        left.setParent(x);
        middle.setParent(x);
        //x.setNodeId(Integer.MAX_VALUE);
        //x.getKey()
        this.root=x ;
        this.topScorer=new Node(Integer.MAX_VALUE , Integer.MAX_VALUE);
        this.topScorer.setScore(0);

    }

    public Node getTopScorer() {
        return topScorer;
    }

    public void setTopScorer(Node topScorer) {
        this.topScorer = topScorer;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }


    public void add(int faculty_id, Node player){
        player.setBelongsTo(faculty_id);
        insert(player);
    }


    public void insert(Node z){ //O(log)
        Node y = this.root;
        while (y.getLeft()!=null){
            if (z.getKey().isBigger(y.getLeft().getKey()) == -1)
                y=y.getLeft();
            else if (z.getKey().isBigger(y.getMiddle().getKey()) == -1)
                y = y.getMiddle();
            else y = y.getRight();
        }
        Node x = y.getParent();
        z = insertAndSplit(x,z);
        while (x != this.root){
            x = x.getParent();
            if (z != null){
                z = insertAndSplit(x,z);
            }
            else
                updateKey(x);
        }
        if (z!=null){
            Node w = new Node(Integer.MAX_VALUE,Integer.MAX_VALUE);
            setChildren(w,x,z,null);
            this.root = w;
        }


    }
    public Node insertAndSplit(Node x, Node z){
        Node l = x.getLeft();
        Node m = x.getMiddle();
        Node r = x.getRight();
        if (r==null){
            if (z.getKey().isBigger(l.getKey()) == -1)
                setChildren(x,z,l,m);

            else if (z.getKey().isBigger(m.getKey()) == -1)
                setChildren(x,l,z,m);

            else
                setChildren(x,l,m,z);

            return null;
        }
        Node y = new Node(Integer.MAX_VALUE,Integer.MAX_VALUE);
        if (z.getKey().isBigger(l.getKey()) == -1){
            setChildren(x,z,l,null);
            setChildren(y,m,r,null);
        }
        else if (z.getKey().isBigger(m.getKey()) == -1){
            setChildren(x,l,z,null);
            setChildren(y,m,r,null);
        }
        else if (z.getKey().isBigger(r.getKey()) == -1){
            setChildren(x,l,m,null);
            setChildren(y,z,r,null);
        }
        else {
            setChildren(x,l,m,null);
            setChildren(y,r,z,null);
        }
        return y;

    }

    public void setChildren(Node x, Node l, Node m, Node r){
        x.setLeft(l);
        x.setMiddle(m);
        x.setRight(r);
        l.setParent(x);
        if (m != null){
            m.setParent(x);
        }
        if (r!=null){
            r.setParent(x);
        }
        updateKey(x);
    }

    public void updateKey(Node x){
        x.setKey(x.getLeft().getKey());
        if (x.getMiddle()!=null){
            x.setKey(x.getMiddle().getKey());
        }
        if (x.getRight()!=null){
            x.setKey(x.getRight().getKey());
        }
    }

    public Node borrowOrMerge(Node y){
        Node z = y.getParent();
        if (y==z.getLeft()){
            Node x = z.getMiddle();
            if (x.getRight()!=null){
                setChildren(y,y.getLeft(),x.getLeft(),null);
                setChildren(x,x.getMiddle(),x.getRight(),null);
            }
            else {
                setChildren(x, y.getLeft(), x.getLeft(), x.getMiddle());
                setChildren(z,x,z.getRight(),null);
            }
            return z;
        }
        if (y==z.getMiddle()){
            Node x = z.getLeft();
            if (x.getRight()!=null){
                setChildren(y,x.getRight(),y.getLeft(),null);
                setChildren(x,x.getLeft(),x.getMiddle(),null);
            }
            else{
                setChildren(x,x.getLeft(),x.getMiddle(),y.getLeft());
                setChildren(z,x,z.getRight(),null);
            }
            return z;
        }
        Node x = z.getMiddle();
        if (x.getRight()!=null){
            setChildren(y,x.getRight(),y.getLeft(),null);
            setChildren(x,x.getLeft(),x.getMiddle(),null);
        }
        else{
            setChildren(x,x.getLeft(),x.getMiddle(),y.getLeft());
            setChildren(z,z.getLeft(),x,null);
        }
        return z;

    }

    public Node search(Node x, int value, int score){ // O(log)
        Key key= new Key(value,score) ;
        if (x.getLeft()==null && x.getMiddle()==null){
            if (x.getKey().isBigger(key) == 0){
                return x;
            }
            else return null;
        }
        if (key.isBigger(x.getLeft().getKey()) == 0 || key.isBigger(x.getLeft().getKey()) == -1){
            return search(x.getLeft(),value,score);
        }
        else if (key.isBigger(x.getMiddle().getKey()) == 0 || key.isBigger(x.getMiddle().getKey()) == -1) {
            return search(x.getMiddle(), value,score);
        }
        else return search(x.getRight(), value,score);
    }



    public void delete(Node x){ // O(log)
        Node y = x.getParent();
        if (x==y.getLeft()){
            setChildren(y,y.getMiddle(),y.getRight(),null);
        }
        else if (x==y.getMiddle()){
            setChildren(y,y.getLeft(),y.getRight(),null);
        }
        else{
            setChildren(y,y.getLeft(),y.getMiddle(),null);
        }
        while (y!=null){
            if (y.getMiddle()==null){
                if (y!=this.root){
                    y = borrowOrMerge(y);
                }
                else{
                    this.root=y.getLeft();
                    y.getLeft().setParent(null);
                    return;
                }
            }
            else {
                updateKey(y);
                y=y.getParent();
            }
        }
    }

    public Node successor(Node x){ //O(log)
        Node z = x.getParent() ;
        Node y ;
        while (x==z.getRight() || (z.getRight()==null && x==z.getMiddle()) ){
            x=z ;
            z= z.getParent() ;
        }
        if ( x==z.getLeft())
            y = z.getMiddle() ;
        else
            y = z.getRight() ;
        while ( y.getLeft() != null && y.getMiddle() != null )
            y = y.getLeft() ;
        if (y.getKey().getValueFirstPrior()  < Integer.MAX_VALUE)
            return y ;
        else return null ;
    }

    public Node predecessor(Node x){ //O(log)
        Node z = x.getParent() ;
        Node y ;
        while (x==z.getLeft() ){
            x=z ;
            z= z.getParent() ;
        }
        if ( x==z.getRight())
            y = z.getMiddle() ;
        else //if ( x==z.getRight())
            y = z.getLeft() ;
        while ( y.getLeft() != null && y.getMiddle() != null ) {
            if (y.getRight() == null)
                y = y.getMiddle();
            else
                y = y.getRight();
        }
        if (y.getKey().getValueFirstPrior() > Integer.MIN_VALUE)
            return y ;
        else return null ;
    }


    public Node minimum(){ //O(log)
        Node x =this.root ;
        while (x.getLeft() != null && x.getMiddle() != null)
            x=x.getLeft() ;
        x=x.getParent().getMiddle() ;
        if (x.getKey().getValueFirstPrior() != Integer.MAX_VALUE && x.getKey().getValueFirstPrior() != Integer.MIN_VALUE)
            return x ;
        else
            return null ;
    }

}