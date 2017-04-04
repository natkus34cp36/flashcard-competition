package nat.flashcardcompetitionModel;

/**
 * Created by Nat on 2/23/2017.
 */

public class CardMeta {
    public int id;
    public int cardID;
    public int isActive;

    public CardMeta(){}

    public CardMeta(int id, int cardID, int isActive){
        this.id = id;
        this.cardID = cardID;
        this.isActive = isActive;
    }

    public String printOut(){
        return ""+cardID+", "+((isActive>0)?"active":"inactive");
    }
}
