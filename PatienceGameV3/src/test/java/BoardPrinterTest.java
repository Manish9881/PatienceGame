import org.junit.*;

public class BoardPrinterTest {

    @Test
    public void testPrintGameVertical() {
        GameInitializer.initialiseGame();
        BoardPrinter.printGameVertical(Game.lanes, Game.foundationPiles);
        System.out.println("Game board printed successfully.");
    }
}
