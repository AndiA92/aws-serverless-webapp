package io.github.andia92.serverless.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    public Node(String name) {
        this.name = name;
    }

    public Node(String name, String parent) {
        this.name = name;
        this.parent = parent;
    }

    public void addChild(Node node) {
        children.add(node);
    }
}
