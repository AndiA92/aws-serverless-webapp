package io.github.andia92.serverless.models;


import io.github.andia92.serverless.models.Server;
import lombok.Data;

@Data
public class ServerLink {

    private final Server parent;

    private final Server child;
}
