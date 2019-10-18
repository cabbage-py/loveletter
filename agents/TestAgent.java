package agents;

import loveletter.Action;
import loveletter.Agent;
import loveletter.Card;
import loveletter.State;

public class TestAgent implements Agent {

    private State current;
    private int myIndex;
    private int[] deck;
    int[][] matches = {
           // 1,  2,  3,  4,  5,  6,  7,  8
            {-1, -1, -1, -1, -1, -1, -1, -1 }, //Guard
            {-1, -1, -1, -1, -1, -1, -1, -1 }, // 2
            {-1, -1, -1, -1, -1, -1, -1, -1 }, // 3
            {-1, -1, -1, -1, -1, -1, -1, -1 }, // 4
            {-1, -1, -1, -1, -1, -1, -1, -1 }, // 5
            {-1, -1, -1, -1, -1, -1, -1, -1 }, // 6
            {-1, -1, -1, -1, -1, -1, -1, -1 }, // 7
            {-1, -1, -1, -1, -1, -1, -1, -1 }, // 8
    };


    private int cardToIndex(Card c){
        return c.value() - 1;
    }

    /**
     * Reports the agents name
     * */
    public String toString(){return "Testo";}

    @Override
    public void newRound(State start) {
        current = start;
        myIndex = current.getPlayerIndex();
        /**
         * Initialises the deck.
         * index is equal to value-1 (guards are index 0, princess is index 7)
         * value of each index is the number of such cards left in deck
         * */
        deck = new int[] {5, 2, 2, 2, 2, 1, 1, 1};
    }

    @Override
    public void see(Action act, State results) {
        current = results;
        Card card = act.card();
        int player = act.player();
        int target = act.target();
        Card guess = act.guess();
    }

    @Override
    public Action playCard(Card c) {
        Card d = current.getCard(myIndex);

        int i = cardToIndex(c);
        int j = cardToIndex(d);

        Action act = Action.playHandmaid(myIndex);
        return null;
    }
}
