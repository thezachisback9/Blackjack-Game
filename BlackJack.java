/**
 * Try to win blackjack!
 * @author zach amanuel
 **/
import java.util.Scanner;

class BlackJack_SAmanuel {
    public static void main(String[] args) {
        int dealerWins = 0;
        int playerWins = 0;
        double playerMoney = 200;
        double betMoney;
        Scanner inputx = new Scanner(System.in);
        Scanner inputd = new Scanner(System.in);
        System.out.println("Do you want to play blackjack?");
        
        while (inputd.nextLine().toLowerCase().equals("yes")) {
            System.out.println("Player wins: " + playerWins + "\nDealer wins: " + dealerWins);
            System.out.println("Welcome to blackjack. How much do you want to bet? You have $" + playerMoney + " left");
            betMoney = inputx.nextDouble();
            Deck deck = new Deck();
            Player player = new Player();
            Dealer dealer = new Dealer();
            Scanner inputs = new Scanner(System.in);
            
            Card dealtCard = deck.deal();
            player.addCard(dealtCard);
            dealtCard = deck.deal();
            dealer.addToDealer(dealtCard);
            dealtCard = deck.deal();
            player.addCard(dealtCard);
            dealtCard = deck.deal();
            dealer.addToDealer(dealtCard);
            
            System.out.println("Dealer: ");
            dealer.showDHand();
            System.out.println("Dealer also has a face-down card, you can't see its value right now.");
            System.out.println();
            System.out.println("Player: ");
            player.display();
            
            while (player.getUserChoice(inputs)) {
                dealtCard = deck.deal();
                player.addCard(dealtCard);
                player.display();
                
                if (player.bust()) {
                    System.out.println("You lose.");
                    dealerWins++;
                    playerMoney -= betMoney;
                    break;
                }
                
                if (player.win()) {
                    break;
                }
            }
            
            if (player.score() == 21) {
                System.out.println("----------------- YOU WIN. --------------------\n");
                playerWins++;
                playerMoney += betMoney;
            } else if (player.bust()) {
            } else if (dealer.getDHandValue() > 21) {
                System.out.println("DEALER BUSTED!!!!");
                playerWins++;
                playerMoney += betMoney;
            } else if (dealer.getDHandValue() > player.score() && dealer.getDHandValue() <= 21) {
                System.out.println("------------------- DEALER WINS. -------------------\n");
                dealerWins++;
                playerMoney -= betMoney;
            } else if (player.score() > dealer.getDHandValue() && player.score() <= 21) {
                System.out.print("----------------- YOU WIN. --------------------\n");
                System.out.println("Player's cards: ");
                player.display();
                playerWins++;
                playerMoney += betMoney;
            }
            
            while (dealer.shouldAdd()) {
                Card dealerCard = deck.deal();
                dealer.addToDealer(dealerCard);
            }
            dealer.endGame();
            System.out.println("Thanks for playing.");
            System.out.println("Do you want to play again? You have $" + playerMoney + " left");
            
            if (playerMoney <= 0) {
                System.out.println("you have no money left... don't go into debt!");
                break;
            }
        }
    }
}

class Dealer { // dealer class
    private Hand dHand = new Hand();

    public void showDHand() {
        System.out.println(dHand.toString2());
    }

    public int getDHandValue() {
        return dHand.getHandValue();
    }

    public void addToDealer(Card dealtCard) {
        dHand.add(dealtCard);
    }

    public boolean shouldAdd() {
        if (dHand.getHandValue() <= 16 || dHand.getHandValue() == 17) {
            return true;
        } else {
            return false;
        }
    }

    public void endGame() {
        System.out.println("Dealer's hand: \n" + dHand.toString());
    }
}

class Player { // Player Class
    private Hand hand = new Hand();

    public boolean getUserChoice(Scanner input) { // this method will ask the user whether they want to stay or hit, and will add a card to their hand if they choose to hit
        System.out.println("Do you want to hit or stay?");
        String userInput = input.nextLine();
        if (userInput.equals("hit")) {
            return true;
        } else {
            return false;
        }
    }

