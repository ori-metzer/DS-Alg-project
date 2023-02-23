import java.util.ArrayList;

public class TechnionTournament implements Tournament{
    private TwoThreeTree playersTreeScore;
    private TwoThreeTree facultiesWithPlayersTree;
    private TwoThreeTree facultyTreeScore ;


    TechnionTournament(){};

    @Override
    public void init() { // O(1)
        this.playersTreeScore = new TwoThreeTree();
        this.facultiesWithPlayersTree = new TwoThreeTree();
        this.facultyTreeScore = new TwoThreeTree();
    }

    @Override
    public void addFacultyToTournament(Faculty faculty) { // O(logn)
        //adding faculty to the tree sorted by ID
        Key key1 = new Key(faculty.getId(), 0) ;
        Node node1= new Node(faculty, key1) ;
        facultiesWithPlayersTree.insert(node1) ; //O(logn)

        //adding faculty to the tree sorted by score
        Key key2 = new Key(0,faculty.getId()) ;
        Node node2= new Node(faculty, key2) ;
        facultyTreeScore.insert(node2); //O(logn)
        addingNeighbors(facultyTreeScore, node2) ; //O(logn)
        updateTopScorer(facultyTreeScore, node2, true); //O(1)

    }

    @Override
    public void removeFacultyFromTournament(int faculty_id){ //O(logn+logm)
        Node facultyToRemove = facultiesWithPlayersTree.search( facultiesWithPlayersTree.getRoot(),faculty_id, 0); //O(logn)
        //before removing faculty set players as free agents.
        TwoThreeTree playersOfFaculty = facultyToRemove.getFacultyPlayersTree() ; //O(1)
        Node currNode = playersOfFaculty.minimum(); //O(log(11))

        while (currNode!= null){ // 11*O(logm) = O(logm)
            Node playerInScoreTree = playersTreeScore.search(playersTreeScore.getRoot(),currNode.getScore(),currNode.getKey().getValueFirstPrior());//O(logm)
            playerInScoreTree.setBelongsTo(0); //O(1)
            currNode= playersOfFaculty.successor(currNode) ; //O(log(11))
        }

        //searching faculty in faculty tree sorted by score
        Node facultyToRemove2 = facultyTreeScore.search( facultyTreeScore.getRoot(),facultyToRemove.getScore(), faculty_id); //O(logn)
        //deleting faculty from faculty tree sorted by ID
		facultiesWithPlayersTree.delete(facultyToRemove); //O(logn)
        if (facultyToRemove2 == facultyTreeScore.getTopScorer()){ //O(1) updating the top scorer in faculty tree sorted by score
            facultyTreeScore.setTopScorer(facultyToRemove2.getPredecessor());
        }
        changeNeighbors(facultyToRemove2) ; //O(1)
        facultyTreeScore.delete(facultyToRemove2); //O(logn)
    }

    @Override
    public void addPlayerToFaculty(int faculty_id,Player player) { //O(logm+logn)
        //inserting the player to the player tree sorted by score
        Key key1= new Key(0, player.getId()) ;
        Node playerNode1= new Node(player,key1) ;
        playersTreeScore.add(faculty_id, playerNode1); //O(logm)
        addingNeighbors(playersTreeScore, playerNode1); //O(logm)
        updateTopScorer(playersTreeScore, playerNode1, true); //O(1)

        //inserting the player to the player tree sorted by ID in the faculty
        Key key2 = new Key(player.getId(), 0) ;
        Node playerNode2 = new Node(player,key2) ;
        Node faculty = facultiesWithPlayersTree.search(facultiesWithPlayersTree.getRoot() ,faculty_id, 0); //O(logn)
        faculty.getFacultyPlayersTree().add(faculty_id, playerNode2); //O(log11)
        updateTopScorer(faculty.getFacultyPlayersTree(),playerNode2,false); //O(1)

    }

