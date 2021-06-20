
/**
 * Blocky is a game where blocks are created and the user tries
 * to get as many of the same color blocks touching as possible
 * <p>
 * The game uses different calls to switch around the board as best
 * as possible to achieve the highest number of points and ends when the user
 * says "end"
 * <p>
 * Bugs: Smash method doesn't work completely right
 *
 * @Author Kross Krueger
 */

import javax.swing.*;
import java.awt.*;
import java.lang.Math;
import java.util.Scanner;


/**
 * BlockBoard class inherits JFrame and holds all of the code for
 * the program
 */
public class BlockBoard extends JFrame {

    //instantiate private variables
    private int maxDepth = 4;
    private Node root;


    /**
     *
     * Node contains the methods Node, Smash, flipLeftAndRight, flipTopAndBottom,
     * rotateClockwise and rotateCounterClockwise,
     */
    public class Node extends JPanel {

        //create a node array called child
        Node[][] child;

        //color of node
        Color color;

        //depth of node
        int depth = 0;

        /**
         * Node sets the color of the node and
         * contains the smash method
         * @param depth is the depth of the node
         */
        public Node(int depth) {

            //randomly chooses colors for the blocks
            int rand = (int) (Math.random() * 4);
            if (rand == 0) {
                color = color.RED;
            } else if (rand == 1) {
                color = color.YELLOW;
            } else if (rand == 2) {
                color = color.BLUE;
            } else color = color.GREEN;


            double rand2 = Math.random();
            double dub = Math.exp(-0.25 * depth);
            if (rand2 < dub) {
                smash(depth);
            }

            //Sets the background color of the JPanel
            setBackground(this.color);
            //Sets the border of the JPanel to black
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        }


        /**
         * Smash will split up the blocks of the game
         * @param depth is the depth of the node
         * @return
         */
        public boolean smash(int depth) {
            depth++;

            //Sets the background color of the JPanel
            setLayout(new GridLayout(2, 2));
            //Sets the border of the JPanel to black
            setBackground(Color.darkGray);

            /**
             * if the node is not at the maximum depth and instantiate the
             * child array and then instantiate each element of the array.
             */
            if (depth < maxDepth - 1) {
                child = new Node[2][2];
                for (int row = 0; row < 2; row++) {
                    for (int col = 0; col < 2; col++) {
                        child[row][col] = new Node(depth);
                        add(child[row][col]);
                    }
                }
            }

            //updates the gui as needed
            revalidate();
            return true;
        }

        //Create temporary array to hold the moves
        Node[][] temp = new Node[2][2];


        /**
         *rotateClockwise rotates the blocks clockwise
         */
        public void rotateClockwise() {

            //takes the initial Node
            temp[0][1] = child[0][0];
            temp[1][1] = child[0][1];
            temp[1][0] = child[1][1];
            temp[0][0] = child[1][0];

            //Rotates the node
            child[0][0] = temp[0][0];
            child[1][0] = temp[1][0];
            child[1][1] = temp[1][1];
            child[0][1] = temp[0][1];

            //removes all subpanels from the JPanel.
            removeAll();


            //Add the children back to the JPanel in the updated order.
            for (int r = 0; r < 2; r++) {
                for (int c = 0; c < 2; c++) {
                    add(child[r][c]);
                }
            }
            //updates the gui as needed
            revalidate();
        }

        /**
         *rotateCounterClockwise rotates the blocks counter Clockwise
         */
        public void rotateCounterClockwise() {

            //takes the initial node
            temp[0][1] = child[1][1];
            temp[1][1] = child[1][0];
            temp[1][0] = child[0][0];
            temp[0][0] = child[0][1];

            //rotates the node
            child[1][1] = temp[1][1];
            child[0][1] = temp[0][1];
            child[0][0] = temp[0][0];
            child[1][0] = temp[1][0];

            //removes all subpanels from the JPanel.
            removeAll();

            //Add the children back to the JPanel in the updated order.
            for (int r = 0; r < 2; r++) {
                for (int c = 0; c < 2; c++) {
                    add(child[r][c]);
                }
            }
            //updates the gui as needed
            revalidate();
        }

