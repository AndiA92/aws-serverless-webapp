package io.github.andia92.serverless;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Data
class Server {

    @JsonProperty("group")
    private final String group;

    @JsonProperty("host")
    private final String host;

    @JsonProperty("timestamp")
    private final String timestamp;

    @JsonProperty("state")
    private final String state;

    @JsonProperty("remoteEnd")
    private final String remoteEnd;
}
