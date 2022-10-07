package com.sewell.qqbot.game;

import com.sewell.qqbot.entity.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sewell
 * @date 2022/10/7 1:58 PM
 */
public class GameHelper {

    /**
     * 正在玩的游戏
     **/
    public final static Map<String, BotGame> userPlayGame = new HashMap<>();


    public final static Map<String, BotGame> cmdMatchGame = new HashMap<>();




    public static String cacheUserGameKey(Message message) {
        return message.getGuildId() + "_" + message.getChannelId() + "_" + message.getAuthor() == null ? "" : message.getAuthor().getId();
    }

}
