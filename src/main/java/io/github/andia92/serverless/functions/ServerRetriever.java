package io.github.andia92.serverless.functions;


import io.github.andia92.serverless.models.Server;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

public class ServerRetriever implements BiFunction<String, List<Server>, Optional<Server>> {

    @Override
    public Optional<Server> apply(String serverName, @NonNull List<Server> servers) {
        return servers.stream()
                      .filter(server -> server.getHost().equals(serverName))
                      .findFirst();
    }
}
