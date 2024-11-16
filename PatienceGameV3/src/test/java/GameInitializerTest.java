import org.junit.*;

import java.util.*;

import static org.junit.Assert.*;

public class GameInitializerTest {

    @Test
    public void testCreateDeck() {
        List<Card> deck = GameInitializer.createDeck();
        assertEquals(52, deck.size());
    }

    @Test
    public void testInitialiseGame() {
        Game.lanes = new ArrayList<>();
        GameInitializer.initialiseGame();

        assertEquals(7, Game.lanes.size());
        assertEquals(4, Game.foundationPiles.size());
        assertFalse(Game.drawPile.isEmpty());
    }
}