        /**
         * flipLeftAndRight flips the left and right blocks
         */
        public void flipLeftAndRight() {

            //take the initial node
            temp[1][0] = child[1][1];
            temp[1][1] = child[1][0];
            temp[0][1] = child[0][0];
            temp[0][0] = child[0][1];

            //flip the node
            child[1][1] = temp[1][1];
            child[0][1] = temp[0][1];
            child[0][0] = temp[0][0];
            child[1][0] = temp[1][0];

            //removes all subpanels from the JPanel.
            removeAll();

            //Add the children back to the JPanel in the updated order.
            for (int r = 0; r < 2; r++) {
                for (int c = 0; c < 2; c++) {
                    add(child[r][c]);
                }
            }
            //updates the gui as needed
            revalidate();
        }

        /**
         * flipTopAndBottom flips the top and bottom blocks
         */
        public void flipTopAndBottom() {

            //take the initial node
            temp[1][0] = child[0][0];
            temp[0][0] = child[1][0];
            temp[0][1] = child[1][1];
            temp[1][1] = child[0][1];

            //flip the node
            child[0][0] = temp[0][0];
            child[1][0] = temp[1][0];
            child[1][1] = temp[1][1];
            child[0][1] = temp[0][1];

            // removes all subpanels from the JPanel.
            removeAll();

            //Add the children back to the JPanel in the updated order.
            for (int r = 0; r < 2; r++) {
                for (int c = 0; c < 2; c++) {
                    add(child[r][c]);
                }
            }
            //updates the gui as needed
            revalidate();
        }
    }

    /**
     * Constructor for BlockBoard
     */
    public BlockBoard() {
        root = new Node(2);
        add(root);
    }


    /**
     * Start method prints out the start menu
     * and has the code of when each move is entered
     */
    public void start() {

        //Possible Moves
        System.out.println("Move List: ");
        System.out.println("flr: Flip Left & Right");
        System.out.println("ftb: Flip top & Bottom");
        System.out.println("rc: Rotate Clockwise");
        System.out.println("rcc: Rotate Counter Clockwise");
        System.out.println("s: smash");
        System.out.println("end: end the game but do not exit");
        System.out.println("For a Specific location, enter the coordinates next to");
        System.out.println("the command with spaces in between.");
        System.out.println("Ex: rcc 01 01");
        System.out.println();
        System.out.println("Enter a move: ");

        /*
        Creates Scanner and gets input from user
         */
        Scanner input = new Scanner(System.in);
        String next = input.nextLine();

        Node current;

        Scanner string = new Scanner(next);
        String hold = string.next();

        //while end isnt called
        while (!hold.equals("end")) {

            current = root;
            while (string.hasNextInt()) {
                int r = string.nextInt();
                int c = string.nextInt();
                current = current.child[r][c];
            }

            if (hold.equals("ftb")) {    //if Flip top and bottom is called
                current.flipTopAndBottom();

            } else if (hold.equals("flr")) {    //if Flip left and right is called
                current.flipLeftAndRight();

            } else if (hold.equals("rc")) {    //if Rotate clockwise is called
                current.rotateClockwise();
            } else if (hold.equals("rcc")) {    //if Rotate Counterclockwise is called
                current.rotateCounterClockwise();

            } else if (hold.equals("s")) {      //if smash is called
                if (root.depth < maxDepth) {
                    current.smash(current.depth);
                } else
                    System.out.println("Cannot smash any further.");

            } else if (hold.equals("end")) {    //if end is called
                System.out.println("Ending the game.");
                break;

            } else {    //if something other than a command is called
                System.out.println("Error: Not a command, try again.");
            }

            //will get another move
            System.out.println("Enter another move: ");
            next = input.nextLine();
            string = new Scanner(next);
            hold = string.next();

        }


    }


    /**
     * Main method creates the puzzle and starts the game
     * given to use
     *
     * @param args
     */
    public static void main(String[] args) {
        BlockBoard puzzle = new BlockBoard();
        puzzle.setSize(500, 500);
        puzzle.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        puzzle.setVisible(true);
        puzzle.start();
    }
}






