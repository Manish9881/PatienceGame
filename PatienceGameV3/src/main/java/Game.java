import java.util.*;

// All the major operations/checks for the game are added in this class

public class Game {

    // Constants used to define the cards
    static final String[] TYPE = {"H", "D", "C", "S"};
    static final String[] CARDVALUE = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

    //All the below variables are initialized in Board class
    static List<Stack<Card>> lanes = new ArrayList<>();                     //P1-P7 lanes stored here
    static Deque<Card> drawPile = new ArrayDeque<>();                       //Double ended queue to store the uncovered pile of cards
    static Map<String, Stack<Card>> foundationPiles = new HashMap<>();      //Foundation cards map - Created a map for ease of access

    //Variables used to store the state of different aspects of the game
    static Card drawnCard;                                          //Used to store the card to be shown to the user from the drawPile
    static int score = 0;                                           //Used to store the Player score
    static int movesCount = 0;                                      //Used to store the number of moves used by player
    static List<Card> drawnCardList = new ArrayList<>();            //Variable to track which cards are drawn till the current moment from the DeQue



    //Method responsible for handling the user inputs
    public static void handleUserInput() {
        Scanner sc = new Scanner(System.in);
        boolean gameOn = true;

        //Game Rules -
        System.out.println("=========================================================================================================");
        System.out.println("Game Rules:");
        System.out.println("- Foundation Piles are denoted by: \"H\" (Heart), \"D\" (Diamond), \"C\" (Club), \"S\" (Spade)");
        System.out.println("- Lanes are denoted by: \"P1\", \"P2\", \"P3\", \"P4\", \"P5\", \"P6\", \"P7\"");
        System.out.println("- Draw pile is denoted by: \"B\"");
        System.out.println();
        System.out.println("SCORING:");
        System.out.println("- Draw pile(B) to Foundation piles(H,S,D,C) - 10 points");
        System.out.println("- Any of the lanes(P1-P7) to Foundation piles(H,S,D,C) - 10 points");
        System.out.println("- Card movement between lanes - (5 * number of cards moved) points");
        System.out.println();
        System.out.println("MOVES:");
        System.out.println("- To uncover a card from the draw pile, use: \"B\"");
        System.out.println("- To move a card: <from><space><to><space><number_of_cards> (use space or the move will be invalid!)(number of cards is optional)");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("- From draw pile to Foundation pile: \"B H\"");
        System.out.println("- From draw pile to a lane: \"B P1\"");
        System.out.println("- From one lane to another: \"P4 P1\" (moves the top card from P4 to P1)");
        System.out.println("- From one lane to another (multiple cards): \"P3 P1 4\" (moves the top 4 cards from P3 to P1)");
        System.out.println("- From a lane to a Foundation pile: \"P1 C\"");
        System.out.println("=========================================================================================================");

        //Game Input -
        while (gameOn) {
            BoardPrinter.printGameVertical(lanes, foundationPiles);
            System.out.println("Enter command (B to draw, Q to quit, or a move command) : ");
            String command = sc.nextLine().toUpperCase().trim();

            switch (command) {
                case "B":
                    drawCard();
                    break;
                case "Q":
                    gameOn = false;
                    System.out.println("Thanks for playing!");
                    System.out.println("Your final Score: " + score + " | Moves: " + movesCount);
                    break;
                default:
                    if (isMoveCommand(command)) {
                        processMove(command);

                        if (checkIfGameEnded()) {
                            System.out.println("===============================================-GAME OVER!-===============================================");
                            System.out.println("Congratulations! You have successfully moved all the cards to the foundation piles!");
                            System.out.println("Your final Score: " + score + " | Total Moves: " + movesCount);
                            gameOn = false;
                        }
                    } else {
                        System.out.println("Invalid command. Please try again.");
                    }
                    break;
            }
        }
    }

    //Method to check if the user input is a valid move command
    //4 things checked using pattern matching
    // 1 -> Pile to Pile with number of cards
    // 2 -> Pile to Foundation Pile
    // 3 -> Draw Pile to normal piles
    // 4 -> Draw Pile to foundation piles
    public static boolean isMoveCommand(String command) {
        return command.matches("^(P[1-7] P[1-7]( \\d+)?)$|^(P[1-7] [HDSC])$|^(B P[1-7])$|^(B [HDSC])$");
    }

