package blackjack.domain.participant;

import blackjack.domain.card.Card;
import blackjack.domain.card.Number;
import blackjack.domain.card.Symbol;
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

        dealer.addCard(new Card(Symbol.CLOVER, Number.ACE));
        dealer.addCard(new Card(Symbol.DIAMOND, Number.TEN));

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
                Arguments.of(List.of(new Card(Symbol.SPADE, Number.SEVEN), new Card(Symbol.CLOVER, Number.TWO)), 9),
                Arguments.of(List.of(new Card(Symbol.HEART, Number.NINE), new Card(Symbol.HEART, Number.TWO)), 11),
                Arguments.of(List.of(new Card(Symbol.DIAMOND, Number.TEN), new Card(Symbol.SPADE, Number.TEN)), 20),
                Arguments.of(List.of(new Card(Symbol.CLOVER, Number.THREE), new Card(Symbol.CLOVER, Number.TWO)), 5),
                Arguments.of(List.of(new Card(Symbol.CLOVER, Number.SEVEN), new Card(Symbol.SPADE, Number.SEVEN), new Card(Symbol.DIAMOND, Number.SEVEN)), 21),
                Arguments.of(List.of(new Card(Symbol.CLOVER, Number.TEN), new Card(Symbol.SPADE, Number.ACE), new Card(Symbol.DIAMOND, Number.SEVEN)), 18)
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
                Arguments.of(List.of(new Card(Symbol.SPADE, Number.SEVEN), new Card(Symbol.CLOVER, Number.TWO)), true),
                Arguments.of(List.of(new Card(Symbol.HEART, Number.NINE), new Card(Symbol.HEART, Number.TWO)), true),
                Arguments.of(List.of(new Card(Symbol.DIAMOND, Number.TEN), new Card(Symbol.SPADE, Number.TEN)), false),
                Arguments.of(List.of(new Card(Symbol.CLOVER, Number.THREE), new Card(Symbol.CLOVER, Number.TWO), new Card(Symbol.SPADE, Number.TWO), new Card(Symbol.HEART, Number.TWO)), false),
                Arguments.of(List.of(new Card(Symbol.CLOVER, Number.SEVEN), new Card(Symbol.SPADE, Number.SEVEN), new Card(Symbol.DIAMOND, Number.SEVEN)), false),
                Arguments.of(List.of(new Card(Symbol.CLOVER, Number.TEN), new Card(Symbol.SPADE, Number.TEN), new Card(Symbol.DIAMOND, Number.SEVEN)), false)
        );
    }
}
