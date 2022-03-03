import java.util.Random;
import java.util.Scanner;

public class Game {

    Scanner sc = new Scanner(System.in); // User input

    Board board = new Board();
    final char[] possibleColours = {'R', 'G', 'B', 'Y', 'O', 'C'}; // These are the colours that will be used by the game
    Guess[] allGuesses = new Guess[12];

    boolean gameOver;
    boolean playerWon;
    int playerScore = 0; // Number of games the player won
    int computerScore = 0; // Number of games the computer won
    int currentRound = 0;


    boolean coloursRepeat;


    public Game() {
        this.gameOver = false;
        this.playerWon = false;
    }

    // Displays the menu and returns the option the user chose
    int showMenu() {

        int userInput = 0;

        do {
            System.out.println("===================");
            System.out.println("1. Start a new game");
            System.out.println("2. Scoreboard");
            System.out.println("3. exit");
            System.out.println("===================");
            System.out.print("Select an option: ");

            if (sc.hasNextInt())
                userInput = sc.nextInt();
            else
                sc.next(); //This consumes the bad token (\n)

            if (!(userInput == 1 || userInput == 2 || userInput == 3))
            {
                System.out.println("Enter a NUMBER corresponding with the option you chose");
            }

        } while (!(userInput == 1 || userInput == 2 || userInput == 3));
        return userInput;
    }

    // Displays the scoreboard
    void displayScoreboard() {
        System.out.println("#####################");
        System.out.println("Player " + playerScore + " - " + computerScore + " Computer");
        System.out.println("#####################");
    }

    // Quits the game
    void exitGame() {
        System.out.println("Closing program...");
        System.exit(0);
    }

    // Sets the game options before the game would start and returns the secret answer
    char[] createSecretAnswer() {

        String input;

        do {

            System.out.print("Should colours repeat?: ");
            input = sc.next();

            switch (input.toLowerCase()) {
                case "yes" -> coloursRepeat = true;
                case "no" -> coloursRepeat = false;
                default -> System.out.println("Enter either yes or no");
            }

        } while (!input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("no"));

        char[] secretAnswer = new char[4];

        if (coloursRepeat) {
            for (int i = 0; i < 4; i++) {
                char chosenColour = (char) getRandomColour(possibleColours);
                secretAnswer[i] = chosenColour;
            }
        } else {
            boolean alreadyPresent = false; // Flags whether colour is already present

            for (int i = 0; i < 4; i++) // Adds 4 different colours to secret answer
            {
                char chosenColour = (char) getRandomColour(possibleColours); // Chooses a random colour from the possible colours array

                for (char x : secretAnswer) // Checks whether colour is already present in secret answer
                {
                    if (x == chosenColour) // If colour is present it goes back
                    {
                        alreadyPresent = true;
                        break;
                    }
                }
                if (alreadyPresent) {
                    i--; // Goes back 1 step if colour is already present
                    alreadyPresent = false;
                } else {
                    secretAnswer[i] = chosenColour; // If colour isn't present it adds it to the answer
                }
            }
        }

        return secretAnswer;
    }

