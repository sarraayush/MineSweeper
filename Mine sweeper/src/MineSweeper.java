import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.Border;

public class MineSweeper{

    private class MineTile extends JButton {
        int r;
        int c;
        public MineTile( int r, int c)
        {
            this.r = r;
            this.c = c;
        }

    }
    JFrame frame = new JFrame("MineSweeper");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();

    JPanel boardPanel = new JPanel();

    int tileSize = 70;
    int numRows = 8;
    int numCols = numRows;

    int boardWidth = tileSize * numCols;
    int boardHeight = tileSize * numRows;

    MineTile[][] board = new MineTile[numRows][numCols];
    int mineCount = 10;
    ArrayList<MineTile>mineList;

    Random random = new Random();
    int clickedTiles = 0;
    boolean gameOver = false;
    MineSweeper()
    {
        frame.setSize(boardWidth , boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLayout(new BorderLayout());
        textLabel.setFont(new Font("Arial", Font.BOLD , 25 ));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("MineSweeper");
        textLabel.setOpaque(true);
        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel , BorderLayout.NORTH);
        boardPanel.setLayout(new GridLayout(numRows , numCols));
//        boardPanel.setBackground(Color.GREEN);
        frame.add(boardPanel);



        for(int r =0 ; r < numRows ; r++)
        {
            for(int c = 0; c < numCols; c++)
            {
                MineTile tile = new MineTile(r , c);
                board[r][c] = tile;
                tile.setFocusable(false);
                tile.setMargin(new Insets(0 ,0 , 0 , 0));
                tile.setFont(new Font( "Arial Unicode MS" , Font.PLAIN , 45));
//                tile.setText("");
                tile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e)
                    {
                        if(gameOver)
                        {
                            return;
                        }
                        MineTile tile = (MineTile) e.getSource();
                        if(e.getButton() == MouseEvent.BUTTON1)
                        {
                            if(tile.getText() == "")
                            {
                                if(mineList.contains(tile))
                                {
                                    revealMines();
                                }
                                else
                                {
                                    checkMines(tile.r , tile.c);
                                }
                            }
                        }
                        else
                            if(e.getButton() == MouseEvent.BUTTON3)
                            {
                                if(tile.getText() == "" && tile.isEnabled())
                                {
                                    tile.setText("🚩");
                                }
                                else
                                    if(tile.getText() == "🚩")
                                    {
                                        tile.setText("");
                                    }
                            }
                    }
                }

                );
                boardPanel.add(tile);
            }
        }
        frame.setVisible(true);

        setMines();

    }
    void setMines()
    {
        mineList = new ArrayList<MineTile>();
//        mineList.add(board[2][2]);
//        mineList.add(board[2][3]);
//        mineList.add(board[5][6]);
//        mineList.add(board[3][4]);
//        mineList.add(board[1][1]);
        int mineLeft = mineCount;
        while(mineLeft > 0)
        {
            int r = random.nextInt(numRows);
            int c = random.nextInt(numCols);

            MineTile tile = board[r][c];
            if(!mineList.contains(tile))
            {
                mineList.add(tile);
                mineLeft--;
            }
        }

    }

    void revealMines()
    {
        for(int i =0 ; i < mineList.size() ; i++)
        {
            MineTile tile = mineList.get(i);
            tile.setText("💣");
        }
        gameOver = true;
        textLabel.setText("Game Over");
    }
    void checkMines(int r  , int c) {
        if(r < 0 || r >= numRows || c < 0 || c >= numCols)
        {
            return ;
        }
        MineTile tile = board[r][c];
        if(!tile.isEnabled())
        {
            return;
        }
        tile.setEnabled(false);
        clickedTiles += 1;
        int minesFound = 0;
        int[] rows = {-1, -1, 0, 1, 1, 1, 0, -1};
        int[] col = {0, 1, 1, 1, 0, -1, -1, -1};
        for (int i = 0; i < 8; i++) {
            minesFound += countMines(r + rows[i], c + col[i]);
        }
        if (minesFound > 0) {
            tile.setText(Integer.toString(minesFound));
        } else
        {
            tile.setText("");
            for (int i = 0; i < 8; i++) {
                checkMines(r+rows[i] , c + col[i]);
            }
        }
        if(clickedTiles == numRows * numCols - mineList.size())
        {
            gameOver = true;
            textLabel.setText("Mines Cleared!");
        }
    }

    int countMines(int r , int c)
    {
        if(r < 0 || r >= numRows || c < 0 || c >= numCols)
        {
            return 0;
        }
        if(mineList.contains(board[r][c]))
        {
            return 1;
        }
        return 0;
    }

    public static void main(String[] args)
    {
        MineSweeper m = new MineSweeper();

    }
    }



