package blackjack.domain.participant;

import blackjack.domain.card.Card;
import blackjack.domain.card.Denomination;
import blackjack.domain.card.Suit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class DealerTest {

    @Test
    @DisplayName("딜러는 받은 카드를 자신의 패에 추가한다.")
    void addCardInCards() {
        Dealer dealer = new Dealer();

        dealer.addCard(new Card(Suit.CLOVER, Denomination.ACE));
        dealer.addCard(new Card(Suit.DIAMOND, Denomination.TEN));

        assertThat(dealer.getCards().getCount()).isEqualTo(2);
    }

    @ParameterizedTest
    @MethodSource("generateCards")
    @DisplayName("참여자는 자신의 카드 점수를 계산한다.")
    void calculateCards(List<Card> cards, int expectedScore) {
        Dealer dealer = new Dealer();
        for (Card card : cards) {
            dealer.addCard(card);
        }

        int score = dealer.calculateCurrentScore();

        assertThat(score).isEqualTo(expectedScore);
    }

    static Stream<Arguments> generateCards() {
        return Stream.of(
                Arguments.of(List.of(new Card(Suit.SPADE, Denomination.SEVEN), new Card(Suit.CLOVER, Denomination.TWO)), 9),
                Arguments.of(List.of(new Card(Suit.HEART, Denomination.NINE), new Card(Suit.HEART, Denomination.TWO)), 11),
                Arguments.of(List.of(new Card(Suit.DIAMOND, Denomination.TEN), new Card(Suit.SPADE, Denomination.TEN)), 20),
                Arguments.of(List.of(new Card(Suit.CLOVER, Denomination.THREE), new Card(Suit.CLOVER, Denomination.TWO)), 5),
                Arguments.of(List.of(new Card(Suit.CLOVER, Denomination.SEVEN), new Card(Suit.SPADE, Denomination.SEVEN), new Card(Suit.DIAMOND, Denomination.SEVEN)), 21),
                Arguments.of(List.of(new Card(Suit.CLOVER, Denomination.TEN), new Card(Suit.SPADE, Denomination.ACE), new Card(Suit.DIAMOND, Denomination.SEVEN)), 18)
        );
    }

    @ParameterizedTest
    @MethodSource("generateCardsAndFlag")
    @DisplayName("카드를 더 받을 수 있는 여부를 반환한다")
    void getParticipantIntention(List<Card> cards, boolean expectedFlag) {
        Dealer dealer = new Dealer();
        for (Card card : cards) {
            dealer.addCard(card);
        }

        assertThat(dealer.canReceive()).isEqualTo(expectedFlag);
    }

    static Stream<Arguments> generateCardsAndFlag() {
        return Stream.of(
                Arguments.of(List.of(new Card(Suit.SPADE, Denomination.SEVEN), new Card(Suit.CLOVER, Denomination.TWO)), true),
                Arguments.of(List.of(new Card(Suit.HEART, Denomination.NINE), new Card(Suit.HEART, Denomination.TWO)), true),
                Arguments.of(List.of(new Card(Suit.DIAMOND, Denomination.TEN), new Card(Suit.SPADE, Denomination.TEN)), false),
                Arguments.of(List.of(new Card(Suit.CLOVER, Denomination.THREE), new Card(Suit.CLOVER, Denomination.TWO), new Card(Suit.SPADE, Denomination.TWO), new Card(Suit.HEART, Denomination.TWO)), false),
                Arguments.of(List.of(new Card(Suit.CLOVER, Denomination.SEVEN), new Card(Suit.SPADE, Denomination.SEVEN), new Card(Suit.DIAMOND, Denomination.SEVEN)), false),
                Arguments.of(List.of(new Card(Suit.CLOVER, Denomination.TEN), new Card(Suit.SPADE, Denomination.TEN), new Card(Suit.DIAMOND, Denomination.SEVEN)), false)
        );
    }
}