    @Override
    public void removePlayerFromFaculty(int faculty_id, int player_id) { //O(logm+logn)
        //remove player from faculty team.
        Node faculty = facultiesWithPlayersTree.search(facultiesWithPlayersTree.getRoot() ,faculty_id, 0) ;  //O(log(n))
        Node playerToRemove = faculty.getFacultyPlayersTree().search(faculty.getFacultyPlayersTree().getRoot() , player_id,0) ; //O(log(11))
        int score = playerToRemove.getScore() ;
        faculty.getFacultyPlayersTree().delete(playerToRemove);  //O(log(11))

        //updating the top scorer in faculty
        int maxScore = -1;
        Node currPlayer = faculty.getFacultyPlayersTree().minimum(); //O(log(11))
        Node newTopScorer = currPlayer;
        while (currPlayer!=null){ //11*O(log(11))
            if (currPlayer.getScore() > maxScore){
                maxScore = currPlayer.getScore();
                newTopScorer = currPlayer;
            }
            currPlayer=faculty.getFacultyPlayersTree().successor(currPlayer); //O(log(11))
        }
        faculty.getFacultyPlayersTree().setTopScorer(newTopScorer);

        // in playersTree find player and set to free agent.
        Node player = playersTreeScore.search(playersTreeScore.getRoot(), score,player_id) ; //O(log(m))
        player.setBelongsTo(0); //O(1)

    }

    @Override
    //O(logn+k*logm)
    public void playGame(int faculty_id1, int faculty_id2, int winner,
                         ArrayList<Integer> faculty1_goals, ArrayList<Integer> faculty2_goals) {
        Node faculty1 = facultiesWithPlayersTree.search(facultiesWithPlayersTree.getRoot(), faculty_id1 , 0); //O(logn)
        Node faculty2 = facultiesWithPlayersTree.search(facultiesWithPlayersTree.getRoot(), faculty_id2 , 0); //O(logn)

        Node faculty1_InScoreTree = facultyTreeScore.search(facultyTreeScore.getRoot(), faculty1.getScore(),faculty_id1) ; //O(logn)
        Node faculty2_InScoreTree = facultyTreeScore.search(facultyTreeScore.getRoot(), faculty2.getScore(),faculty_id2) ; //O(logn)
        // updating the score of the winning faculty.
        if (winner == 1) {
            int score=faculty1.getScore() + 3;
            faculty1.setScore(score);
            //update the score in the faculty tree score
            updateTree(facultyTreeScore, faculty1_InScoreTree, score) ; //O(logn)

        }
        else if (winner == 2) {
            int score=faculty2.getScore() + 3;
            faculty2.setScore(score);
            //update the score in the faculty tree score
            updateTree(facultyTreeScore, faculty2_InScoreTree, score) ; //O(logn)

        }

        else{
            int score1= faculty1.getScore() + 1 ;
            int score2=faculty2.getScore() + 1 ;
            faculty1.setScore(score1);
            faculty2.setScore(score2);

            //update the score in the faculty tree score
            updateTree(facultyTreeScore, faculty1_InScoreTree, score1) ; //O(logn)

            //update the score in the faculty tree score
            updateTree(facultyTreeScore, faculty2_InScoreTree, score2) ; //O(logn)

        }
        //update the top scorer
        updateTopScorer(facultiesWithPlayersTree, faculty1, false); // O(1)
        updateTopScorer(facultiesWithPlayersTree, faculty2,false); // O(1)
        updateTopScorer(facultyTreeScore, faculty1_InScoreTree,true); // O(1)
        updateTopScorer(facultyTreeScore, faculty2_InScoreTree,true); // O(1)


        // updating players score
        for (int player_id :  faculty1_goals) { // O(k*logm)
            //update players score in faculty
            Node player = faculty1.getFacultyPlayersTree().search(faculty1.getFacultyPlayersTree().getRoot() , player_id ,0);//O(log11)
            int score = player.getScore() ;
            player.setScore(score+1);
            //update top scorer player in faculty
            updateTopScorer(faculty1.getFacultyPlayersTree(),player, false);// O(1)

            //update player score in playersTree
            Node playerInPlayers = playersTreeScore.search(playersTreeScore.getRoot(),score, player_id) ; //O(logm)
            updateTree(playersTreeScore , playerInPlayers, score+1);// O(logm)

            //update top scorer player in playersTree
            updateTopScorer(playersTreeScore,playerInPlayers, true); // O(1)
        }

        for (int player_id :  faculty2_goals) { // O(k*logm)
            //update players score in faculty
            Node player = faculty2.getFacultyPlayersTree().search(faculty2.getFacultyPlayersTree().getRoot() , player_id , 0) ;
            int score= player.getScore() ;
            player.setScore(score+1);
            //update top scorer player in faculty
            updateTopScorer(faculty2.getFacultyPlayersTree(),player, false);

            //update player score in playersTree
            Node playerInPlayers = playersTreeScore.search(playersTreeScore.getRoot(),score, player_id) ;
            updateTree(playersTreeScore , playerInPlayers, score+1) ;

            //update top scorer player in playersTree
            updateTopScorer(playersTreeScore,playerInPlayers,true);
        }

    }

