package io.github.andia92.serverless.functions;

import io.github.andia92.serverless.models.Server;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServerRetrieverTest {

    private ServerRetriever retriever;

    private List<Server> servers = initServers();

    @Before()
    public void before() {
        retriever = new ServerRetriever();
    }

    @Test(expected = IllegalArgumentException.class)
    public void retrieveServerFromNulListThrowsException() {
        retriever.apply(anyObject(), null);
    }

    @Test
    public void retrieveExistingServer() {
        String serverName = "host2";
        final Optional<Server> actualServer = retriever.apply(serverName, servers);
        Assert.assertTrue(actualServer.isPresent());
        final Server expectedServer = servers.get(1);
        Assert.assertEquals(expectedServer, actualServer.get());
    }

    @Test
    public void retrieveNullServer_ReturnsEmpty() {
        String serverName = null;
        final Optional<Server> actualServer = retriever.apply(serverName, servers);
        Assert.assertFalse(actualServer.isPresent());
    }

    @Test
    public void retrieveMissingServer_ReturnsEmpty() {
        String serverName = "host4";
        final Optional<Server> actualServer = retriever.apply(serverName, servers);
        Assert.assertFalse(actualServer.isPresent());
    }

    private List<Server> initServers() {
        Server mockServer1 = mock(Server.class);
        Server mockServer2 = mock(Server.class);
        Server mockServer3 = mock(Server.class);

        String host1 = "host1", host2 = "host2", host3 = "host3";

        when(mockServer1.getHost()).thenReturn(host1);
        when(mockServer2.getHost()).thenReturn(host2);
        when(mockServer3.getHost()).thenReturn(host3);

        List<Server> servers = new ArrayList<>();
        servers.addAll(Arrays.asList(mockServer1, mockServer2, mockServer3));
        return servers;
    }
}
