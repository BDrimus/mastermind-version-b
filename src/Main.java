import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        boolean leaveMenu = true; // Leaves the option selector loop if it's true
        boolean playAgain = false; // Displays the menu again if it's true

        Game game = new Game();

        do { // Keeps looping the game until the user ends it
            do { // Stays in the menu until leaveMenu is true

                int userInput = game.showMenu();

                // Option selector for the menu
                switch (userInput) {
                    case 1:
                        char[] secretAnswer = game.createSecretAnswer(); // sets the secret answer to the colours that were generated

                        game.board.displayBoard(secretAnswer);
                        game.playGame(secretAnswer);

                        leaveMenu = true;
                        break;
                    case 2:
                        game.displayScoreboard();
                        leaveMenu = false;
                        break;
                    case 3:
                        game.exitGame();
                        break;
                    default:
                        break;
                }
            } while (!leaveMenu);

            String userInputPlayAgain; // User input

            do { // Stays in the loop until user enters either yes or no

                System.out.print("Would you like to play again?: ");
                userInputPlayAgain = sc.next();

                switch (userInputPlayAgain.toLowerCase()) {
                    case "yes" -> playAgain = true;
                    case "no" -> playAgain = false;
                    default -> System.out.println("Enter either yes or no");
                }

            } while (!userInputPlayAgain.equalsIgnoreCase("yes") && !userInputPlayAgain.equalsIgnoreCase("no"));

            game.restartGame();

        } while (playAgain);

        game.exitGame();
    }
}

