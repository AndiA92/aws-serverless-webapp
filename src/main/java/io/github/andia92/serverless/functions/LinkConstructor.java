package io.github.andia92.serverless.functions;

import io.github.andia92.serverless.models.Server;
import io.github.andia92.serverless.models.ServerLink;
import io.github.andia92.serverless.ServerState;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.Optional;
import java.util.function.Function;

@AllArgsConstructor
public class LinkConstructor implements Function<Server, Optional<ServerLink>> {

    private final ServerRetriever serverRetriever;

    @Override
    public Optional<ServerLink> apply(@NonNull Server server) {
        final String state = server.getState();
        return remoteEndConnected(state) ?
                serverRetriever.apply(server.getRemoteEnd())
                               .map(p -> new ServerLink(p, server))
                : Optional.empty();
    }

    private boolean remoteEndConnected(String state) {
        return ServerState.REMOTE.getState().equals(state) || ServerState.BOTH.getState().equals(state);
    }
}