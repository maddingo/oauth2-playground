package no.lyse.plattform.oauth2playground.resourceserver.data;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.HashSet;
import java.util.List;
import java.util.random.RandomGenerator;

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
        return randomQuotes(randomGenerator.nextInt(2, quotes.size()));
    }

    /**
     * return a random selection from quotes.
     * @param count number of quotes to return;
     */
    private Flux<String> randomQuotes(int count) {
        return Flux.generate(
            () -> new HashSet<Integer>(),
            (state, sink) -> {
                if (state.size() == count || state.size() == quotes.size()) {
                    sink.complete();
                    return state;
                }

                int listIndex = randomGenerator.nextInt(quotes.size());
                while(state.contains(listIndex)) {
                    listIndex = randomGenerator.nextInt(quotes.size());
                }
                state.add(listIndex);
                sink.next(quotes.get(listIndex));
                return state;
            }
        );
    }
}
