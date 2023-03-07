package blackjack.domain.game;

import blackjack.domain.card.Card;
import blackjack.domain.card.Denomination;
import blackjack.domain.card.Suit;
import blackjack.domain.participant.Dealer;
import blackjack.domain.participant.Player;
import blackjack.domain.participant.Players;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class BlackJackGameTest {

    private Players players;

    @BeforeEach
    void init() {
        players = Players.create(List.of("gray", "luca"));
    }

    @Test
    @DisplayName("생성 테스트")
    void createBlackjackGame() {
        BlackJackGame blackJackGame = BlackJackGame.create(players);

        assertThat(blackJackGame).isNotNull();
    }

    @Test
    @DisplayName("초기에 딜러와 플레이어에게 2장씩 카드를 나눠준다.")
    void passCard() {
        BlackJackGame blackJackGame = BlackJackGame.create(players);

        blackJackGame.setUp();
        List<Player> players = blackJackGame.getPlayers();
        Dealer dealer = blackJackGame.getDealer();

        Assertions.assertAll(
                () -> assertThat(players.get(0).getCards().getCount()).isEqualTo(2),
                () -> assertThat(players.get(1).getCards().getCount()).isEqualTo(2),
                () -> assertThat(dealer.getCards().getCount()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("플레어어에게 카드를 한 장 나눠준다.")
    void passExtraCardToPlayer() {
        BlackJackGame blackJackGame = BlackJackGame.create(players);
        List<Player> players = this.players.getPlayers();
        Player gray = players.get(0);
        Player luca = players.get(1);

        blackJackGame.passCardTo(gray);
        blackJackGame.passCardTo(luca);
        blackJackGame.passCardTo(luca);

        assertAll(
                () -> assertThat(gray.getCards().getCount()).isEqualTo(1),
                () -> assertThat(luca.getCards().getCount()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("딜러가 16점 이하이면 카드 한 장을 나눠준다.")
    void passExtraCardToDealer() {
        BlackJackGame blackJackGame = BlackJackGame.create(players);
        Dealer dealer = blackJackGame.getDealer();
        dealer.addCard(new Card(Suit.SPADE, Denomination.TEN));
        int beforeCount = dealer.getCards().getCount();

        blackJackGame.passCardTo(dealer);

        assertThat(dealer.getCards().getCount()).isEqualTo(beforeCount + 1);
    }

    @Test
    @DisplayName("딜러가 17점 이상이면 카드 나눠줄 수 없다.")
    void canNotPassExtraCardToDealer() {
        BlackJackGame blackJackGame = BlackJackGame.create(players);
        Dealer dealer = blackJackGame.getDealer();
        dealer.addCard(new Card(Suit.SPADE, Denomination.TEN));
        dealer.addCard(new Card(Suit.HEART, Denomination.SEVEN));
        int beforeCount = dealer.getCards().getCount();

        blackJackGame.passCardTo(dealer);

        assertThat(dealer.getCards().getCount()).isEqualTo(beforeCount);
    }

    @Test
    @DisplayName("딜러와 플레이어의 승패를 반환한다.")
    void calculateResult() {
        BlackJackGame blackJackGame = BlackJackGame.create(players);
        List<Player> players = blackJackGame.getPlayers();
        Dealer dealer = blackJackGame.getDealer();
        Player gray = players.get(0);
        Player luca = players.get(1);

        setCards(dealer, gray, luca);
        Map<Player, Result> result = blackJackGame.calculateResult();

        Assertions.assertAll(
                () -> assertThat(result).containsEntry(gray, Result.WIN),
                () -> assertThat(result).containsEntry(luca, Result.LOSE)
        );
    }

    private void setCards(Dealer dealer, Player gray, Player luca) {
        dealer.addCard(new Card(Suit.HEART, Denomination.TEN));
        dealer.addCard(new Card(Suit.HEART, Denomination.SEVEN));
        gray.addCard(new Card(Suit.SPADE, Denomination.ACE));
        gray.addCard(new Card(Suit.SPADE, Denomination.TEN));
        luca.addCard(new Card(Suit.DIAMOND, Denomination.SIX));
        luca.addCard(new Card(Suit.DIAMOND, Denomination.TEN));
    }
}
