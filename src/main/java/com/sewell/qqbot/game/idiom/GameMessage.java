package com.sewell.qqbot.game.idiom;

/**
 * @author sewell
 * @date 2022/10/7 6:43 PM
 */
public interface GameMessage {

    String NOT_BEGIN = "尚未开始游戏，请输入【/成语接龙】开始游戏";

    String NO_LAST_WORD = "尚未进行接龙，无法查询";

    String NOT_FOUND_WORD = "【%s】 找不到该成语!";

    String CHOOSE_DIFFICULTY_ALERT = "请先选择难度";

    String CHOOSE_ORDER_ALERT = "请先选择先后手";

    String NOT_IDIOM = "【%s】不是成语!";

    String LOSE_FAILED = "\n" +
            "累计答错%s次，你输了!";

    String NOT_MATCH_RULE = "【%s】不符合接龙规则!";

    String RIGHT_WIN = "连续答对%s次，你赢了!";

    String AI_FAILED = "我想不出来了:( 你赢了!";

    String GAME_ALREADY_BEGIN = "您已经开始游戏，请继续";

    String CHOOSE_DIFFICULTY = "请选择成语接龙游戏难度：\n" +
            "【简单】答对%s个成语或AI接龙失败则玩家获胜，累计答错%s个成语则玩家失败，允许同音不同调 \n" +
            "【中等】连续答对%s个成语或AI接龙失败则玩家获胜，累计答错%s个成语则玩家失败，允许同音不同调 \n" +
            "【困难】连续答对%s个成语或AI接龙失败则玩家获胜，累计答错%s个成语则玩家失败，不允许同音不同调";

    String GAME_ALREADY_STOP = "游戏已停止，感谢您的使用！";

    String CHOOSE_ORDER = "已选择难度，难度设置为：【%s】\n" +
            "请选择先后手：\n" +
            "【先手】己方先出\n" +
            "【后手】机器人先出";

    String NO_CHOOSE_DIFFICULTY_AGAIN = "已选择过难度，当前难度为:【%s】请勿重新选择";


    String PLEASE_USER_BEGIN = "请您开始";

    String BOT_GAME_START = "游戏开始喽！\n%s";


    String INVALID_CHOOSE = "无效的选择";

    String QUERY_PROGRESS = "该局游戏中，您连续答对%s次，累计失败%s次！";


    String INVALID_CMD = "无效的命令，请重新输入";

    String IDIOM_DICT = "【%s】\n"
            + "拼音：%s\n"
            + "释义：%s\n"
            + "出处：%s\n"
            + "举例：%s";



}
