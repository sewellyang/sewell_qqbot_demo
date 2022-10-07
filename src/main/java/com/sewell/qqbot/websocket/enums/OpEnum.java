package com.sewell.qqbot.websocket.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/**
 * @ClassName OpEnum
 * @Author sewell
 * @Date 2022/10/6 6:24 PM
 */
@RequiredArgsConstructor
public enum OpEnum {

    DISPATCH(0),
    RECONNECT(7),
    INVALID_SESSION(9),
    HELLO(10),
    HEART_BEAT(11),


    OP_6(6),
    OP_2(2),
    OP_1(1),
    ;

    @Getter
    private final Integer opCode;

    public static OpEnum acquireByName(final Integer code) {
      return Arrays.stream(OpEnum.values())
              .filter(e -> Objects.equals(e.opCode, code))
              .findFirst().orElseThrow(() -> new RuntimeException(String.format(" 未定义的op_code %s", code)));
    }
}