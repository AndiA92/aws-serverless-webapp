package io.github.andia92.serverless;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
class Node {

    @JsonProperty("name")
    private final String name;

    @JsonProperty("parent")
    private final String parent;

    @JsonProperty("children")
    private final List<Node> children;
}
