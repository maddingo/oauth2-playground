package no.lyse.plattform.oauth2playground.resourceserver.web;

import lombok.RequiredArgsConstructor;
import no.lyse.plattform.oauth2playground.jokeapi.Joke;
import no.lyse.plattform.oauth2playground.resourceserver.data.JokeRepository;
import no.lyse.plattform.oauth2playground.resourceserver.data.MessagesRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ResourceController {

    private final MessagesRepository messages;
    private final JokeRepository jokes;

    @GetMapping(value = "/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<String>> listMessages() {
        return messages.listMessages().log()
            .collectList();
    }

    @GetMapping(value = "/joke", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Joke> getJoke() {
        return jokes.randomJoke().log();
    }
}
