package com.sewell.qqbot.game.idiom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author sewell
 * @date 2022/10/7 3:29 PM
 */
public class GameCode {

    @RequiredArgsConstructor
    @Getter
    public enum GamePhase{
        SELECT_DIFFICULTY,
        SELECT_ORDER,
        PLAYING;
    }


    @RequiredArgsConstructor
    @Getter
    public enum GameDifficulty {
        SIMPLE(1, 3, 5,true,"简单"),
        MIDDLE(2, 5,3,true,"中等"),
        HARD(3, 10,1,false,"困难"),
        ;


        private final Integer code;
        private final Integer rightNum;
        private final Integer failNum;
        private final Boolean allowFurtherSearch;
        private final String desc;

        public static GameDifficulty acquireByCode(final Integer code) {
            return Arrays.stream(GameDifficulty.values())
                    .filter(e -> Objects.equals(e.code, code))
                    .findFirst().orElseThrow(() -> new RuntimeException(String.format("无效的难度 %s", code)));
        }
    }


}
