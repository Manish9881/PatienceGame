import org.junit.*;

import java.util.*;

import static org.junit.Assert.*;

public class GameTest {

    @BeforeClass
    public static void setUp() {

        Game.lanes = new ArrayList<>();
        Game.drawPile = new ArrayDeque<>();
        Game.foundationPiles = new HashMap<>();
        Game.drawnCard = null;
        Game.score = 0;
        Game.movesCount = 0;
        Game.drawnCardList = new ArrayList<>();
    }

    @Test
    public void testIsMoveCommand_Valid() {
        assertTrue(Game.isMoveCommand("P1 P2"));
        assertTrue(Game.isMoveCommand("P3 P5 3"));
        assertTrue(Game.isMoveCommand("B P1"));
        assertTrue(Game.isMoveCommand("B H"));
    }

    @Test
    public void testIsMoveCommand_Invalid() {
        assertFalse(Game.isMoveCommand("p1p2"));
        assertFalse(Game.isMoveCommand("P8 P1"));
        assertFalse(Game.isMoveCommand("B Z"));
    }

    @Test
    public void testProcessMove_InvalidMove() {
        Game.movesCount = 0;
        GameInitializer.initialiseGame();
        Game.processMove("P1 P2 45");
        assertEquals(0, Game.movesCount);
    }

    @Test
    public void testProcessMove_ValidMove() {
        Game.movesCount = 0;
        Stack<Card> lane1 = new Stack<>();
        Stack<Card> lane2 = new Stack<>();

        Card card1 = new Card("H", "K");
        card1.flip();
        lane1.push(card1);

        Card card2 = new Card("S", "Q");
        card2.flip();
        lane2.push(card2);

        Game.lanes.add(lane1);
        Game.lanes.add(lane2);

        Game.processMove("P2 P1");

        assertTrue(Game.lanes.get(0).peek().cardValue.equals("Q"));
        assertTrue(Game.lanes.get(1).isEmpty());
        assertEquals(1, Game.movesCount);
    }

    @Test
    public void testProcessMove_ValidMoveMultiple() {
        Game.movesCount = 0;
        Stack<Card> lane1 = new Stack<>();
        Stack<Card> lane2 = new Stack<>();

        Card card1 = new Card("H", "K");
        card1.flip();
        lane1.push(card1);

        Card card2 = new Card("S", "Q");
        Card card3 = new Card("D", "J");
        card2.flip();
        card3.flip();
        lane2.push(card2);
        lane2.push(card3);

        //System.out.println(lane2);
        //System.out.println(lane2.peek().printCard());

        Game.lanes = new ArrayList<>();
        Game.lanes.add(lane1);
        Game.lanes.add(lane2);

        Game.processMove("P2 P1 2");

        assertTrue(Game.lanes.get(0).peek().cardValue.equals("J"));
        assertTrue(Game.lanes.get(1).isEmpty());
        assertEquals(1, Game.movesCount);
    }

    @Test
    public void testGetStackByCode_DrawPile() {
        Card card = new Card("H", "A");
        card.flip();
        Game.drawnCard = card;
        Stack<Card> stack = Game.getStackByCode("B");
        assertNotNull(stack);
        assertEquals("HA", stack.peek().printCard());
    }

    @Test
    public void testGetStackByCode_Invalid() {
        Stack<Card> stack = Game.getStackByCode("X");
        assertNull(stack);
    }

    @Test
    public void testIsValidMove_ValidMove() {
        Card card1 = new Card("H", "A");
        Card card2 = new Card("H", "2");
        Stack<Card> foundation = new Stack<>();
        foundation.push(card1);
        Game.foundationPiles.put("H", foundation);
        assertTrue(Game.isValidMove(card2, foundation, "H"));
    }

    @Test
    public void testIsValidMove_MultipleCards() {
        Game.lanes = new ArrayList<>();
        Stack<Card> lane1 = new Stack<>();
        Card card1 = new Card("S", "10");
        Card card2 = new Card("H", "9");
        Card card3 = new Card("C", "8");
        card1.flip();
        card2.flip();
        card3.flip();
        lane1.push(card1);
        lane1.push(card2);
        lane1.push(card3);

        Stack<Card> lane2 = new Stack<>();
        Card targetCard = new Card("D", "J");
        targetCard.flip();
        lane2.push(targetCard);

        //System.out.println(lane2);
        //System.out.println(lane2.peek().printCard());

        boolean isValid = Game.isValidMove(card1, lane2, "P2");
        assertTrue(isValid);
    }

    @Test
    public void testIsValidMove_InvalidMove() {
        Card card1 = new Card("H", "A");
        Card card2 = new Card("S", "2");
        Stack<Card> foundation = new Stack<>();
        foundation.push(card1);
        Game.foundationPiles.put("H", foundation);
        assertFalse(Game.isValidMove(card2, foundation, "H"));
    }

    @Test
    public void testDrawCard() {
        Game.movesCount = 0;
        List<Card> deck = GameInitializer.createDeck();
        Game.drawPile.addAll(deck);
        Game.drawCard();
        assertNotNull(Game.drawnCard);
        assertTrue(Game.drawnCard.isFaceUp);
        assertEquals(1, Game.movesCount);
    }

}