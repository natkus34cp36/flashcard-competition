package nat.flashcardcompetitionModel;

/**
 * Created by Nat on 2/15/2017.
 */

public class Card {
    public int cardID;
    public String first;
    public String second;
    public boolean active;

    public Card(){}

    public Card(int cardID, String first, String second, boolean active){
        this.cardID = cardID;
        this.first = first;
        this.second = second;
        this.active = active;
    }

    public String toString(){
        return cardID + ": " + first + ", " + second;
    }


}
