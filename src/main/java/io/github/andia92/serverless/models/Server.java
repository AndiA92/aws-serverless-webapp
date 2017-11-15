package io.github.andia92.serverless.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Data
public class Server {

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
