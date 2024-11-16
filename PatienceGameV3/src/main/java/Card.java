// This class hosts the structure of the Card, to be used in the game
class Card {
    String type;
    String cardValue;
    Boolean isFaceUp;

    public Card(String type, String cardValue) {
        this.type = type;
        this.cardValue = cardValue;
        this.isFaceUp = false;
    }

    public void flip() {
        isFaceUp = !isFaceUp;
    }

    public String printCard() {
        return isFaceUp ? (type + cardValue) : "**";
    }
}