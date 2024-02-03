public class test {
    public static void main(String[] args) {
        Board b = new Board(); // creates a new empty board
        b.board[0][0] = 2;
        b.board[0][4] = 2;
        b.board[1][3] = 1;
        b.board[2][0] = 2;
        b.board[2][1] = 2;
        b.board[2][2] = 2;
        b.board[2][3] = 1;
        b.board[4][0] = 1;
        b.board[4][1] = 1;
        b.board[4][2] = 2;
        b.board[4][3] = 1;
        b.board[4][4] = 1; // assign different points with values
        b.displayBoard();
        System.out.println(new AIPlayer().heuristicEvaluation(b)); // prints the score
    }
}
