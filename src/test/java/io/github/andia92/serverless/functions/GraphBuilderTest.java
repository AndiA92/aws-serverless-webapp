package io.github.andia92.serverless.functions;


import io.github.andia92.serverless.ServerState;
import io.github.andia92.serverless.models.Group;
import io.github.andia92.serverless.models.Node;
import io.github.andia92.serverless.models.Server;
import io.github.andia92.serverless.models.ServerLink;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GraphBuilderTest {

    private GraphBuilder graphBuilder;

    @Mock
    private LinkConstructor linkConstructor;

    @Before
    public void before() {
        graphBuilder = new GraphBuilder(linkConstructor);
    }

    @Test(expected = IllegalArgumentException.class)
    public void applyNullGroupNameThrowsException() {
        graphBuilder.apply(null, Collections.emptyList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void applyNullServerListThrowsException() {
        graphBuilder.apply("test", null);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void applyConnectedServersInARowReturnsOneConnectedGroup() {
        Server server1 = mock(Server.class);
        Server server2 = mock(Server.class);
        Server server3 = mock(Server.class);

        List<Server> servers = Arrays.asList(server1, server2, server3);

        String host1 = "host1", host2 = "host2", host3 = "host3";
        when(server1.getHost()).thenReturn(host1);
        when(server2.getHost()).thenReturn(host2);
        when(server3.getHost()).thenReturn(host3);

        when(server1.getState()).thenReturn(ServerState.LOCAL.getState());
        when(server2.getState()).thenReturn(ServerState.BOTH.getState());
        when(server3.getState()).thenReturn(ServerState.BOTH.getState());

        when(server1.getRemoteEnd()).thenReturn(null);
        when(server2.getRemoteEnd()).thenReturn(host1);
        when(server3.getRemoteEnd()).thenReturn(host2);

        String groupName = "name";

        when(server1.getGroup()).thenReturn(groupName);
        when(server2.getGroup()).thenReturn(groupName);
        when(server3.getGroup()).thenReturn(groupName);

        ServerLink link10 = mock(ServerLink.class);
        ServerLink link21 = mock(ServerLink.class);
        ServerLink link32 = mock(ServerLink.class);

        when(link10.getNode()).thenReturn(server1);

        when(link21.getNode()).thenReturn(server2);
        when(link21.getParent()).thenReturn(server1);

        when(link32.getNode()).thenReturn(server3);
        when(link32.getParent()).thenReturn(server2);

        when(linkConstructor.apply(server1, servers)).thenReturn(Optional.of(link10));
        when(linkConstructor.apply(server2, servers)).thenReturn(Optional.of(link21));
        when(linkConstructor.apply(server3, servers)).thenReturn(Optional.of(link32));

        Group actualGroup = graphBuilder.apply(groupName, servers);
        Assert.assertEquals(groupName, actualGroup.getName());

        Node actualNode = actualGroup.getEntryPoint();
        Assert.assertEquals(server1.getHost(), actualNode.getName());
    }
}
