package io.github.andia92.serverless.functions;


import io.github.andia92.serverless.models.Group;
import io.github.andia92.serverless.models.Node;
import io.github.andia92.serverless.models.Server;
import io.github.andia92.serverless.models.ServerLink;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GraphBuilder implements BiFunction<String, List<Server>, Group> {

    private final BiFunction<Server, List<Server>, Optional<ServerLink>> linkConstructor;

    @Override
    public Group apply(@NonNull String groupName, @NonNull List<Server> servers) {
        List<Optional<ServerLink>> links = servers.stream()
                                                  .map(server -> linkConstructor.apply(server, servers))
                                                  .collect(Collectors.toList());

        List<ServerLink> nonEmptyLinks = links.stream()
                                              .filter(Optional::isPresent)
                                              .map(Optional::get)
                                              .collect(Collectors.toList());

        Map<String, Node> nodes = nonEmptyLinks.stream()
                                               .map(link -> {
                                                   String currentNodeHost = link.getNode().getHost();
                                                   if (link.getParent() == null) {
                                                       return new Node(currentNodeHost);
                                                   } else {
                                                       return new Node(currentNodeHost, link.getParent().getHost());
                                                   }
                                               })
                                               .collect(Collectors.toMap(Node::getName, node -> node));


        setChildren(nodes);
        Optional<Node> root = getRootNode(nodes);

        return new Group(groupName, root.orElseThrow(() -> new IllegalArgumentException("Could not create group: " + groupName)));
    }

    private void setChildren(Map<String, Node> nodes) {
        nodes.values()
             .forEach(node -> {
                 String nameOfParent = node.getParent();
                 if (nameOfParent != null) {
                     Node parentNode = nodes.get(nameOfParent);
                     if (parentNode != null) {
                         parentNode.addChild(node);
                     }
                 }
             });
    }

    private Optional<Node> getRootNode(Map<String, Node> nodes) {
        return nodes.values()
                    .stream()
                    .filter(node -> node.getParent() == null)
                    .findFirst();
    }
}