import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


class JSONElementBuilder {

    static JSONElement build(List<Server> servers) {
        Map<Server, JSONElement> elements = new HashMap<>();
        Map<String, Server> hostNameServerMapping = new HashMap<>();
        servers.forEach(server -> {
            JSONElement jsonElement = new JSONElement();
            jsonElement.setName(server.getHost());
            elements.put(server, jsonElement);
            hostNameServerMapping.put(server.getHost(), server);
        });
        elements.entrySet().forEach(entry -> {
            Server server = entry.getKey();
            JSONElement element = entry.getValue();
            ServerState serverState = ServerState.getStateByName(server.getState());

            switch (serverState) {
                case REMOTE:
                case BOTH:
                    element.setParent(server.getRemoteEnd());
                    break;
            }
        });

        elements.values().forEach(element -> {
            String parentElementName = element.getParent();
            if (parentElementName != null) {
                Server parentServer = hostNameServerMapping.get(parentElementName);
                JSONElement parentJsonElement = elements.get(parentServer);
                parentJsonElement.setChildren(Collections.singletonList(element));
            }
        });

        return elements.values()
                       .stream()
                       .filter(element -> element.getParent() == null)
                       .findFirst()
                       .orElseGet(null);
    }
}
