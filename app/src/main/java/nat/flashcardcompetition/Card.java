package nat.flashcardcompetition;

/**
 * Created by Nat on 2/15/2017.
 */

public class Card {
    int cardID;
    String first;
    String second;
    String pronunciation;
    boolean active;

    public Card(int cardID, String first, String second, String pronunciation, boolean active){
        this.cardID = cardID;
        this.first = first;
        this.second = second;
        this.pronunciation = pronunciation;
        this.active = active;
    }


}
