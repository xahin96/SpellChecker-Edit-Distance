import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SpellChecker
{
    // A list to store the words in the dictionary
    private static List<String> dictionary = new ArrayList<>();

    // Reads and stores all the words provided in the dictionary.txt file in a list
    private static void populateDictionary()
    {
        try (BufferedReader reader = new BufferedReader(new FileReader("dictionary.txt")))
        {
            String line;
            // Read words from the file and add them to the dictionary list
            while ((line = reader.readLine()) != null)
            {
                dictionary.add(line.trim());
            }
        }
        catch (IOException e)
        {
            System.out.println("Failed to read dictionary file.");
        }
    }

    // Edit Distance method for calculating the distance between two strings
    private static int calculateEditDistance(String s1, String s2)
    {
        int m = s1.length();
        int n = s2.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++)
        {
            for (int j = 0; j <= n; j++)
            {
                if (i == 0)
                {
                    dp[i][j] = j;
                }
                else if (j == 0)
                {
                    dp[i][j] = i;
                }
                else
                {
                    int substitutionCost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + substitutionCost);
                }
            }
        }
        return dp[m][n];
    }

    // Find and suggest up to 3 matching words
    private static List<String> findClosestMatchingWords(String input, List<String> dictionary, int maxDistanceThreshold, int maxMatches)
    {
        List<String> matchedWords = new ArrayList<>();
        for (String word : dictionary)
        {
            int distance = calculateEditDistance(input, word);
            if (distance <= maxDistanceThreshold)
            {
                matchedWords.add(word);
                if (matchedWords.size() >= maxMatches)
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

        // Display a welcome message
        System.out.println("   _____            ____   ________              __            ");
        System.out.println("  / ___/____  ___  / / /  / ____/ /_  ___  _____/ /_____  _____");
        System.out.println("  \\__ \\/ __ \\/ _ \\/ / /  / /   / __ \\/ _ \\/ ___/ //_/ _ \\/ ___/");
        System.out.println(" ___/ / /_/ /  __/ / /  / /___/ / / /  __/ /__/ ,< /  __/ /    ");
        System.out.println("/____/ .___/\\___/_/_/   \\____/_/ /_/\\___/\\___/_/|_|\\___/_/     ");
        System.out.println("    /_/                                                        ");
        System.out.print("~Welcome to the Spell Checker! Start writing to check your spelling game.~ \n\n");

        // Displaying the list of words available in the dictionary
        System.out.println("Words available in the dictionary:");
        for (String word : dictionary)
        {
            System.out.println("- " +word);
        }
        while (true)
        {
            // Prompting the user for entering a word
            System.out.print("\nEnter a misspelled word to check its spelling (or 'exit' to quit): ");

            // Receiving user input
            String userInput = scanner.nextLine().trim().toLowerCase();

            if (userInput.equals("exit"))
            {
                System.out.print("Thank you for using Spell Checker! Goodbye :)");
                break;
            }

            // Edit distance accuracy threshold parameter.
            // The higher the number, the more accurate match is being searched
            int maxDistanceThreshold = 3;

            // Suggesting up to 3 matches
            int maxMatches = 3;

            if (dictionary.contains(userInput))
            {
                System.out.println("The word '" + userInput + "' is correct.\n");
            }
            else
            {
                List<String> matchedWords = findClosestMatchingWords(userInput, dictionary, maxDistanceThreshold, maxMatches);

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
