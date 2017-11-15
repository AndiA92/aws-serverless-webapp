package io.github.andia92.serverless.functions;


import io.github.andia92.serverless.models.Server;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
public class ServerRetriever implements Function<String, Optional<Server>> {

    private final List<Server> servers;

    @Override
    public Optional<Server> apply(String serverName) {
        return servers.stream()
                      .filter(server -> server.getHost().equals(serverName))
                      .findFirst()
                      .map(server -> server);
    }
}
