package no.lyse.plattform.oauth2playground.resourceserver.web;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.text.MessageFormat;

@RestController
@RequiredArgsConstructor
public class ResourceController {

    private final HttpExchangeRepository  traces;

    @GetMapping(value = "/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<String> logMessages() {
        return Flux.just(
            "Artificial Intelligence is no match for natural stupidity.",
            "The time to relax is when you don't have time for it."
        );
//        return Flux.fromStream(traces.findAll().stream())
//            .map(exc -> MessageFormat.format("{0}: ", exc.getTimestamp(), exc.getRequest().getUri()));
    }
}