    @Override
    public void getTopScorer(Player player) { //O(1)
        Node topScorer = playersTreeScore.getTopScorer() ;
        player.setName(topScorer.getName());
        player.setId(topScorer.getKey().getValueSecondPrior());
    }

    @Override
    public void getTopScorerInFaculty(int faculty_id, Player player) { //O(logn)
        TwoThreeTree playersInFaculty = facultiesWithPlayersTree.search(facultiesWithPlayersTree.getRoot() ,faculty_id, 0).getFacultyPlayersTree(); //O(logn)
        Node topScorer = playersInFaculty.getTopScorer() ;
        player.setName(topScorer.getName());
        player.setId(topScorer.getKey().getValueFirstPrior());
    }

    @Override
    public void getTopKFaculties(ArrayList<Faculty> faculties, int k, boolean ascending) { //O(k)
        Node currNode= facultyTreeScore.getTopScorer() ;
        ArrayList<Faculty> temp= new ArrayList<>() ;
        for (int i=0; i<k ; i++) { //O(k)
            Faculty faculty = new Faculty(currNode.getKey().getValueSecondPrior(), currNode.getName());
            temp.add(faculty);
            currNode = currNode.getPredecessor(); //O(1)
        }
        if (ascending==true){ //O(k)
            for (int i=k-1; i>=0 ; i--)
                faculties.add(temp.get(i)) ;
        }
        else{
            for (int i=0; i<k ; i++) //O(k)
                faculties.add(temp.get(i)) ;
        }
    }

    @Override
    public void getTopKScorers(ArrayList<Player> players, int k, boolean ascending) { //O(k)
        Node currNode= playersTreeScore.getTopScorer() ;
        ArrayList<Player> temp= new ArrayList<>() ;
        for (int i=0; i<k ; i++) {
            Player player = new Player(currNode.getKey().getValueSecondPrior(), currNode.getName());
            temp.add(player);
            currNode = currNode.getPredecessor();
        }
        if (ascending==true){
            for (int i=k-1; i>=0 ; i--)
                players.add(temp.get(i)) ;
        }
        else{
            for (int i=0; i<k ; i++)
                players.add(temp.get(i)) ;
        }
    }

    @Override
    public void getTheWinner(Faculty faculty) { //O(1)
        Node topScorer = facultyTreeScore.getTopScorer() ;
        faculty.setName(topScorer.getName());
        faculty.setId(topScorer.getKey().getValueSecondPrior());
    }

    ///TODO - add below your own variables and methods

    public void updateTopScorer(TwoThreeTree tree, Node node, boolean sortedByScore) { //O(1)
        Node topScorer = tree.getTopScorer();
        if (node.getScore() > topScorer.getScore()) { //O(1)
            tree.setTopScorer(node);
        } else if (node.getScore() == topScorer.getScore()) { //O(1)
            if (sortedByScore == true) {
                if (node.getKey().getValueSecondPrior() < topScorer.getKey().getValueSecondPrior())
                    tree.setTopScorer(node);
            }
            else
                if (node.getKey().getValueFirstPrior() < topScorer.getKey().getValueFirstPrior())
                    tree.setTopScorer(node);
            }
        }



    public void addingNeighbors(TwoThreeTree twoThreeTree,Node node){ //O(log)
        Node succ = twoThreeTree.successor(node) ; //O(log)
        Node pred = twoThreeTree.predecessor(node) ; //O(log)
        node.setSuccessor(succ);
        node.setPredecessor(pred);
        if (succ!=null) // if the node we added is not a neighbor of the sentinel
            succ.setPredecessor(node);
        if (pred!= null) // if the node we added is not a neighbor of the sentinel
            pred.setSuccessor(node);
    }

    public void changeNeighbors(Node node) { //O(1) when removing a node updating its neighbors Predecessor and successor
        Node succ = node.getSuccessor() ;
        Node pred= node.getPredecessor() ;
        if (succ!=null)
            succ.setPredecessor(pred);
        if (pred!= null)
            pred.setSuccessor(succ);
    }
    //this function delete a node and update its neighbors and then insert the node back and update its neighbors
    public void updateTree(TwoThreeTree twoThreeTree, Node node, int score){ //O(log)
        changeNeighbors(node) ; //O(1)
        twoThreeTree.delete(node); //O(log)
        node.setScore(score);
        node.getKey().setValueFirstPrior(score);
        twoThreeTree.insert(node) ; //O(log)
        addingNeighbors(twoThreeTree,node); //O(log)
    }


}
