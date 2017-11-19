package io.github.andia92.serverless.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
public class Node {

    @JsonProperty("name")
    @Getter
    private final String name;

    @JsonProperty("parent")
    @Getter
    private String parent;

    @JsonProperty("children")
    @Getter
    private final List<Node> children = new ArrayList<>();

    public void addChild(Node node) {
        children.add(node);
    }
}
