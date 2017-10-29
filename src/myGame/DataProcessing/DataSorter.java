package myGame.DataProcessing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Created by liamkreiss on 10/28/17.
 */
public class DataSorter {

    public static void main(String[] args) throws FileNotFoundException {
        String[] filesToProcess = new String[] {
            //TODO
        };
        ArrayList<String> allGames = new ArrayList<>();
        for (String file : filesToProcess) {
            Scanner curFile = new Scanner(new File(file));
            while (curFile.hasNextLine()) {
                String nextLine = curFile.nextLine();
                allGames.add(nextLine);
            }
        }

        Collections.sort(allGames);

        File outputFile = new File("sorted_data" + System.currentTimeMillis());
        PrintStream output = new PrintStream(outputFile);

        for (String s : allGames) {
            output.println(s);
        }
    }
}
