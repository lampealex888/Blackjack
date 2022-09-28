/**
* The Blackjack class plays blackjack using the
* BlackjackCards class. The command line parameters
* are the number of rounds, the number of decks in
* the shoe, and the number of rounds to play with
* trace on.
* 
* @author  Alex Lampe
* @since   1-4-2022 
*/
public class Blackjack extends BlackjackCards
{
    private static BlackjackCards shoe;
    private static BlackjackCards discardPile;
    private static BlackjackCards dealerHand;
    private static BlackjackCards playerHand;

    public Blackjack(int initSize)
    {
        super(initSize);
    }

    private static void reshuffle(BlackjackCards deck, BlackjackCards discard)
    {
        for (int i = 0; i < discard.size(); i++) {
            deck.enqueue(discard.dequeue());
        }
        deck.shuffle();
    }

    private static void playRoundTrace(int numRounds)
    {
        int round = 1;
        boolean playerIn;
        boolean dealerIn;
        for (int i = 0; i < numRounds; i++) {
            playerIn = true;
            dealerIn = true;
            System.out.println("Round " + round + " beginning");
            // Deal the player cards
            playerHand.enqueue(shoe.dequeue());
            playerHand.enqueue(shoe.dequeue());
            System.out.println("Player: " + playerHand.toString() + " : " + playerHand.getValue());
            // Deal the dealer cards
            dealerHand.enqueue(shoe.dequeue());
            dealerHand.enqueue(shoe.dequeue());
            System.out.println("Dealer: " + dealerHand.toString() + " : " + dealerHand.getValue());
            // Determine if the player should HIT
            while (playerHand.getValue() < 17) {
                Card temp = shoe.dequeue();
                System.out.println("Player HITS: " + temp.toString());
                playerHand.enqueue(temp);
            }
            // Determine if the player BUSTS
            if (playerHand.getValue() > 21) {
                playerIn = false;
                System.out.println("Player BUSTS: " + playerHand.toString() + " : " + playerHand.getValue());
                System.out.println("Result: Dealer Wins!");
            }
            // Determine if the player STANDS
            if (playerIn) {
                System.out.println("Player STANDS: " + playerHand.toString() + " : " + playerHand.getValue());
            }
    
            if (playerIn) {
                // Determine if the dealer should HIT
                while (dealerHand.getValue() < 17) {
                    Card temp = shoe.dequeue();
                    System.out.println("Dealer HITS: " + temp.toString());
                    dealerHand.enqueue(temp);
                }
                // Determine if the dealer BUSTS
                if (dealerHand.getValue() > 21) {
                    dealerIn = false;
                    System.out.println("Dealer BUSTS: " + dealerHand.toString() + " : " + dealerHand.getValue());
                    System.out.println("Result: Player Wins!");
                }
                // Determine if the dealer STANDS
                if (dealerIn) {
                    System.out.println("Dealer STANDS: " + dealerHand.toString() + " : " + dealerHand.getValue());
                }
            }
            // Decide the result
            if (playerIn && dealerIn) {
                if (dealerHand.getValue() > playerHand.getValue()) {
                    System.out.println("Result: Dealer Wins!");
                } else if (playerHand.getValue() == dealerHand.getValue()) {
                    System.out.println("Result: Push!");
                } else {
                    System.out.println("Result: Player Wins!");
                }
            }
            // Add the hands to the discard pile
            while (playerHand.size() > 0) {
                discardPile.enqueue(playerHand.dequeue());
            }
            while (dealerHand.size() > 0) {
                discardPile.enqueue(dealerHand.dequeue());
            }
            // Check if the shoe is 1/4 or less of its original size and reshuffle if true
            if (shoe.size() <= (0.25) * shoe.capacity()) reshuffle(shoe, discardPile);
            round++;
            System.out.println();
        }
    }

    private static int[] playRound(int numRounds)
    {
        int round = 1;
        boolean playerIn;
        boolean dealerIn;
        int dealerWins = 0;
        int playerWins = 0;
        int pushes = 0;
        for (int i = 0; i < numRounds; i++) {
            playerIn = true;
            dealerIn = true;
            // Deal the player cards
            playerHand.enqueue(shoe.dequeue());
            playerHand.enqueue(shoe.dequeue());
            // Deal the dealer cards
            dealerHand.enqueue(shoe.dequeue());
            dealerHand.enqueue(shoe.dequeue());
            // Determine if the player should HIT
            while (playerHand.getValue() < 17) {
                playerHand.enqueue(shoe.dequeue());
            }
            // Determine if the player BUSTS
            if (playerHand.getValue() > 21) {
                playerIn = false;
                dealerWins++;
            }
    
            if (playerIn) {
                // Determine if the dealer should HIT
                while (dealerHand.getValue() < 17) {
                    dealerHand.enqueue(shoe.dequeue());
                }
                // Determine if the dealer BUSTS
                if (dealerHand.getValue() > 21) {
                    dealerIn = false;
                    playerWins++;
                }
            }
            // Decide the result
            if (playerIn && dealerIn) {
                if (dealerHand.getValue() > playerHand.getValue()) {
                    dealerWins++;
                } else if (playerHand.getValue() == dealerHand.getValue()) {
                    pushes++;
                } else {
                    playerWins++;
                }
            }
            // Add the hands to the discard pile
            while (playerHand.size() > 0) {
                discardPile.enqueue(playerHand.dequeue());
            }
            while (dealerHand.size() > 0) {
                discardPile.enqueue(dealerHand.dequeue());
            }
            // Check if the shoe is 1/4 or less of its original size and reshuffle if true
            if (shoe.size() <= (0.25) * shoe.capacity()) {
                System.out.println();
                System.out.println("Reshuffling the shoe in round " + round);
                reshuffle(shoe, discardPile);
            } 
            round++;
        }
        int[] results = new int[]{ dealerWins, playerWins, pushes };
        return results;
    }

    public static void main(String[] args)
    {
        int rounds = Integer.parseInt(args[0]);
        int numDecks = Integer.parseInt(args[1]);
        int numTracing = Integer.parseInt(args[2]);

        // Initialize the variables
        shoe = new BlackjackCards(numDecks*52);
        discardPile = new BlackjackCards(numDecks*52);
        dealerHand = new BlackjackCards(3);
        playerHand = new BlackjackCards(3);
        
        // Add the decks to the shoe and shuffle it
        for (int i = 0; i < numDecks; i++)
            for (Card.Suits s: Card.Suits.values())
                for (Card.Ranks r: Card.Ranks.values())
                    shoe.enqueue(new Card(s, r));
        shoe.shuffle();

        System.out.println("Starting Blackjack with " + rounds + " rounds and " + numDecks + " decks in the shoe");
        System.out.println();

        playRoundTrace(numTracing);

        int[] temp = playRound(rounds);
        System.out.println();
        System.out.println("After " + rounds + " rounds, here are the results");
        System.out.println("	Dealer Wins: " + temp[0]);
        System.out.println("	Player Wins: " + temp[1]);
        System.out.println("	Pushes: " + temp[2]);
    }
}