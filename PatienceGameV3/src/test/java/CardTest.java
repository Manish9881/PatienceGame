import org.junit.*;
import static org.junit.Assert.*;

public class CardTest {

    @Test
    public void testFlip() {
        Card card = new Card("H", "A");
        assertFalse(card.isFaceUp);
        card.flip();
        assertTrue(card.isFaceUp);
    }

    @Test
    public void testPrintCard() {
        Card card = new Card("H", "A");
        assertEquals("**", card.printCard());
        card.flip();
        assertEquals("HA", card.printCard());
    }
}
