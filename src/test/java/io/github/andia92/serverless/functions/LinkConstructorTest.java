package io.github.andia92.serverless.functions;

import io.github.andia92.serverless.ServerState;
import io.github.andia92.serverless.functions.LinkConstructor;
import io.github.andia92.serverless.models.Server;
import io.github.andia92.serverless.models.ServerLink;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;
import java.util.function.Function;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LinkConstructorTest {

    private LinkConstructor linkConstructor;

    @Mock
    private ServerRetriever serverRetriever;

    @Before
    public void before() {
        linkConstructor = new LinkConstructor(serverRetriever);
    }

    @Test(expected = IllegalArgumentException.class)
    public void applyNullServerThrowsException() {
        linkConstructor.apply(null);
    }

    @Test
    public void applyServerHasParent_StateIsBOTH_LinkToParentIsReturned() {
        final String serverHost = "host1", parentHost = "host2";
        final Server serverMock = mock(Server.class);
        final Server parentMock = mock(Server.class);
        when(serverMock.getHost()).thenReturn(serverHost);
        when(parentMock.getHost()).thenReturn(parentHost);
        when(serverMock.getRemoteEnd()).thenReturn(parentHost);
        when(serverMock.getState()).thenReturn(ServerState.BOTH.getState());

        when(serverRetriever.apply(parentHost)).thenReturn(Optional.of(parentMock));

        final Optional<ServerLink> actualParent = linkConstructor.apply(serverMock);
        final ServerLink expectedLink = new ServerLink(parentMock, serverMock);
        Assert.assertTrue(actualParent.isPresent());
        Assert.assertEquals(expectedLink, actualParent.orElseThrow(RuntimeException::new));
    }

    @Test
    public void applyServerHasParent_StateIsNone_EmptyIsReturned() {
        final String serverHost = "host1", parentHost = "host2";
        final Server serverMock = mock(Server.class);
        final Server parentMock = mock(Server.class);
        when(serverMock.getHost()).thenReturn(serverHost);
        when(parentMock.getHost()).thenReturn(parentHost);
        when(serverMock.getRemoteEnd()).thenReturn(parentHost);
        when(serverMock.getState()).thenReturn(ServerState.NONE.getState());

        when(serverRetriever.apply(parentHost)).thenReturn(Optional.of(parentMock));

        final Optional<ServerLink> actualParent = linkConstructor.apply(serverMock);
        Assert.assertFalse(actualParent.isPresent());
    }

    @Test
    public void applyServerDoesNotHaveParent_StateIsBoth_EmptyIsReturned(){
        final String serverHost = "host1";
        final String unknown = "unknown";
        final Server serverMock = mock(Server.class);
        when(serverMock.getHost()).thenReturn(serverHost);
        when(serverMock.getRemoteEnd()).thenReturn(unknown);
        when(serverMock.getState()).thenReturn(ServerState.BOTH.getState());

        when(serverRetriever.apply(unknown)).thenReturn(Optional.empty());

        final Optional<ServerLink> actualParent = linkConstructor.apply(serverMock);
        Assert.assertFalse(actualParent.isPresent());
    }

    @Test
    public void applyServerDoesNotHaveParent_StateIsNonde_EmptyIsReturned(){
        final String serverHost = "host1";
        final String unknown = "unknown";
        final Server serverMock = mock(Server.class);
        when(serverMock.getHost()).thenReturn(serverHost);
        when(serverMock.getRemoteEnd()).thenReturn(unknown);
        when(serverMock.getState()).thenReturn(ServerState.NONE.getState());

        when(serverRetriever.apply(unknown)).thenReturn(Optional.empty());

        final Optional<ServerLink> actualParent = linkConstructor.apply(serverMock);
        Assert.assertFalse(actualParent.isPresent());
    }
}
