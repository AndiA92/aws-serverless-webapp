package io.github.andia92.serverless.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode
@AllArgsConstructor
@RequiredArgsConstructor
public class ServerLink {

    @Getter
    private final Server node;

    @Getter
    private Server parent;
}
