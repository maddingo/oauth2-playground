package no.lyse.plattform.oauth2playground.resourceserver.data;

/*
{
    "error": false,
    "category": "Programming",
    "type": "single",
    "joke": "\"Can I tell you a TCP joke?\"\n\"Please tell me a TCP joke.\"\n\"OK, I'll tell you a TCP joke.\"",
    "flags": {
        "nsfw": false,
        "religious": false,
        "political": false,
        "racist": false,
        "sexist": false,
        "explicit": false
    },
    "id": 57,
    "safe": true,
    "lang": "en"
}
 */

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.boot.jackson.JsonComponent;

/**
 * Data class for jokes that complies with https://v2.jokeapi.dev/
 */
@Data
@Builder
@Jacksonized
public class Joke {

    private boolean error;
    private String category;
    private JokeType type;
    private String joke;
    private JokeFlags flags;
    private int id;
    private boolean safe;
    private String lang;

    public enum JokeType {
        SINGLE, TWOPART;

        @JsonCreator
        public static JokeType fromString(String type) {
            if (type == null) {
                return null;
            }
            return JokeType.valueOf(type.toUpperCase());
        }

        @JsonValue
        public String jsonValue() {
            return name().toLowerCase();
        }
    }

    @Data
    @Builder
    @Jacksonized
    public static class JokeFlags {
        private boolean nsfw;
        private boolean religious;
        private boolean political;
        private boolean racist;
        private boolean sexist;
        private boolean explicit;
    }
}
