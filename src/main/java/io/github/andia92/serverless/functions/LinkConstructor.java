package io.github.andia92.serverless.functions;

import io.github.andia92.serverless.models.Server;
import io.github.andia92.serverless.models.ServerLink;
import io.github.andia92.serverless.ServerState;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

@AllArgsConstructor
public class LinkConstructor implements BiFunction<Server, List<Server>, Optional<ServerLink>> {

    private final BiFunction<String, List<Server>, Optional<Server>> serverRetriever;

    @Override
    public Optional<ServerLink> apply(@NonNull Server server, @NonNull List<Server> servers) {
        final String state = server.getState();
        return remoteEndConnected(state) ?
                serverRetriever.apply(server.getRemoteEnd(), servers)
                               .map(parent -> new ServerLink(server, parent))
                : (server.getRemoteEnd() == null) ? Optional.of(new ServerLink(server)) : Optional.empty();
    }

    private boolean remoteEndConnected(String state) {
        return ServerState.REMOTE.getState().equals(state) || ServerState.BOTH.getState().equals(state);
    }
}