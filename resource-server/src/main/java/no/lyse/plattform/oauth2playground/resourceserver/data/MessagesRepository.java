package no.lyse.plattform.oauth2playground.resourceserver.data;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.function.IntSupplier;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

@Component
public class MessagesRepository {
    private final List<String> quotes = List.of(
        "No man remains quite what he was when he recognizes himself. (Thomas Mann)",
        "If you hate a person, you hate something in him that is part of yourself. What isn't part of ourselves doesn't disturb us. (Hermann Hesse)",
        "I'm lazy. But it's the lazy people who invented the wheel and the bicycle because they didn't like walking or carrying things. (Lech Walesa)",
        "The best way to predict the future is to invent it. (Alan Kay)"
    );

    private final RandomGenerator randomGenerator = RandomGenerator.getDefault();

    public Flux<String> listMessages() {
        return randomQuotes(2);
    }

    /**
     * return a random selection from quotes.
     * @param count number of quotes to return;
     */
    private Flux<String> randomQuotes(int count) {
        return Flux.generate(
            () -> 0,
            (state, sink) -> {
                if (state == count) {
                    sink.complete();
                    return state;
                }

                int listIndex = randomGenerator.nextInt(quotes.size());
                sink.next(quotes.get(listIndex));
                return state + 1;
            }
        );
    }
}
