package com.sewell.qqbot.game.idiom.entity;

import com.sewell.qqbot.game.idiom.GameCode;
import lombok.Data;

/**
 * @author sewell
 * @date 2022/10/7 1:30 PM
 */
@Data

public class UserGameInfo {


    /**
     * 处于游戏阶段：1 选择难度 2 选择先后手 3 正在接龙
     **/
    private GameCode.GamePhase phase;

    /**
     * 难度：1 低 2 中 3 高
     **/
    private Integer difficulty;

    private String lastWord;

    /**
     * 累计答错次数
     **/
    private Integer playerLoseNum = 0;

    /**
     * 连续答对次数
     **/
    private Integer playerStreakWinNum = 0;
//    /**
//     * 接龙阶段：1 用户 2 机器人先手
//     **/
//    private String turn;



    public UserGameInfo() {
        this.phase = GameCode.GamePhase.SELECT_DIFFICULTY;
    }

    public void plusLoseNum() {
        playerLoseNum++;
    }
    public void plusWinNum() {
        playerStreakWinNum++;
    }
}
