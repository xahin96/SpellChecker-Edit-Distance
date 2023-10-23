import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SpellChecker {

    private static List<String> dictionary = new ArrayList<>();

    // Load words from a text file and populate the dictionary
    private static void populateDictionary() {
        try (BufferedReader reader = new BufferedReader(new FileReader("dictionary.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                dictionary.add(line.trim());
            }
        } catch (IOException e) {
            System.out.println("Failed to read dictionary file.");
        }
    }

    // Calculate the Edit Distance between two strings
    private static int calculateEditDistance(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int substitutionCost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + substitutionCost);
                }
            }
        }
        return dp[m][n];
    }

    // Find and suggest up to 3 matching words
    private static List<String> findClosestMatchingWords(String input, List<String> dictionary, int maxDistanceThreshold, int maxMatches) {
        List<String> matchedWords = new ArrayList<>();
        for (String word : dictionary) {
            int distance = calculateEditDistance(input, word);
            if (distance <= maxDistanceThreshold) {
                matchedWords.add(word);
                if (matchedWords.size() >= maxMatches) {
                    break;
                }
            }
        }
        return matchedWords;
    }

    public static void main(String[] args) {
        // Populating the dictionary with words from the dictionary.txt file
        populateDictionary();

        Scanner scanner = new Scanner(System.in);

        System.out.println("   _____            ____   ________              __            ");
        System.out.println("  / ___/____  ___  / / /  / ____/ /_  ___  _____/ /_____  _____");
        System.out.println("  \\__ \\/ __ \\/ _ \\/ / /  / /   / __ \\/ _ \\/ ___/ //_/ _ \\/ ___/");
        System.out.println(" ___/ / /_/ /  __/ / /  / /___/ / / /  __/ /__/ ,< /  __/ /    ");
        System.out.println("/____/ .___/\\___/_/_/   \\____/_/ /_/\\___/\\___/_/|_|\\___/_/     ");
        System.out.println("    /_/                                                        ");
        System.out.print("\n~Welcome to the Spell Checker! Start writing to check your spelling game.~ \n\n");

        System.out.println("Words in the dictionary:");
        for (String word : dictionary) {
            System.out.println(word);
        }
        while (true) {
            System.out.print("Enter a word to check its spelling (or 'exit' to quit): ");

            // Receiving user input
            String userInput = scanner.nextLine().trim().toLowerCase();

            if (userInput.equals("exit")) {
                System.out.print("Thank you for using Spell Checker! Goodbye :)");
                break;
            }

            // Edit distance accuracy threshold parameter.
            // The higher the number, the more accurate match is being searched
            int maxDistanceThreshold = 3;

            // Suggest up to 3 matches
            int maxMatches = 3;

            if (dictionary.contains(userInput)) {
                System.out.println("Input '" + userInput + "' is correct.\n");
            } else {
                List<String> matchedWords = findClosestMatchingWords(userInput, dictionary, maxDistanceThreshold, maxMatches);

                System.out.println("Input '" + userInput + "' is not in the dictionary.");
                if (!matchedWords.isEmpty()) {
                    System.out.println("Did you mean:");
                    for (String words : matchedWords) {
                        System.out.println("  - '" + words + "'");
                    }
                }
            }
        }
        scanner.close();
    }
}
