package blackjack.domain.game;

import blackjack.domain.card.Card;
import blackjack.domain.card.CardFactory;
import blackjack.domain.card.Deck;
import blackjack.domain.participant.Dealer;
import blackjack.domain.participant.Participant;
import blackjack.domain.participant.Player;
import blackjack.domain.participant.Players;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlackJackGame {

    private static final int INIT_CARD_COUNT = 2;

    private final Players players;
    private final Dealer dealer;
    private final Deck deck;

    private BlackJackGame(Players players, Dealer dealer, Deck deck) {
        this.players = players;
        this.dealer = dealer;
        this.deck = deck;
    }

    public static BlackJackGame create(Players players) {
        Dealer dealer = new Dealer();
        Deck deck = Deck.create(CardFactory.createShuffledCard());
        return new BlackJackGame(players, dealer, deck);
    }

    public void setUp() {
        List<Player> players = this.players.getPlayers();
        for (Player player : players) {
            drawCard(player, INIT_CARD_COUNT);
        }
        drawCard(dealer, INIT_CARD_COUNT);
    }

    public Map<Player, Result> calculateResult() {
        Map<Player, Result> result = new HashMap<>();

        for (Player player : players.getPlayers()) {
            result.put(player, Result.calculate(player, dealer));
        }
        return result;
    }

    public void drawCard(Participant participant, int count) {
        for (int i = 0; i < count; i++) {
            passCard(participant);
        }
    }

    private void passCard(Participant participant) {
        if (participant.canReceive()) {
            Card card = deck.draw();
            participant.addCard(card);
        }
    }

    public List<Player> getPlayers() {
        return players.getPlayers();
    }

    public Dealer getDealer() {
        return dealer;
    }
}