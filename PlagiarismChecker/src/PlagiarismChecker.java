import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;


/**
 * PlagiarismChecker is a Java program to check source
 * code for possible plagiarism.
 * It has 3 methods and a main
 * <p>
 * Author:Kross Krueger
 * <p>
 * bugs: none
 */
public class PlagiarismChecker {

    /**
     * lcsLength calculates the lcs_length of the two programs.
     * This method should take the contents of the two programs as parameters
     *It is similar to the one from the book with minor tweaks
     *
     * @param prog1Contents is the first files contents
     * @param prog2Contents is the second files contents
     * @return
     */
    public int lcsLength(String prog1Contents, String prog2Contents) {

        //creates a 2d array called array that is the length of both
        //parameters plus one
        int[][] array = new int[prog1Contents.length() + 1][prog2Contents.length() + 1];

        //nested for loop goes through the 2d array
        for (int i = 1; i < array.length; i++) {
            for (int j = 1; j < array[0].length; j++) {

                //if statement checks to see if the characters are the same
                if (prog1Contents.charAt(i - 1) == prog2Contents.charAt(j - 1)) {
                    array[i][j] = 1 + array[i - 1][j - 1];
                }
                //else if will get the max of the array column and row
                else {
                    array[i][j] = Math.max(array[i - 1][j], array[i][j - 1]);
                }
            }
        }
        //then returns te length of the array
        return array[prog1Contents.length()][prog2Contents.length()];
    }


    /**
     * PlagiarismScore should use lcsLength() to calculate the plagiarism score for the two files.
     * Most of this was given from the RandomAccesRead file
     *
     * @param filename1 is the first filename from the reader
     * @param filename2 is the second filename from the reader
     * @return
     * @throws IOException
     */
    public double plagiarismScore(String filename1, String filename2) throws IOException {

        //two RandomAccessFiles are created for the two filenames from the parameters
        RandomAccessFile firstFile = new RandomAccessFile(new File(filename1), "r");
        RandomAccessFile firstFile2 = new RandomAccessFile(new File(filename2), "r");

        //read in the values and initiate the strings
        int value = firstFile.read();
        String file1String = "";
        int value2 = firstFile2.read();
        String file2String = "";

        //two while loops that load in each char value into the fileStrings and reads them
        while (value != -1) {
            file1String = file1String + (char) value;
            value = firstFile.read();
        }
        while (value2 != -1) {
            file2String = file2String + (char) value2;
            value2 = firstFile2.read();
        }

        //file1Length and file2Length are just the length of the fileStrings
        int file1Length = file1String.length();
        int file2Length = file2String.length();

        //lcs is the variable that calls the lcsLength method to use the fileStrings
        int lcs = lcsLength(file1String, file2String);

        //returns the double of 200* lcs/(file1Length + file2Length)
        return (double) 200 * lcs / (file1Length + file2Length);

    }

    /**
     * plagiarismChecker should do a pairwise comparison of all the files in the
     * array and print a neatly formatted report listing any suspicious pairs.
     * The report should include their plagiarism score
     *
     * @param filenames are the String array filenames
     * @param threshold is the percentage that the files are checked against each other for
     *                  plagiarism
     * @throws IOException
     */
    public void plagiarismChecker(String[] filenames, double threshold) throws IOException {
        //check is a double that will hold the score of the plaigarismChecker
        double check;

        //nested for loop goes through the length of the filenames
        for (int i = 0; i < filenames.length; i++)
            //this for loop has k which is the index after i
            for (int k = i + 1; k < filenames.length; k++) {
                //check holds the score of the filenames being checked
                check = plagiarismScore(filenames[k - 1], filenames[k]);
                //if statement checks the files vs the threshold and prints if its greater
                //than the threshold
                if (check >= threshold)
                    System.out.println("" + filenames[i] + "               " + filenames[k] + "    " + plagiarismScore(filenames[i], filenames[k]));
            }
    }

    /**
     * main method creates a PlagiarismChecker variable and asks the user
     * for the filenames and puts them in an array and runs the program
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        //Scanner keybd is created
        Scanner keybd = new Scanner(System.in);
        //new PlagiarismChecker is made
        PlagiarismChecker checker = new PlagiarismChecker();
        System.out.println("Enter files:");
        //string array is created to hold the filenames
        String[] array = new String[8];
        //for loop initializes the array with the users input
        for (int i = 0; i < array.length; i++) {
            array[i] = keybd.next();
        }
        //prints the output
        System.out.println("File 1      File 2     Score");
        //starts the program
        checker.plagiarismChecker(array, 30);


    }

}