    // Returns a randomly chosen colour from the array
    public static int getRandomColour(char[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    void playGame(char[] secretAnswer) {
        while (currentRound != 12 || !gameOver) {

            boolean validInput;

            currentRound++;

            String userInput;
            String tips = "";

            do {
                System.out.print("Enter a combination of 4 colours eg RGBY: ");
                userInput = sc.next().toUpperCase();

                boolean ifExists = false;

                String newString = new String(possibleColours);

                int counter = 0;

                if (userInput.length() == 4) {
                    while (counter < 4 && !ifExists) {
                        if (!newString.contains(String.valueOf(userInput.charAt(counter)))) { // Checks whether all the characters in user input are valid
                            ifExists = true;
                        }
                        counter++;
                    }
                }

                if (userInput.length() != 4) {
                    System.out.println("Enter exactly 4 colours");
                    System.out.println("Possible colours: R- Red; G - Green; B - Blue; Y - Yellow; O - Orange; C - Cyan");
                    validInput = false;
                } else if (ifExists) // Checks whether the input uses valid colours
                {
                    System.out.println("Invalid input. Choose 4 colours from the possible colours list:");
                    System.out.println("Possible colours: R- Red; G - Green; B - Blue; Y - Yellow; O - Orange; C - Cyan");
                    validInput = false;
                } else
                    validInput = true;
            } while (!validInput);

            int zeroCounter = 4; // Counter for the filler tips

            // Creates the tips if the colours don't repeat
            if (!coloursRepeat) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        if (userInput.charAt(i) == secretAnswer[j] && i == j) {
                            tips += "+";
                            break;
                        } else if (userInput.charAt(i) == secretAnswer[j] && i != j) {
                            tips += "-";
                            break;
                        } else if (j == 3) {
                            tips += "0";
                        }
                    }
                }
            } else // Creates the tips if the colours do repeat
                {
                String match = ""; // Keeps count of what colours at which position already have a tip assigned to them
                for (int i = 0; i < 4; i++) {
                    if (userInput.charAt(i) == secretAnswer[i]) {
                        tips += "+";
                        match += i;
                    } else
                        match += "e";
                }
                for (int i = 0; i < 4; i++) {
                    String charCounter = "" + i;
                    if (charCounter.charAt(0) != match.charAt(i)) {
                        for (int j = 0; j < 4; j++) {
                            if (userInput.charAt(i) == secretAnswer[j] && i != j && !(match.contains("0") && (j == 0) || match.contains("1") && (j == 1) || match.contains("2") && (j == 2) || match.contains("3") && (j == 3))) {

                                tips += "-";
                                match += j; // Takes note of the location if a symbol was assigned to the tips
                                break;

                            }
                        }
                    }
                }

                // Helps to fill out each array to 4 elements as otherwise the program would crash
                int matchCounter = 0;
                while (matchCounter < match.length()) {
                    if (match.charAt(matchCounter) == '0' || match.charAt(matchCounter) == '1' || match.charAt(matchCounter) == '2' || match.charAt(matchCounter) == '3')
                        zeroCounter--;
                    matchCounter++;
                }
            }

            // Assigns a 0 to each empty array location
            while (zeroCounter != 0) {
                tips += "0";
                zeroCounter--;
            }

            tips = sortTips(tips); // Sorts the tips

            String secretAnswerString = new String(secretAnswer); // Turns the secret answer into a string. This makes it easier to compare it to other variables

            if (userInput.equals(secretAnswerString)) {
                gameOver = true;
                playerWon = true;
                playerScore++;
            }
            if (currentRound == 12) {
                gameOver = true;
                computerScore++;
            }

            allGuesses[currentRound - 1] = new Guess(userInput, tips); // Adds the user guess to the all guesses
            board.displayBoard(secretAnswer);

            // Displays the end message
            if (userInput.equals(secretAnswerString)) {
                System.out.println("You Won!");
                break;
            }
            if (currentRound == 12) {
                System.out.println("You Lost!");
                break;
            }
        }
    }

    // Sorts the tips and then returns them
    String sortTips(String tempTips) {

        String tips = "";

        for (int i = 0; i < 4; i++) {
            if (tempTips.charAt(i) == '+') {
                tips += "+";
            }
        }
        for (int i = 0; i < 4; i++) {
            if (tempTips.charAt(i) == '-') {
                tips += "-";
            }
        }
        for (int i = 0; i < 4; i++) {
            if (tempTips.charAt(i) == '0') {
                tips += "0";
            }
        }

        return tips;
    }

    // resets certain game settings. Essentially starts a new game
    void restartGame() {
        this.currentRound = 0;
        this.allGuesses = new Guess[12];
        this.gameOver = false;
        this.playerWon = false;
    }

    public class Board {
        // Displays the board
        void displayBoard(char[] secretAnswer) {

            // Box top
            System.out.println("##############################################################################################  Player " + playerScore + "-" + computerScore + " Computer");
            for (int i = 0; i < 8; i++) // Height of guesses & tips
            {
                for (int j = 0; j < allGuesses.length; j++) // Length of guesses
                {
                    // Box Left Side
                    if (j == 0) System.out.print("|");

                    // If array is empty it won't run the print command. This makes sure that the program doesn't crash
                    if (allGuesses[j] != null && i < 4)
                        System.out.print("   " + allGuesses[j].userGuess[i] + "   ");
                    else if (i < 4)
                        System.out.print("       ");

                    // Displays tips
                    if (i > 3) {
                        if (allGuesses[j] != null && allGuesses[j].userGuessTips != null && allGuesses[j].userGuessTips[i - 4] == '0')
                            System.out.print("       ");
                        else if (allGuesses[j] != null && allGuesses[j].userGuessTips != null)
                            System.out.print("   " + allGuesses[j].userGuessTips[i - 4] + "   ");
                        else
                            System.out.print("       ");
                    }

                    // Separator line
                    if (j == allGuesses.length - 1) System.out.print("|");

                    // Secret answer
                    if (!gameOver && i > 1 && i < 6 && j == allGuesses.length - 1)
                        System.out.print("   *   |");
                    else if (gameOver && i > 1 && i < 6 && j == allGuesses.length - 1)
                        System.out.print("   " + secretAnswer[i - 2] + "   |");
                    else if (j == allGuesses.length - 1)
                        System.out.print("       |");

                    if (i == 1 && j == allGuesses.length - 1)
                        System.out.print("  Possible colours: R- Red; G - Green;");
                    if (i == 2 && j == allGuesses.length - 1)
                        System.out.print("  B - Blue; Y - Yellow; O - Orange; C - Cyan");
                    if (i == 4 && j == allGuesses.length - 1)
                        System.out.print("  - = Colour is at the wrong place");
                    if (i == 6 && j == allGuesses.length - 1)
                        System.out.print("  Current round: " + currentRound);
                }
                System.out.println();
                // Box separator line
                if (i == 3)
                    System.out.println("|------------------------------------------------------------------------------------|       |  + = Colour is at the right place");
            }
            // Box bottom
            System.out.println("##############################################################################################");
        }
    }

    // Each player guess and tip is stored in this class
    public class Guess {
        char[] userGuess = new char[4];
        char[] userGuessTips = new char[4];

        public Guess() {

        }

        public Guess(String userGuess, String userGuessTips) {
            this.userGuess = userGuess.toCharArray();
            this.userGuessTips = userGuessTips.toCharArray();
        }

        void setUserGuess(String userGuess) {
            this.userGuess = userGuess.toCharArray();
        }


        void setUserGuessTips(String userGuessTips) {
            this.userGuessTips = userGuessTips.toCharArray();
        }
    }
}