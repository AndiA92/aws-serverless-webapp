package io.github.andia92.serverless.functions;


import io.github.andia92.serverless.models.Group;
import io.github.andia92.serverless.models.Node;
import io.github.andia92.serverless.models.Server;
import io.github.andia92.serverless.models.ServerLink;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GraphBuilder implements BiFunction<String, List<Server>, Group> {

    private final LinkConstructor linkConstructor;

    @Override
    public Group apply(@NonNull String groupName, @NonNull List<Server> servers) {
        List<Optional<ServerLink>> links = servers.stream()
                                                  .map(server -> linkConstructor.apply(server, servers))
                                                  .collect(Collectors.toList());
        List<ServerLink> nonEmptyLinks = links.stream()
                                              .filter(Optional::isPresent)
                                              .map(Optional::get)
                                              .collect(Collectors.toList());

        Map<Server, List<ServerLink>> linkToChildrenMap = nonEmptyLinks.stream()
                                                                       .collect(Collectors.groupingBy(ServerLink::getParent));

        return null;
    }
}


// /   static io.github.andia92.serverless.models.Node build(List<io.github.andia92.serverless.Server> servers) {
//        Map<io.github.andia92.serverless.Server, io.github.andia92.serverless.models.Node> elements = new HashMap<>();
//        Map<String, io.github.andia92.serverless.Server> hostNameServerMapping = new HashMap<>();
//        servers.forEach(server -> {
//            io.github.andia92.serverless.models.Node jsonElement = new io.github.andia92.serverless.models.Node();
//            jsonElement.setName(server.getHost());
//            elements.put(server, jsonElement);
//            hostNameServerMapping.put(server.getHost(), server);
//        });
//        elements.entrySet().forEach(entry -> {
//            io.github.andia92.serverless.Server server = entry.getKey();
//            io.github.andia92.serverless.models.Node element = entry.getValue();
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
//                io.githƒcub.andia92.serverless.Node parentJsonElement = elements.get(parentServer);
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
