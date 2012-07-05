package knightstour2;
import java.util.ArrayList;
public class Solver 
{
    private int[] horizontal = {2,1,-1,-2,-2,-1,1,2};
    private int[] vertical = {-1,-2,-2,-1,1,2,2,1};
    private int currentRow, currentColumn;
    private int counter;
    private int[][] board;
    private int[][] accessibilityHeuristic = {{2,3,4,4,4,4,3,2},
                                              {3,4,6,6,6,6,4,3},
                                              {4,6,8,8,8,8,6,4},
                                              {4,6,8,8,8,8,6,4},
                                              {4,6,8,8,8,8,6,4},
                                              {4,6,8,8,8,8,6,4},
                                              {3,4,6,6,6,6,4,3},
                                              {2,3,4,4,4,4,3,2}};
    
    public static void main(String argrs[])
    {
        new Solver();
    }

    public Solver()
    {
        board = new int[8][8];
        counter = 1;
        currentRow = 1;
        currentColumn = 1;
        run(currentRow,currentColumn);
        //runAll();
    }
    
    public void runAll()
    {
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                currentRow = i;
                currentColumn = j;
                run(currentRow,currentColumn);
                reset();
            }
        }
    }

    public void run(int beginningRow, int beginningColumn)
    {
        board[beginningRow][beginningColumn] = 1;
        int numberOfMovesMade = 1;
        try
        {
            for(numberOfMovesMade = 1; numberOfMovesMade < 65; numberOfMovesMade++)
            {
                makeMove(findMinMove());
                //printBoard();
                //printHeuristicBoard();
            }
        }
        catch(Exception e)
        {
            
        }
        printBoard();
        System.out.println(beginningRow + ":" + beginningColumn + " - " + numberOfMovesMade);
        String stats = "";
        if(isFullTour())
        {
            stats += "Full Tour\n";
            if(isClosedTour())
            {
                stats += "Closed Tour\n";
            }
        }
        System.out.println(stats);
    }
    
    public boolean isFullTour()
    {
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                if(board[i][j] == 0)
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean isClosedTour()
    {
        for(int i = 0; i < 8; i++)
        {
            try
            {
                makeMove(i);
                if(board[currentRow][currentColumn] == 1)
                {
                    return true;
                }
                retractMove(i);
            }
            catch(Exception e)
            {
                
            }
        }
        return false;
    }

    public int findMinMove()
    {
        ArrayList<Integer>identicalMoveValues = new ArrayList<Integer>();
        int minMove = -1;
        int minHeuristicValue = 9;
        for(int i = 0; i < 8; i++)
        {
            try
            {
                makeMove(i);
                if(getHeuristicValue() <= minHeuristicValue)
                {
                    if(getHeuristicValue() == minHeuristicValue)
                    {
                        identicalMoveValues.add(getHeuristicValue());
                    }
                    minMove = i;
                    minHeuristicValue = getHeuristicValue();
                }
                retractMove(i);
            }
            catch(Exception e)
            {
                
            }
        }
        if(identicalMoveValues.size() > 0)
        {
            try
            {
                //minMove = resolveMoves(identicalMoveValues.toArray());
            }
            catch(Exception e)
            {
                
            }
        }
        return minMove;
    }
    
    public int resolveMoves(Object[] moves) throws Exception
    {
        int minMove = -1;
        
        for(Object o : moves)
        {
            makeMove((Integer)o);
            int temp = findMinMove();
            makeMove(findMinMove());
            if(getHeuristicValue() <= minMove)
            {
                minMove = getHeuristicValue();
            }
            retractMove((Integer)o);
            retractMove(temp);
        }
        return minMove;
    }

    public void makeMove(int moveNumber) throws Exception
    {
        if(moveNumber <= 7 && moveNumber >= 0)
        {
            if(board[currentRow + vertical[moveNumber]][currentColumn + horizontal[moveNumber]] == 0 &&
               currentRow + vertical[moveNumber] <= 7 && currentRow + vertical[moveNumber] >= 0 &&
               currentColumn + horizontal[moveNumber] <= 7 && currentColumn + horizontal[moveNumber] >= 0)
            {
                accessibilityHeuristic[currentRow][currentColumn]--;
                currentRow += vertical[moveNumber];
                currentColumn += horizontal[moveNumber];
                counter++;
                board[currentRow][currentColumn] = counter;
            }
            else
            {
                throw new Exception("out of bounds");
            }
        }
        else
        {
            throw new Exception();
        }
    }

    public void retractMove(int moveNumber) throws Exception
    {
        if(moveNumber <= 7 && moveNumber >= 0)
        {
            board[currentRow][currentColumn] = 0;
            currentRow -= vertical[moveNumber];
            currentColumn -= horizontal[moveNumber];
            accessibilityHeuristic[currentRow][currentColumn]++;
            counter--;
        }
        else
        {
            throw new Exception();
        }
    }
    
    public int getHeuristicValue()
    {
        return accessibilityHeuristic[currentRow][currentColumn];
    }

    public void printBoard()
    {
        for(int i = 0; i < board.length; i++)
        {
            for(int j = 0; j < board[i].length; j++)
            {
                System.out.printf("%4d", board[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
    
    public void printHeuristicBoard()
    {
        for(int i = 0; i < board.length; i++)
        {
            for(int j = 0; j < board[i].length; j++)
            {
                System.out.printf("%4d", accessibilityHeuristic[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
    
    public void reset()
    {
        counter = 1;
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                board[i][j] = 0;
            }
        }
        int[][] temp = {{2,3,4,4,4,4,3,2},
                        {3,4,6,6,6,6,4,3},
                        {4,6,8,8,8,8,6,4},
                        {4,6,8,8,8,8,6,4},
                        {4,6,8,8,8,8,6,4},
                        {4,6,8,8,8,8,6,4},
                        {3,4,6,6,6,6,4,3},
                        {2,3,4,4,4,4,3,2}};
        accessibilityHeuristic = temp;
    }
}