package com.sewell.qqbot.game.constant;

import com.sewell.qqbot.game.idiom.SolitaireGameCoreHandle;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author sewell
 * @date 2022/10/7 2:06 PM
 */
@Component
public class SolitaireGame implements Commands{

    @RequiredArgsConstructor
    @Getter
    public enum SolitaireGameEnum {
        SOLITAIRE_START("/成语接龙"),
        SOLITAIRE_STOP("/停止接龙"),
        SOLITAIRE_DIFFICULTY_SIMPLE("/简单"),
        SOLITAIRE_DIFFICULTY_MID("/中等"),
        SOLITAIRE_DIFFICULTY_HARD("/困难"),
        SOLITAIRE_ORDER_PLAYER_FIRST("/先手"),
        SOLITAIRE_ORDER_PLAYER_LAST("/后手"),

        SOLITAIRE_WORD_DESC("/成语释义"),
        ;
        private final String cmd;


        public static SolitaireGameEnum acquireByName(final String cmd) {
            return Arrays.stream(SolitaireGameEnum.values())
                    .filter(e -> Objects.equals(e.cmd, cmd))
                    .findFirst().orElseThrow(() -> new RuntimeException(String.format("无效的命令 %s", cmd)));
        }
    }


    @Override
    public Class getBotGame() {
        return SolitaireGameCoreHandle.class;
    }

    @Override
    public Collection<String> getCmds() {
        return Arrays.stream(SolitaireGameEnum.values()).map(o->o.getCmd()).collect(Collectors.toList());
    }


}
