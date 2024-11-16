import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class GameInitializer {
    // Method to create the deck of cards to be used for the game.
    public static List<Card> createDeck() {
        List<Card> deck = new ArrayList<>();
        for (String type : Game.TYPE) {
            for (String cardValue : Game.CARDVALUE) {
                deck.add(new Card(type, cardValue));
            }
        }
        return deck;
    }

    // Method to initialize all the necessary variables to play the game
    public static void initialiseGame() {
        Game.foundationPiles.put("D", new Stack<>());
        Game.foundationPiles.put("S", new Stack<>());
        Game.foundationPiles.put("C", new Stack<>());
        Game.foundationPiles.put("H", new Stack<>());

        List<Card> deck = createDeck();
        Collections.shuffle(deck);

        for (int i = 0; i < 7; i++) {
            Game.lanes.add(new Stack<>());
            for (int j = 0; j <= i; j++) {
                Card c = deck.remove(deck.size() - 1);
                if (j == i){
                    c.flip();
                }
                Game.lanes.get(i).push(c);
            }
        }
        Game.drawPile.addAll(deck);
    }

}
