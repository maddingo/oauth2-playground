package no.lyse.plattform.oauth2playground.resourceserver.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.token.SecureRandomFactoryBean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Component
public class JokeRepository {

    private List<Joke> jokes = new ArrayList<>();

    private final SecureRandom secure = new SecureRandom();

    public Mono<Joke> randomJoke() {
        return Mono.fromSupplier(() -> secure.nextInt(jokes.size()))
            .log()
            .map(idx -> jokes.get(idx));
    }

    public void addJoke(Joke joke) {
        jokes.add(joke);
    }
}
