package io.github.andia92.serverless.functions;

import io.github.andia92.serverless.models.Group;
import io.github.andia92.serverless.models.Server;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GroupBuilder implements Function<List<Server>, List<Group>> {

    private final Function<List<Server>, Map<String, List<Server>>> serverGrouper;

    private final BiFunction<String, List<Server>, Group> graphBuilder;

    @Override
    public List<Group> apply(@NonNull List<Server> servers) {
        final Map<String, List<Server>> serversByGroup = serverGrouper.apply(servers);
        return serversByGroup.entrySet()
                .stream()
                .map(entry -> graphBuilder.apply(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }


}
