import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
public class Server {

    @JsonProperty("host")
    @Setter
    @Getter
    private String host;

    @JsonProperty("timestamp")
    @Setter
    @Getter
    private String timestamp;

    @JsonProperty("state")
    @Setter
    @Getter
    private String state;

    @JsonProperty("remoteEnd")
    @Setter
    @Getter
    private String remoteEnd;
}
