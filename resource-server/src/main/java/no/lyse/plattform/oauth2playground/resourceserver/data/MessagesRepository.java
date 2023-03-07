package no.lyse.plattform.oauth2playground.resourceserver.data;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

@Component
public class MessagesRepository {
    public Flux<String> listMessages() {
        return Flux.just(
                "Artificial Intelligence is no match for natural stupidity.",
                "The time to relax is when you don't have time for it."
        );
    }
}
