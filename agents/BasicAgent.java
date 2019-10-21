package agents;

import loveletter.*;

import java.util.Random;

public class BasicAgent implements Agent {

    private Random rand;
    private State current;
    private int myIndex;
    private int[] deck;
    private int[][] recommendedAction = {
           //Gu, Pt, Ba, Ha, Pc, Ki, Co, Ps
            {0, 1, 0, 0, 0, 0, 0, 0 }, // Guard     0
            {1, 1, 1, 1, 1, 1, 1, 1 }, // Priest    1
            {0, 1, 2, 2, 2, 2, 2, 2 }, // Baron     2
            {0, 1, 2, 3, 3, 3, 3, 3 }, // Handmaid  3
            {0, 1, 2, 3, 4, 4, 6, 4 }, // Prince    4
            {0, 1, 2, 3, 4, -1, 6, 5 }, // King     5
            {0, 1, 2, 3, 6, 6, -1, 6 }, // Countess 6
            {0, 1, 2, 3, 4, 5, 6, -1 }, // Princess 7
    };

    private boolean hasPriority = false;
    private int winningPlayer = -1;
    private int runnerUpPlayer = -1;

    //0 place default constructor
    public BasicAgent(){
        rand  = new Random();
    }


    private int cardToIndex(Card c){
        return c.value() - 1;
    }

    /*
     * Reports the agents name
     */
    public String toString(){return "Basic";}

    @Override
    public void newRound(State start) {
        current = start;
        myIndex = current.getPlayerIndex();
        /*
         * Initialises the deck.
         * index is equal to value-1 (guards are index 0, princess is index 7)
         * value of each index is the number of such cards left in deck
         */
        deck = new int[] {5, 2, 2, 2, 2, 1, 1, 1};

        // Checks which player is winning
        int max = 0;
        boolean hasWinner = false;

        for (int i=0; i<current.numPlayers(); i++){
            if (i != myIndex) {
                if (current.score(i) > max){
                    max = current.score(i);
                    winningPlayer = i;
                    hasWinner = true;
                    hasPriority = true;
                }
            }
        }
        if (hasWinner) {
            System.out.println("\n" + "The winning player is player " + winningPlayer);

            max = 0;
            for (int i = 0; i < current.numPlayers(); i++) {
                if (i != myIndex && i != winningPlayer) {
                    if (current.score(i) >= max) {
                        max = current.score(i);
                        runnerUpPlayer = i;
                    }
                }
            }
            System.out.println("\n" + "The runnerup player is player " + runnerUpPlayer + "\n");
        }
    }

    @Override
    public void see(Action act, State results) {
        current = results;
        Card card = act.card();
        int player = act.player();
        int target = act.target();
        Card guess = act.guess();

        deck[card.value() - 1] -= 1;
    }

    //Assigns winning player as target, else sets target to random
    //HOW DO I GET RID OF PLAYERS OUT OF THE ROUND
    private int getTarget(){
        int target;
        if (hasPriority) {
            if (!current.handmaid(winningPlayer) && !current.eliminated(winningPlayer)) {
                target = winningPlayer;
            } else if (!current.handmaid(runnerUpPlayer) && !current.eliminated(runnerUpPlayer)) {
                target = runnerUpPlayer;
            } else {
                target = 6 - myIndex - winningPlayer - runnerUpPlayer;
                if (!current.handmaid(target) || !current.eliminated(target)){
                    target = winningPlayer;
                }
            }
        }

        //else the target is randomized
        else {
            target = rand.nextInt(current.numPlayers());
            while (target == myIndex){
                target = rand.nextInt(current.numPlayers());
            }
        }

        return target;
    }

    //predictor func
    private Card predict(int [] deck){
        int noOfCards = 0;
        for (int i=0; i<8; i++){
            noOfCards += deck[i];
        }
        int selected = rand.nextInt(noOfCards);
        int selectedIndex = 0;
        for (int i=0; i<8; i++){
            selected -= deck[i];
            if (selected <= 0) selectedIndex = i;
        }
        return Card.values()[selectedIndex];
    }

    @Override
    public Action playCard(Card c) {
        //initializing variables
        Action act = null;
        //change maybe
        int target = getTarget();

        /*
        while (current.handmaid(target) || current.eliminated(target) || target == myIndex) {
            target = rand.nextInt(current.numPlayers());
        } */

        //Looks at both cards in hand
        Card d = current.getCard(myIndex);
        int i = cardToIndex(c);
        int j = cardToIndex(d);

        //Decides recommended Action based on cards
        int k = recommendedAction[i][j];

        try {
            switch (k) {
                case 0:
                    //while (!current.legalAction(act, c)) {
                    Card guess = predict(deck);
                    while(guess.value() == 1){
                        guess = predict(deck);
                    }
                    act = Action.playGuard(myIndex, target, predict(deck));
                    break;
                case 1:
                    act = Action.playPriest(myIndex, target);
                    break;
                case 2:
                    act = Action.playBaron(myIndex, target);
                    break;
                case 3:
                    act = Action.playHandmaid(myIndex);
                    break;
                case 4:
                    act = Action.playPrince(myIndex, target);
                    break;
                case 5:
                    act = Action.playKing(myIndex, target);
                    break;
                case 6:
                    act = Action.playCountess(myIndex);
                    break;
                default:
                    break;

            }

        } catch (IllegalActionException e) {
            e.printStackTrace();
        }

        return act;
    }
}
