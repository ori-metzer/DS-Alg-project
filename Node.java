public class Node {
    private Key key ;
    private String name ;
    private Node left;
    private Node middle;
    private Node right;
    private Node parent;
    private TwoThreeTree facultyPlayersTree;
    private int belongsTo;
    private int score ;
    private Node successor ;
    private Node predecessor ;


    public Node(Player player, Key key){
        this.key = key ;
        this.name= player.getName();
        this.left=null;
        this.middle=null;
        this.right=null;
        this.parent=null;
        this.facultyPlayersTree = null ;
        this.belongsTo = 0 ; // init as
        this.score= 0 ;
    }

    public Node(Faculty faculty , Key key){
        this.key=key ;
        this.name= faculty.getName()  ;
        this.left=null;
        this.middle=null;
        this.right=null;
        this.parent=null;
        this.facultyPlayersTree = new TwoThreeTree() ;
        this.belongsTo = 0 ;
        this.score= 0 ;


    }
    public Node(int score ,int id){ // for sentinels
        this.key = new Key(score,id) ;
        this.left=null;
        this.middle=null;
        this.right=null;
        this.parent=null;
    }

    public int getBelongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(int belongsTo) {
        this.belongsTo = belongsTo;
    }

    public void setScore(int score) {
        this.score=score ;
    }

    public int getScore() {
        return this.score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TwoThreeTree getFacultyPlayersTree() {
        return facultyPlayersTree;
    }

    public void setFacultyPlayersTree(TwoThreeTree facultyPlayersTree) {
        this.facultyPlayersTree = facultyPlayersTree;
    }

    public Node getLeft() {
        return left;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setMiddle(Node middle) {
        this.middle = middle;
    }

    public Node getMiddle() {
        return middle;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getParent() {
        return parent;
    }

  //  public void setNodeId(int nodeId) {
  //      this.key.setId(nodeId) ;
  //  }

    public Node getRight() {
        return right;
    }


   // public int getNodeId() {
  //     return this.key.getId();
 //   }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public Node getSuccessor() {
        return successor;
    }

    public void setSuccessor(Node successor) {
        this.successor = successor;
    }

    public Node getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(Node predecessor) {
        this.predecessor = predecessor;
    }
}
