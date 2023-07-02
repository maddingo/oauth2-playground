package no.lyse.plattform.oauth2playground.resourceserver.data;

import no.lyse.plattform.oauth2playground.jokeapi.Joke;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.random.RandomGenerator;

@Component
public class JokeRepository {

    private final List<Joke> jokes = new ArrayList<>();

    private final AtomicInteger lastIndex = new AtomicInteger(0);
    public Mono<Joke> randomJoke() {
        return Mono.fromSupplier(() -> lastIndex.updateAndGet(u ->
            {
                if (jokes.isEmpty()) {
                   return -1;
                } else if (u >= (jokes.size() - 1)) {
                    return 0;
                } else {
                    return u + 1;
                }
            }))
            .filter(idx -> idx >= 0)
            .log()
            .map(jokes::get);
    }

    public void addJoke(Joke joke) {
        jokes.add(joke);
    }
}
