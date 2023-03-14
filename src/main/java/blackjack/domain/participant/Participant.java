package blackjack.domain.participant;

import blackjack.domain.card.Card;
import blackjack.domain.card.Hand;

public abstract class Participant {

    protected static final int BLACK_JACK_SCORE = 21;
    protected static final int BLACK_JACK_COUNT = 2;
    protected static final int ACE_ALTER_VALUE = 10;

    protected final Hand hand;

    public Participant() {
        this.hand = new Hand();
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public int calculateCurrentScore() {
        int sum = hand.sum();
        int aceCount = hand.getAceCount();

        while (sum > BLACK_JACK_SCORE && aceCount > 0) {
            sum -= ACE_ALTER_VALUE;
            aceCount -= 1;
        }
        return sum;
    }

    public boolean isBlackJack() {
        return hand.getCount() == BLACK_JACK_COUNT && hand.sum() == BLACK_JACK_SCORE;
    }

    public boolean isBust() {
        return calculateCurrentScore() > BLACK_JACK_SCORE;
    }

    public boolean isHigherThan(Participant other) {
        return this.calculateCurrentScore() > other.calculateCurrentScore();
    }

    public abstract boolean canReceive();

    public Hand getCards() {
        return hand;
    }
}
