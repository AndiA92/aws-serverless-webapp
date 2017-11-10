package io.github.andia92.serverless;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class NodeBuilder implements Function<List<Server>, List<Group>> {

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

//    static io.github.andia92.serverless.Node build(List<io.github.andia92.serverless.Server> servers) {
//        Map<io.github.andia92.serverless.Server, io.github.andia92.serverless.Node> elements = new HashMap<>();
//        Map<String, io.github.andia92.serverless.Server> hostNameServerMapping = new HashMap<>();
//        servers.forEach(server -> {
//            io.github.andia92.serverless.Node jsonElement = new io.github.andia92.serverless.Node();
//            jsonElement.setName(server.getHost());
//            elements.put(server, jsonElement);
//            hostNameServerMapping.put(server.getHost(), server);
//        });
//        elements.entrySet().forEach(entry -> {
//            io.github.andia92.serverless.Server server = entry.getKey();
//            io.github.andia92.serverless.Node element = entry.getValue();
//            io.github.andia92.serverless.ServerState serverState = io.github.andia92.serverless.ServerState.getStateByName(server.getState());
//
//            switch (serverState) {
//                case REMOTE:
//                case BOTH:
//                    element.setParent(server.getRemoteEnd());
//                    break;
//            }
//        });
//
//        elements.values().forEach(element -> {
//            String parentElementName = element.getParent();
//            if (parentElementName != null) {
//                io.github.andia92.serverless.Server parentServer = hostNameServerMapping.get(parentElementName);
//                io.github.andia92.serverless.Node parentJsonElement = elements.get(parentServer);
//                parentJsonElement.setChildren(Collections.singletonList(element));
//            }
//        });
//
//        return elements.values()
//                       .stream()
//                       .filter(element -> element.getParent() == null)
//                       .findFirst()
//                       .orElseGet(null);
//    }


}
