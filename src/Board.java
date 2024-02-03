import java.util.*;

// Class for storing x and y values for the board
class Point {
    int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // overriding to return different format
    @Override
    public String toString() {
        return "[" + (x+1) + ", " + (y+1) + "]";
    }
}

// Class used to store points and score, so we can use it to choose what point to use from the score
class PointsAndScores {
    int score;
    Point point;

    PointsAndScores(int score, Point point) {
        this.score = score;
        this.point = point;
    }
}

// This class builds the board and holds different functions that hold control / represent the board
class Board {
    List<Point> availablePoints; // holds all position available
    Scanner scan = new Scanner(System.in); // used to get user input
    int[][] board = new int[5][5]; // build a 5 x 5 int to store values for each positions

    public Board() {
    }

    public boolean isGameOver() { // returns if the game is over by checking is there's no points left or if either players have won
        return (hasWhoWon(1) || hasWhoWon(2) || getAvailablePoints().isEmpty());
    }

    // takes in an int as a parameter which determines which player it is and checks if they've won
    public boolean hasWhoWon(int who) {

        // checks if the player has won diagonally both left and right, the parameter value is used to
        // check if the board coordinates are all equal to it
        if ((board[0][0] == board[1][1]
                && board[0][0] == board[2][2]
                && board[0][0] == board[3][3]
                && board[0][0] == board[4][4]
                && board[0][0] == who) ||
                (board[0][4] == board[1][3]
                        && board[0][4] == board[2][2]
                        && board[0][4] == board[3][1]
                        && board[0][4] == board[4][0]
                        && board[0][4] == who)) {
            return true;
        }

        // checks each row and columns to see if the player has won
        for (int i = 0; i < 5; ++i) {
            if ((board[i][0] == board[i][1]
                    && board[i][0] == board[i][2]
                    && board[i][0] == board[i][3]
                    && board[i][0] == board[i][4]
                    && board[i][0] == who) ||
                    (board[0][i] == board[1][i]
                            && board[0][i] == board[2][i]
                            && board[0][i] == board[3][i]
                            && board[0][i] == board[4][i]
                            && board[0][i] == who)) {
                return true;
            }
        }
        return false;  // none of the statement was true so the player did not win
    }

    // this function returns point available in board
    public List<Point> getAvailablePoints() {
        availablePoints = new ArrayList<>(); // initializes the array
        // stacked for loop to check each points. if the value of the point is 0 then it's free and this is added to the array
        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {
                if (board[i][j] == 0) {
                    availablePoints.add(new Point(i, j));
                }
            }
        }
        return availablePoints; // returns all the point
    }

    // returns the value of a coordinate
    public int getState(Point point){
        return board[point.x][point.y];
    }

    // updates the value of a coordinate when a player picks the point.
    public void placeAMove(Point point, int player) {
        board[point.x][point.y] = player;
    }

    // displays the board
    public void displayBoard() {
        System.out.println();

        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {
                if (board[i][j]==1)
                    System.out.print("X "); // checks if the value of the coordinate is 1 then prints x rather than .
                else if (board[i][j]==2)
                    System.out.print("O "); // similar to above but displays 0
                else
                    System.out.print(". "); // no player has the point
            }
            System.out.println();
        }
    }
}
