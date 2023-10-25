import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SpellChecker
{
    // A list for storing data fetched from txt
    private static List<String> dataDict = new ArrayList<>();

    // Reads and stores all the words provided in the dictionary.txt file in a list
    private static void populateDictionary()
    {
        try (BufferedReader dataReader = new BufferedReader(new FileReader("dictionary.txt")))
        {
            String eachLine;
            // Reading line by line for storing them into the list
            while ((eachLine = dataReader.readLine()) != null)
            {
                dataDict.add(eachLine.trim());
            }
        }
        catch (IOException ex)
        {
            System.out.println("Failed to read dictionary file.");
        }
    }

    // Edit Distance method for calculating the distance between two strings
    private static int calculateEditDistance(String str1, String str2)
    {
        int first = str1.length();
        int second = str2.length();
        int[][] dynaPro = new int[first + 1][second + 1];

        for (int firstLoop = 0; firstLoop <= first; firstLoop++)
        {
            for (int secondLoop = 0; secondLoop <= second; secondLoop++)
            {
                if (firstLoop == 0)
                {
                    dynaPro[firstLoop][secondLoop] = secondLoop;
                }
                else if (secondLoop == 0)
                {
                    dynaPro[firstLoop][secondLoop] = firstLoop;
                }
                else
                {
                    int substitutionCost = (str1.charAt(firstLoop - 1) == str2.charAt(secondLoop - 1)) ? 0 : 1;
                    dynaPro[firstLoop][secondLoop] = Math.min(Math.min(dynaPro[firstLoop - 1][secondLoop] + 1, dynaPro[firstLoop][secondLoop - 1] + 1), dynaPro[firstLoop - 1][secondLoop - 1] + substitutionCost);
                }
            }
        }
        return dynaPro[first][second];
    }

    // Find and suggest up to 3 matching words
    private static List<String> searchMatchingWords(String incomingStr, List<String> dict, int thresMaxDist, int matchesMax)
    {
        List<String> matchedWords = new ArrayList<>();
        for (String eachWord : dict)
        {
            int calculatedDist = calculateEditDistance(incomingStr, eachWord);
            if (calculatedDist <= thresMaxDist)
            {
                matchedWords.add(eachWord);
                if (matchedWords.size() >= matchesMax)
                {
                    break;
                }
            }
        }
        return matchedWords;
    }

    public static void main(String[] args)
    {
        // Populating the dictionary with words from the dictionary.txt file
        populateDictionary();

        Scanner scanner = new Scanner(System.in);

        System.out.println("███████ ██████  ███████ ██      ██           ██████ ██   ██ ███████  ██████ ██   ██ ███████ ██████      ██████  ");
        System.out.println("██      ██   ██ ██      ██      ██          ██      ██   ██ ██      ██      ██  ██  ██      ██   ██          ██ ");
        System.out.println("███████ ██████  █████   ██      ██          ██      ███████ █████   ██      █████   █████   ██████       █████  ");
        System.out.println("     ██ ██      ██      ██      ██          ██      ██   ██ ██      ██      ██  ██  ██      ██   ██     ██      ");
        System.out.println("███████ ██      ███████ ███████ ███████      ██████ ██   ██ ███████  ██████ ██   ██ ███████ ██   ██     ███████ ");
        System.out.print("\n~Welcome to the Spell Checker! Start writing to check your spelling game.~ \n\n");


        // Displaying the list of words available in the dictionary
        System.out.println("Current Words:");
        for (String eachWord : dataDict)
        {
            System.out.println("- " +eachWord);
        }
        while (true)
        {
            // Prompting the user for entering a word
            System.out.print("\nEnter a misspelled word to check its spelling (or 'exit' to quit): ");

            // Receiving user input
            String userInput = scanner.nextLine().trim().toLowerCase();

            if (userInput.equals("exit"))
            {
                System.out.print("Thanks. See you soon :)");
                break;
            }

            // This is the edit distance accuracy threshold param.
            // A higher value allows more leniency in matching.
            int thresMaxDist = 3;

            // Suggesting up to 3 matches
            int maxMatches = 3;

            if (dataDict.contains(userInput))
            {
                System.out.println("The word '" + userInput + "' is correct.\n");
            }
            else
            {
                List<String> matchedWords = searchMatchingWords(userInput, dataDict, thresMaxDist, maxMatches);

                System.out.println("The word '" + userInput + "' was not found in the dictionary.");
                if (!matchedWords.isEmpty())
                {
                    System.out.println("Suggested Corrections:");
                    for (String words : matchedWords)
                    {
                        System.out.println("  - '" + words + "'");
                    }
                }
            }
        }
        scanner.close();
    }
}