    public void addCard(Card dealtCard) {
        hand.add(dealtCard);
    }

    public void display() { // method to display the cards in the user's hand and their value
        System.out.println(hand.toString());
        if (hand.getHandValue() == 21) {
            win();
        }
    }

    public boolean bust() {
        if (hand.getHandValue() > 21) {
            return true;
        } else {
            return false;
        }
    }

    public boolean win() {
        if (hand.getHandValue() == 21) {
            return true;
        }
        return false;
    }

    public int score() {
        return hand.getHandValue();
    }
}

class Hand { // Hand class
    private Card[] userHand;
    private int numCards;

    public Hand() {
        userHand = new Card[12];
        numCards = 0;
    }

    public void add(Card xcard) {
        userHand[numCards] = xcard;
        numCards++;
    }

    public int getHandValue() { // this method will return the value of each individual card
        int tempValue = 0;
        int ace = 0;
        int value = 0;
        for (int i = 0; i < numCards; i++) {
            Card tempCard = userHand[i];
            tempValue += tempCard.getValue();
            if (tempCard.getValue() == 1) {
                ace++;
                if (tempValue + 10 <= 21) {
                    tempValue += 10;
                }
            }
        }
        return tempValue;
    }

    public int getDHandValue() { // this method will return the value of ONE individual card that the dealer has
        int tempValue = 0;
        int ace = 0;
        int value = 0;
        for (int i = 1; i < numCards; i++) {
            Card tempCard = userHand[i];
            tempValue += tempCard.getValue();
        }
        return tempValue;
    }

    public String toString() {
        String str = "";
        for (int i = 0; i < numCards; i++) {
            str = str + userHand[i] + "\n";
        }
        return "Total value of cards is: " + getHandValue() + "\nCards: \n" + str;
    }

    public String toString2() {
        String str = "";
        for (int i = 1; i < numCards; i++) {
            str = str + userHand[i] + "\n";
        }
        return "total value: " + getDHandValue() + "\nCards: \n" + str;
    }
}

class Deck { // deck class
    private String[] suits = new String[4];
    private Card[] deck = new Card[52];
    private Card[] newDeck = deck.clone();
    private int count;

    public Deck() {
        count = 0;
        String[] suits = {"Hearts", "Spades", "Diamonds", "Clubs"};
        for (String card : suits) {
            for (int rank = 1; rank < 14; rank++) {
                deck[count] = new Card(rank, card);
                count++;
            }
        }
        shuffle();
    }

    public Card deal() { // deals the user a card from the deck
        if (count != 0) {
            Card first = deck[count - 1];
            count -= 1;
            return first;
        }
        return null;
    }

    public void shuffle() { // randomizes the deck
        for (int i = 1; i < 1000; i++) {
            int test1 = ((int) (Math.random() * 52));
            int test2 = ((int) (Math.random() * 52));
            Card card1 = deck[test1];
            Card card2 = deck[test2];
            deck[test2] = card1;
            deck[test1] = card2;
        }
    }

    public String toString() {
        String cards = "";
        for (Card ccard : deck) {
            cards += ccard.toString();
            cards += "\n";
        }
        return cards;
    }
}

class Card {
    private String suit;
    private int rank;
    private int value;

    public Card(int rank, String suit) {
        this.rank = rank;
        this.suit = suit;
        if (rank == 1) {
            value = 1;
        } else if (rank < 14 && rank > 10) {
            value = 10;
        } else {
            value = rank;
        }
    }

    public String toString() {
        if (rank < 11 && rank > 1) {
            return rank + " of " + suit + " (point value = " + value + ")";
        } else if (rank == 11) {
            return "Jack of " + suit + " (point value = " + value + ")";
        } else if (rank == 12) {
            return "Queen of " + suit + " (point value = " + value + ")";
        } else if (rank == 13) {
            return "King of " + suit + " (point value = " + value + ")";
        } else if (rank == 1) {
            return "Ace of " + suit + " (point value = " + value + ", will count as 11 points rather than 1 if current hand value is under 11.)";
        }
        return "";
    }

    public int getValue() {
        return value;
    }
}