    //main brain function responsible to process the commands
    //this method references all the validation methods to process the move
    public static void processMove(String command) {
        String[] parts = command.split(" ");
        String from = parts[0];
        String to = parts[1];
        int numCards = (parts.length == 3) ? Integer.parseInt(parts[2]) : 1;

        Stack<Card> fromStack = getStackByCode(from);
        Stack<Card> toStack = getStackByCode(to);

        int scoreBeforeMove = calculateBoardScore();
        int scoreAfterMove = 0 ;

        //earlier iteration required this check as the isMoveCommand did not filter all the valid moves
        //this method is not used but kept as an additional check to make sure returned stack is not null
        if (fromStack == null || toStack == null) {
            System.out.println("Invalid move. Please try again.");
            return;
        }

        if (fromStack.size() < numCards) {
            System.out.println("Not enough cards in the selected pile to move.");
            return;
        }

        Stack<Card> tempStack = new Stack<>();
        for (int i = 0; i < numCards; i++) {
            tempStack.push(fromStack.pop());
        }

        //When moving multiple cards, validation should be performed based on the first card, which will be placed adjacent to the card in the destination lane.
        Card cardToMove = tempStack.peek();

        if (isValidMove(cardToMove, toStack,to)) {
            while (!tempStack.isEmpty()) {
                toStack.push(tempStack.pop());
            }
            System.out.println("Moved " + numCards + " card(s) from " + from + " to " + to);
            //after successful move, if the from stack is not empty -> uncover the top card
            if (!fromStack.isEmpty() && !fromStack.peek().isFaceUp){
                fromStack.peek().flip();
            }

            //after a successful move from Drawpile, make the current top card variable as null
            //and only uncover the last drawn card if it has been drawn before
            if (from.equals("B")){
                if (!drawPile.isEmpty()){
                    //System.out.println(drawnCard);
                    if (drawnCardList.contains(drawPile.getLast())){
                        drawnCard = drawPile.removeLast(); // hardest bug
                        drawnCard.flip();
                    } else {
                        drawnCard = null;
                    }

                }
                //last card edge case where the draw pile should be empty
                else {
                    drawnCard = null;
                }
            }

            scoreAfterMove = calculateBoardScore();

            if(scoreBeforeMove!=scoreAfterMove || command.matches("^(B [HDSC])$")){
                updateScoreAndMoves(from,to,numCards);
            }else{
                movesCount++;
            }
        }
        //if the move is invalid, re add the popped elements from the major stacks back to them
        else {
            while (!tempStack.isEmpty()) {
                fromStack.push(tempStack.pop());
            }
            System.out.println("Invalid move. You cannot place " + cardToMove.printCard() + " on " + to);
        }
    }


    //method introduced to check the state of the game before and after the move.
    //This method is used to validate the moves if they are redundant
    //like oscillating the cards between 2 lanes to gain more score
    private static int calculateBoardScore() {
        int score = 0;
        for (Stack<Card> lane : lanes) {
            if (lane.isEmpty()) {
                score += 1;
            }
            for (Card card : lane) {
                if (!card.isFaceUp) {
                    score += 2;
                } else {
                    int cardValueIndex = getCardValueIndex(card.cardValue);
                    score += cardValueIndex + 20 + (20*cardValueIndex);
                }
            }
        }
        return score;
    }

    //method responsible to update the score and moves
    //only called if the move is successfully completed in processMove method
    private static void updateScoreAndMoves(String from, String to, int numCards) {
        movesCount++;
        //System.out.println("Inside update score");
        if (foundationPiles.containsKey(to)) {
            if (from.equals("B")) {
                score += 10;
            } else {
                score += 20;
            }
        } else {
            if(!from.equals("B")){
                score += (5*numCards);
            }
        }
    }

    //Method to return the corresponding stack based on the move command input
    //used in processMove method to get the corresponding from and to stacks for the move
    //As drawPile is handled in a deque, we use a temporary stack instead to add only the top card
    //As only one card will be moved at a time from drawpile, the logic in processMove is not hindered
    public static Stack<Card> getStackByCode(String code) {
        if (code.matches("P[1-7]")) {
            int laneIndex = Integer.parseInt(code.substring(1)) - 1;
            return lanes.get(laneIndex);
        } else if (foundationPiles.containsKey(code)) {
            return foundationPiles.get(code);
        } else if (code.equals("B") && drawnCard != null) {
            Stack<Card> tempStack = new Stack<>();
            tempStack.push(drawnCard);
            return tempStack;
        }
        return null;
    }

