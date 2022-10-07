package com.sewell.qqbot.game;

import com.sewell.qqbot.entity.Message;

/**
 * @author sewell
 * @date 2022/10/7 1:57 PM
 */
public interface BotGame {

    void handle(Message message);
}
