package io.github.andia92.serverless.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class ServerLink {

    @Getter
    private final Server node;

    @Getter
    private Server parent;

    public ServerLink(Server node) {
        this.node = node;
    }

    public ServerLink(Server node, Server parent) {
        this.node = node;
        this.parent = parent;
    }
}