    //method responsible to check if the intended card/s can be moved to the destination
    //all level of checks for each pile is performed here
    public static boolean isValidMove(Card card, Stack<Card> targetStack, String to) {
        //for empty stack only 2 scenarios -
        //if foundation pile is the destination -> only aces can be added to the corresponding pile
        //if normal lanes is the destination -> only kings can be placed in empty lanes
        if (targetStack.isEmpty()) {
            if (foundationPiles.containsKey(to)) {
                Stack<Card> foundationStack = foundationPiles.get(card.type);
                if (foundationStack == targetStack) {
                    return card.cardValue.equals("A");
                } else {
                    return false;
                }
            } else {
                return card.cardValue.equals("K");
            }
        }
        //for nonempty stack also 2 cases ->
        //for foundation pile -> same type of cards in ascending order
        //for lanes -> different type of cards with different colour in descending order
        else {
            Card targetCard = targetStack.peek();
            if (foundationPiles.containsValue(targetStack)) {
                return card.type.equals(targetCard.type) && isNextInAscendingOrder(targetCard, card);
            } else {
                boolean isDifferentColor = (isRed(card.type) && !isRed(targetCard.type)) || (!isRed(card.type) && isRed(targetCard.type));
                return isDifferentColor && isNextInDescendingOrder(targetCard, card);
            }
        }
    }

    //checks if the card is red (Hearts and Diamonds)
    public static boolean isRed(String type) {
        return type.equals("H") || type.equals("D");
    }

    //check if the card is in descending order.
    public static boolean isNextInDescendingOrder(Card topCard, Card cardToMove) {
        return getCardValueIndex(topCard.cardValue) == getCardValueIndex(cardToMove.cardValue) + 1;
    }

    //check if the card is in ascending order.
    public static boolean isNextInAscendingOrder(Card topCard, Card cardToMove) {
        return getCardValueIndex(topCard.cardValue) + 1 == getCardValueIndex(cardToMove.cardValue);
    }

    //used the existing array of CARDVALUE to define the ascending/descending order based on index as
    //the cards are already defined and initialized in ascending order.
    public static int getCardValueIndex(String cardValue) {
        for (int i = 0; i < CARDVALUE.length; i++) {
            if (CARDVALUE[i].equals(cardValue)) {
                return i;
            }
        }
        return -1;
    }

    //Checks the game end condition if -> all the stacks in foundation pile has same length as CARDVALUE. i.e 13
    public static boolean checkIfGameEnded() {
        for (String suit : TYPE) {
            Stack<Card> foundation = foundationPiles.get(suit);
            if (foundation.size() != CARDVALUE.length) {
                return false;
            }
        }
        return true;
    }

    //method to draw the card from drawPile deque
    //card is removed from first and added to the last if not used to place in any other lane
    //a separate static Card variable created to maintain the state of the top card.
    public static void drawCard() {
        //if a card is drawn on top of the last drawn card, the last drawn card is flipped and added to the queue in the last
        if (drawnCard != null) {
            drawnCard.flip();
            drawPile.addLast(drawnCard);
        }
        //new card is drawn and placed in the static Card variable to point to the current face up card
        //drawnCardList was added to check if a card was uncovered or not this is done to fix an edge case bug which revealed the
        //previously undrawn card after the current card was moved to any of the other lanes
        //This variable is used for check after a successful move in processMove method
        if (!drawPile.isEmpty()) {
            movesCount++;
            Card drawnCardLocal = drawPile.removeFirst();
            drawnCardList.add(drawnCardLocal);
            drawnCardLocal.flip();
            drawnCard = drawnCardLocal;
            System.out.println("Drawn card: " + drawnCard.printCard());
        }
        //if the draw pile is empty display proper error message and do not increase the move count
        else {
            drawnCard = null;
            System.out.println("No more cards to draw.");
        }
    }

    //main method to start the game
    public static void main(String[] args) {
        GameInitializer.initialiseGame();
        handleUserInput();
    }
}