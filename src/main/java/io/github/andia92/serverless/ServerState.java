package io.github.andia92.serverless;

import lombok.Getter;

import java.util.Arrays;

public enum ServerState {
    NONE("00"),
    LOCAL("10"),
    REMOTE("01"),
    BOTH("11");

    @Getter
    private final String state;

    ServerState(String state) {
        this.state = state;
    }
}
