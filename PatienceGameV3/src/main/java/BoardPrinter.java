import java.util.*;

//This class contains the Board interactive methods like - initializing the board and print the board
public class BoardPrinter {

    // This was an earlier version of the printGame method, where the lanes were displayed horizontally. However, playing the game this way proved challenging, as the horizontal layout led to confusion and disorientation.
    // THIS METHOD IS NOT USED IN THE GAME
    public static void printGameHorizontal(List<Stack<Card>> lanes, Deque<Card> drawPile) {
        System.out.println("=======================================Patience Game========================================");
        System.out.println("Score: " + Game.score + " | Moves: " + Game.movesCount);
        System.out.println();

        System.out.println("------------------Foundation------------------");
        for (String suit : Game.TYPE) {
            System.out.print(suit + ": ");
            if (!Game.foundationPiles.get(suit).isEmpty()) {
                System.out.println(Game.foundationPiles.get(suit).peek().printCard());
            } else {
                System.out.println("[empty]");
            }
        }

        System.out.println("-----------------Draw Pile-----------------");
        if (Game.drawnCard != null && Game.drawnCard.isFaceUp) {
            System.out.println("+-------+");
            System.out.println("|  " + Game.drawnCard.printCard() + "   |");
            System.out.println("+-------+");
        } else {
            System.out.println("+-------+");
            System.out.println("|   **  |");
            System.out.println("+-------+");
        }

        System.out.println("-------------------Lanes-------------------");
        for (int i = 0; i < 7; i++) {
            System.out.print("P" + (i + 1) + ": ");
            for (int j = 0; j < lanes.get(i).size(); j++) {
                System.out.print(lanes.get(i).get(j).printCard() + " ");
            }
            System.out.println();
        }
        System.out.println("============================================================================================");
    }

    // This is the updated version of the printGame method, which aligns the lanes horizontally to improve gameplay clarity and ease.
    public static void printGameVertical(List<Stack<Card>> lanes, Map<String, Stack<Card>> foundationPiles) {
        System.out.println("====================================Patience Game====================================");
        System.out.println("Score: " + Game.score + " | Moves: " + Game.movesCount);
        System.out.println();
        System.out.println("---------------------Foundation---------------------");
        for (String suit : Game.TYPE) {
            System.out.print(suit + ": ");
            if (!foundationPiles.get(suit).isEmpty()){
                System.out.println(foundationPiles.get(suit).peek().printCard());
            } else {
                System.out.println("[empty]");
            }
        }
        System.out.println("---------------------Draw Pile---------------------");

        if (Game.drawnCard != null && Game.drawnCard.isFaceUp){
            System.out.println("+-------+");
            System.out.println("|  " + Game.drawnCard.printCard() + "   |");
            System.out.println("+-------+");
        } else {
            System.out.println("+-------+");
            System.out.println("|   **  |");
            System.out.println("+-------+");
        }

        System.out.println("-----------------------Lanes-----------------------");
        // to print the lanes vertically, we need to first figure out the row count
        // that is not a static value as the verticality will change based on the amount of cards available in a lane
        //so first we count the max size based on the lanes, max values
        int maxLaneSize = 0;
        for (int i = 0; i < 7; i++) {
            maxLaneSize = Math.max(maxLaneSize, lanes.get(i).size());
        }

        for (int i = 1; i <= 7; i++) {
            System.out.print("P" + i + "\t");
        }
        System.out.println();
        System.out.println("---------------------------------------------------");
        for (int row = 0; row < maxLaneSize; row++) {
            for (int lane = 0; lane < 7; lane++) {
                if (row < lanes.get(lane).size()){
                    System.out.print(lanes.get(lane).get(row).printCard() + "\t");
                } else {
                    System.out.print("\t");
                }
            }
            System.out.println();
        }
        System.out.println("=====================================================================================");
    }


}
