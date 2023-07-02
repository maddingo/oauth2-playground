package no.lyse.plattform.oauth2playground.resourceserver.data;

import lombok.RequiredArgsConstructor;
import no.lyse.plattform.oauth2playground.jokeapi.Joke;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JokeRepositoryInitializer implements CommandLineRunner {

    private final JokeRepository jokeRepository;

    @Override
    public void run(String... args) {
        int count = 0;
        addJoke("Im Wald da steht ein Ofenrohr, stell dir mal die Hitze vor.", ++count);
        addJoke("Die Schwalbe ist ein lustig Tier und fliegt auch um die Kirchturmspitz'. Der Löwe ist kein lustig Tier und fliegt nicht um die Kirchturmspitz'.", ++count);
        addJoke("Rumpeldibumpel, weg war der Kumpel, Schippe d'rauf, Glück Auf!.", ++count);
    }

    private void addJoke(String joke, int id) {
        jokeRepository.addJoke(
            Joke.builder()
                .id(id)
                .category("Arthur Schramm")
                .type(Joke.JokeType.SINGLE)
                .flags(Joke.JokeFlags.builder().build())
                .lang("de")
                .safe(true)
                .joke(joke)
                .build());

    }
}
