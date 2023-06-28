package no.lyse.plattform.oauthplayground.client.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

/**
 * Data class for jokes that complies with https://v2.jokeapi.dev/
 * TODO Remove this copy from resource-server and put it in a separate library.
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
