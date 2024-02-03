import java.util.*;

/*
   AIPlayer is a class that contains the methods for implementing the minimax search for playing the TicTacToe game.
*/
class AIPlayer {
    List<PointsAndScores> rootsChildrenScores; //an instance of the List class, a list of PointsAndScores objects holding the available moves and their values at the root of the search tree, i.e., the current game board.

    //constructor
    public AIPlayer() {
    }

    //method for returning the best move, the position that has the maximum value among all the available positions
    public Point returnBestMove() {
        int MAX = -100000; //The scores are not negative
        int best = -1; //The index for available positions are not negative

        for (int i = 0; i < rootsChildrenScores.size(); ++i) {
            if (MAX < rootsChildrenScores.get(i).score) {
                MAX = rootsChildrenScores.get(i).score;
                best = i;
            }
        }
        return best == -1 ? rootsChildrenScores.get(0).point : rootsChildrenScores.get(best).point; //Return the position that has the maximum value among all the available positions
    }

    //method for returning the minimum value in a list
    public int returnMin(List<Integer> list) {
        int min = Integer.MAX_VALUE;
        int index = -1;

        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) < min) {
                min = list.get(i);
                index = i;
            }
        }
        return list.get(index);
    }

    //method for returning the maximum value in a list
    public int returnMax(List<Integer> list) {
        int max = Integer.MIN_VALUE;
        int index = -1;

        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) > max) {
                max = list.get(i);
                index = i;
            }
        }
        return list.get(index);
    }

    //method for calling minimax search, which takes depth, turn of play, and board as arguments.
    public void callMinimax(int depth, int turn, Board b){
        rootsChildrenScores = new ArrayList<>();
        minimax(depth, turn, Integer.MIN_VALUE, Integer.MAX_VALUE, b); //method for implementing the minimax search algorithm with alpha-beta pruning
    }

    /*
       minimax is a method for implementing the minimax search algorithm with alpha-beta pruning in a recursive manner,
       which takes depth, turn of play, alpha, beta, and board as arguments.
       Set depth=0 for the current game position that is the root of the search tree.
       turn=1 represents the AI player's turn to play whilst turn=2 represents the user's turn to play.
       The AI player is player 'X' and the user is player 'O'.
       This method is the most important component of the TicTacToe program.
    */
    public int minimax(int depth, int turn, int alpha, int beta, Board b) { //added alpha, beta for alpha-beta pruning

        if (b.hasWhoWon(1)) return 100000; //If player 'X' has won, the minimax search reaches a win endgame and returns 1 as the value of this endgame position
        if (b.hasWhoWon(2)) return -100000; //If player 'O' has won, the minimax search reaches a loss endgame and returns -1 as the value of this endgame position

        List<Point> pointsAvailable = b.getAvailablePoints(); //Get a list of available moves on the current board
        if (pointsAvailable.isEmpty() || depth >= 6) return 0;   //If there is no available move, the minimax search reaches a draw endgame and returns 0 as the value of this endgame position.
                                                                //If the depth is 6, then it has reached the maximum depth, so it returns 0
        List<Integer> scores = new ArrayList<>(); //an instance of the List class, a list of integer scores holding the values of all the game positions in the next depth

        int temp;
        if (turn ==1) temp = Integer.MIN_VALUE; //added for alpha-beta pruning
        else temp = Integer.MAX_VALUE; //added for alpha-beta pruning

        //Choose an available position in natural order to make a move
        for (Point point : pointsAvailable) { //for each available move (position) ...
            if (turn == 1) { //if it is the AI player's turn to play ...
                b.placeAMove(point, 1); //Make the chosen move to obtain a new board, i.e., a new game position in the next depth
                //HeuristicEvaluation(b) calculates the heuristic value of the current board and adds the value from the recursion of other boards
                //The current score would be the sum of each board evaluated from a specific point
                int currentScore = heuristicEvaluation(b) + minimax(depth + 1, 2, alpha, beta, b); //Recursively call the minimax method with the new board, the corresponding depth and the user's turn as arguments. The recursion ends when reaching an endgame position.
                scores.add(currentScore); //Add the value of the game position to the list of scores
                temp = Math.max(temp, currentScore); //added for alpha-beta pruning
                alpha = Math.max(alpha, temp); //added for alpha-beta pruning
                if (depth == 0) //If it is at the root, add the chosen move and the corresponding value (score) to the list rootsChildrenScores
                    rootsChildrenScores.add(new PointsAndScores(currentScore, point));
            } else if (turn == 2) { //if it is the user's turn to play ...
                b.placeAMove(point, 2); //Make the chosen move to obtain a new board, i.e., a new game position in the next depth
                //HeuristicEvaluation(b) calculates the heuristic value of the current board and adds the value from the recursion of other boards
                //The current score would be the sum of each board evaluated from a specific point
                int currentScore = heuristicEvaluation(b) + minimax(depth + 1, 1, alpha, beta, b); //Recursively call the minimax method with the new board, the corresponding depth and the user's turn as arguments. The recursion ends when reaching an endgame position.
                scores.add(currentScore);
                temp = Math.min(temp, currentScore); //added for alpha-beta pruning
                beta = Math.min(beta, temp); //added for alpha-beta pruning
                //Recursively call the minimax method with the new board, the corresponding depth and the AI player's turn as arguments. The recursion ends when reaching an endgame position.
                //Add the value of the game position to the list of scores
            }
            b.placeAMove(point, 0); //Clear the chosen position after its value has been obtained by the minimax search.
            if (alpha >= beta)  //Check whether alpha is equal to or greater than beta. If so, there is no need to valuate the remaining game positions at this depth.
                break;
        }
        return turn == 1 ? returnMax(scores) : returnMin(scores); //If it is AI player's turn, return the maximum value. Otherwise, return the minimum value.
    }

    //Function that finds the heuristic value and return it
    //variables are being reset and reused
    public int heuristicEvaluation(Board board)
    {

        //If either player has won then it returns 100000 for AI and -100000 for player
        if(board.hasWhoWon(1))
            return (int) +Math.pow(10, 5);
        if(board.hasWhoWon(2))
            return -(int) Math.pow(10, 5);

        boolean[] seen = new boolean[2]; //helps to see if a line contains both players O(n) - doesn't take time
        int[] appearance = new int[2]; //stores how many times 1 or 2 is seen
        int value; //Current board value
        int totalSum = 0; //Total of all calculations
        int sum = 0; //Total of each line
        //Stacked loop to look through all points
        for(int i = 0; i < 5; i++)
        {
            //For analysing the rows
            for (int j = 0; j < 5; j++)
            {
                value = board.board[i][j];
                if (value != 0) {
                 seen[value - 1] = true; //If for example 1(AI) is the row it toggles the value to true, same as 2(player)
                 appearance[value - 1] += 1; //Increment for each value of 1 or 2 found
                }
            }
            if(seen[0] != seen[1]) //Checking if both 1 or 2 are seen. if there's only one true then proceed
            {
                if(appearance[0] != 0) //Checks that there's one or more occurrence of the 1 or 2
                    sum = (int) Math.pow(10, appearance[0]); //Finds 10^n of the number of times
                else if(appearance[1] != 0)
                    sum = -(int) Math.pow(10, appearance[1]); //If it's a player then sum with a negative value
            }
            totalSum += sum; //Adds the sum to the total sum
            sum = appearance[0] = appearance[1] = 0; //Reset the values to 0
            seen[0] = seen[1] = false; //Resets the value to false


            //For analysing the column, similar to the row
            for (int j = 0; j < 5; j++)
            {
                value = board.board[j][i]; //Only difference from row
                if (value != 0) {
                    seen[value - 1] = true;
                    appearance[value - 1] += 1;
                }
            }
            if(seen[0] != seen[1])
            {
                if(appearance[0] != 0)
                    sum = (int) Math.pow(10, appearance[0]);
                else if(appearance[1] != 0)
                    sum = -(int) Math.pow(10, appearance[1]);
            }
            totalSum += sum;
            sum = appearance[0] = appearance[1] = 0;
            seen[0] = seen[1] = false;
        }


        //For analysis the left diagonal
        for(int i = 0;i < 5; i++)
        {
            value = board.board[i][i];
            if (value != 0) {
                seen[value - 1] = true;
                appearance[value - 1] += 1;
            }
        }

        if(seen[0] != seen[1])
        {
            if(appearance[0] != 0)
                sum = (int) Math.pow(10, appearance[0]);
            else if(appearance[1] != 0)
                sum = -(int) Math.pow(10, appearance[1]);
        }
        totalSum += sum;
        sum = appearance[0] = appearance[1] = 0;
        seen[0] = seen[1] = false;

        //For analysis right diagonal
        for(int i = 0;i < 5; i++)
        {
            value = board.board[i][4 - i];
            if (value != 0) {
                seen[value - 1] = true;
                appearance[value - 1] += 1;
            }
        }

        if(seen[0] != seen[1])
        {
            if(appearance[0] != 0)
                sum = (int) Math.pow(10, appearance[0]);
            else if(appearance[1] != 0)
                sum = -(int) Math.pow(10, appearance[1]);
        }
        totalSum += sum;

        return totalSum; //Returns all the sums added together
    }
}
