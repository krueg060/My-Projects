import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

/**
 * PageRank is an algorithm that assigns every web page a
 * numeric value, or rank depending on a key that the user
 * gives to it. The search results are sorted in descending
 * order based on the rank, and the list is returned.
 * This program will run this algorithm.
 *
 * @author Kross Krueger
 * 04-23-2020
 * <p>
 * bugs: none
 */
public class PageRank {

    /**
     * Creating many HashMaps for the page to be stored, the link,
     * the incoming link, the initial rankings, the temporary rankings to hold before it finishing checking
     * all the links, the text of the pages and an arrayList for the names of the page.
     */
    private HashMap<String, HashSet<String>> page = new HashMap();
    private HashMap<String, Integer> inLink = new HashMap<>();
    private ArrayList<String> pageNames = new ArrayList();
    private HashMap<String, ArrayList> link = new HashMap();
    private HashMap<String, Double> ranking = new HashMap<>();
    private HashMap<Double, String> rank2 = new HashMap<>();
    private HashMap<String, Double> tempRanking = new HashMap<>();
    private HashSet<String> pageText = new HashSet();


    /**
     * Ranker takes in a keyword and runs it through the webpages and ranks
     * each page depending on how relevant it is.
     *
     * @param keyWord is the keyword from the user
     */
    private void ranker(String keyWord) {
        try {
            //Creating the file and the Scanner
            String fileName = "CS.txt";
            File file = new File(fileName);
            Scanner keybd = new Scanner(file);

            //Files start with "Page" so we know when to start
            String nextLine = keybd.nextLine();

            //First line after "PAGE" is always the first file name
            String pageName = keybd.nextLine().toLowerCase();

            //Add the first pageName to the array of pageNames
            pageNames.add(pageName);

            //Create new array list for outgoing links called outLink
            ArrayList outLink = new ArrayList();

            /**
             * While loop that scans through the file
             */
            //hits is the number of hits on the webpage
            int hits = 0;
            while (keybd.hasNext()) {

                //Gets the next line of the file.
                nextLine = keybd.nextLine().toLowerCase();


                // newPage is a new string that will be our keyword to show
                // that the next line will be a new page
                String newPage = "page";

                /**
                 * If nextLine is equal to our keyword "newPage", we add
                 * the text to the last page and add it to the page name.
                 * also add the list of outLinks to the link map, along with the key for the page,
                 * and set pageName to be equal to the nextLine, we add that page to our pageNames list.
                 *
                 * Finally clear our text by reinstantiating it,
                 * and we clear our outLink list by reinstantiating it as well.
                 */
                if (nextLine.equals(newPage)) {
                    page.put(pageName, pageText);
                    link.put(pageName, outLink);
                    pageName = keybd.nextLine().toLowerCase();
                    pageNames.add(pageName);

                    //We have to clear the pageText and the outgoing link to
                    //bring in new ones.
                    pageText = new HashSet();
                    outLink = new ArrayList();
                }

                /**
                 * Else if checks to see if the next line contains http, which is the beginning of our link.
                 * if so, we add that link to the list of outLinks.
                 * And we also add it to the list of inLinks for each page.
                 */
                else if (nextLine.contains("http")) {
                    outLink.add(nextLine);
                    if (inLink.get(nextLine) != null)
                        inLink.put(nextLine, inLink.get(nextLine) + 1);
                    else
                        inLink.put(nextLine, 1);
                }
                /**
                 * Else take the text scanning in, and split it into an array, and add that array
                 * to pageText.
                 */
                else {
                    pageText.add(nextLine);
                    if (nextLine.contains(keyWord)) {
                        hits++;
                    }
                }
            }

            //Add final pages pageText to hashMap with the pageName as Key
            page.put(pageName, pageText);

            //Add final pages outLink links to hashMap with pageName as Key
            link.put(pageName, outLink);

            /**
             * while loops goes through all of our pages and gives them the initial rank of 1/# of pages.
             */
            int size = page.size();
            double initialRank = 1.0 / size;
            int pageAdd = 0;

            //Adds Initial Ranks
            while (pageAdd < size) {
                ranking.put(pageNames.get(pageAdd), initialRank);
                pageAdd++;
            }

            //For loop with an if-statement to ensure that none of the values are empty.
            // If there are no incoming links for the page it defaults to 1.
            for (int i = 0; i < page.size(); i++) {
                String page = pageNames.get(i);
                if (!inLink.containsKey(page))
                    inLink.put(page, 1);
            }

            /**
             *
             * This while loop reads each page and checks what page links to it.
             * When it finds a page that links to it, the loop does math that adds
             * the rank of that page, divided by how many incoming links it has to our
             * x variable. then it sets rank equal
             * to the rank + d * x. Which gives us our end rank.
             * Then add that rank to our temporary rank map until all of the pages
             * have been gone through; After the last page, we add all of those temporary
             * ranks to our main ranks.
             *
             * Runs this 10 times to get a stabilized rank that wont change.
             */
            int counter = 0;
            while (counter < 10) {
                for (int i = 0; i < size; i++) {
                    String page = pageNames.get(i);
                    double d = 0.15;
                    double rank = (1 - d) / size;
                    double x = 0;

                    //For loop does (PR(A)/2 + PR(B)/1 + PR(D)/1)
                    for (String key : pageNames) {
                        ArrayList aList = link.get(key);

                        if (aList.contains(page)) {
                            double divide = ranking.get(key) / link.get(key).size();
                            x = x + (divide);
                        }
                    }
                    rank = rank + (d * x);
                    tempRanking.put(page, rank);
                }

                // for loop gets the page names and puts them in
                // the temporary rankings
                for (int i = 0; i < size; i++) {
                    String page = pageNames.get(i);
                    ranking.put(page, tempRanking.get(page));
                    rank2.put(tempRanking.get(page), page);
                }
                //Go through the ranking a few times to make sure they are stabilized
                counter++;
            }

            System.out.println("The number of hits was: " + hits);

            //arr is a double array to hold all the ranks of our pages
            //it then gets instantiated with the ranks
            Double[] arr = new Double[size];
            for (int i = 0; i < size; i++) {
                arr[i] = ranking.get(pageNames.get(i));
            }
            //sorts that double array(lowest to highest)
            Arrays.sort(arr);

            //reverse stores the values of our first array but in reverse
            //so that it will be from highest to lowest
            Double[] reverse = new Double[size];
            for (int i = size - 1, k = 0; i >= 0; i--, k++) {
                reverse[k] = arr[i];
            }
            //firstForm variable formats our doubles out to 7 decimal places
            DecimalFormat firstForm = new DecimalFormat("#.#######");

            //Check to see if there are more than 20 pages that contain the keyword.
            //If so, we display a message that we will only display the top 20 pages.
            if (hits > 20) {
                System.out.println("Only displaying top 20 hits: ");
            }
            //For loop displays rank of the page with the name of it.
            //But only for pages that have hits for the keywords.
            for (int i = 0; i < 20 && i < hits; i++) {

                //double variable created to make it smoother to read for a person
                double format = reverse[i];
                System.out.println(firstForm.format(format) + " " + rank2.get(reverse[i]));
            }

        }//catches exception
        catch (Exception e) {
            System.out.println("An Exception occurred. " + e);
        }
    }

    /**
     * Main creates a Scanner and asks the user
     * to enter a keyword. Then it takes the keyword and runs it though the
     * program.
     *
     * @param args
     */
    public static void main(String[] args) {
        Scanner keybd = new Scanner(System.in);
        PageRank pageRank = new PageRank();
        System.out.println("Enter your search terms:");
        String key = keybd.next();
        pageRank.ranker(key.toLowerCase());
    }
}

