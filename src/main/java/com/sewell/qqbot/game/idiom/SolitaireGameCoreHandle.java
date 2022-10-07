package com.sewell.qqbot.game.idiom;

import com.alibaba.fastjson.JSON;
import com.sewell.qqbot.api.ApiManager;
import com.sewell.qqbot.entity.Message;
import com.sewell.qqbot.game.BotGame;
import com.sewell.qqbot.game.GameHelper;
import com.sewell.qqbot.game.constant.SolitaireGame;
import com.sewell.qqbot.game.idiom.entity.Hanzi;
import com.sewell.qqbot.game.idiom.entity.Idiom;
import com.sewell.qqbot.game.idiom.entity.UserGameInfo;
import com.sewell.qqbot.util.DataUtil;
import com.sewell.qqbot.util.PinyinHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author sewell
 * @date 2022/10/7 1:17 PM
 */
@Service
@Slf4j
public class SolitaireGameCoreHandle implements BotGame, ApplicationRunner {

    @Resource
    private ApiManager api;



    /**
     * 存储某个拼音(无声调)对应的所有汉字
     * 如{"wang":['汪','王','网','忘',...]}
     */
    protected static Map<String, Set<Character>> pinyinZiListMap = new HashMap<>();

    /**
     * 存储以某个汉字开头的所有成语
     * 如以'我'开头的成语：{‘我’：["我黼子佩","我负子戴","我见犹怜","我武惟扬","我心如秤","我行我素","我醉欲眠"]}
     */
    protected static Map<Character, List<String>> initialWordListMap = new HashMap<>();

    /**
     * 存储每个成语对应的详细信息
     * 如{"我行我素":{"abbreviation":"wxws","derivation":...,"example":...,"explanation":...,"hanziList":[...]}}
     */
    protected static Map<String, Idiom> wordIdiomMap = new HashMap<>();

    /**
     * 存储开始玩家游戏数据
     */
    protected static Map<String, UserGameInfo> userGameMap = new HashMap<>();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 读取数据文件
        loadData();
    }



    @Override
    public void handle(Message message) {
        String content = message.getContent();
        String[] args = content.split(" ");
        String command = args[0].trim();
        String gameUserKey = GameHelper.cacheUserGameKey(message);

        String reply = "";
        if (command.startsWith("/")) {
            //命令
            SolitaireGame.SolitaireGameEnum solitaireGameEnum = SolitaireGame.SolitaireGameEnum.acquireByName(command);
            switch (solitaireGameEnum) {
                case SOLITAIRE_START -> {
                    //判断游戏是否已经开始
                    reply = startGame(gameUserKey);
                }
                case SOLITAIRE_STOP -> {
                    reply = stopGame(gameUserKey);
                }
                case SOLITAIRE_DIFFICULTY_SIMPLE -> {
                    reply = selectDifficulty(gameUserKey, GameCode.GameDifficulty.SIMPLE);
                }
                case SOLITAIRE_DIFFICULTY_MID -> {
                    reply = selectDifficulty(gameUserKey, GameCode.GameDifficulty.MIDDLE);
                }
                case SOLITAIRE_DIFFICULTY_HARD -> {
                    reply = selectDifficulty(gameUserKey, GameCode.GameDifficulty.HARD);
                }
                case SOLITAIRE_ORDER_PLAYER_FIRST -> {
                    reply = selectOrder(gameUserKey, Boolean.TRUE);
                }
                case SOLITAIRE_ORDER_PLAYER_LAST -> {
                    reply = selectOrder(gameUserKey, Boolean.FALSE);
                }
                case SOLITAIRE_WORD_DESC ->{
                    reply = wordDesc(gameUserKey);
                }
                case SOLITAIRE_PROGRESS -> {
                    reply = progressQuery(gameUserKey);
                }
                default -> {
                    reply = GameMessage.INVALID_CMD;
                }
            }
        } else {
            //答题模式
            reply = playingGame(gameUserKey, command);
        }

        String channelId = message.getChannelId();
        String messageId = message.getId();
        //回复内容
        api.getMessageApi().sendMessageReference(channelId, reply, messageId);
    }

    private String progressQuery(String gameUserKey) {
        String reply = "";
        UserGameInfo userGameInfo = checkInGame(gameUserKey);
        if (userGameInfo == null) {
            reply = GameMessage.NOT_BEGIN;
            return reply;
        }
        reply = String.format(GameMessage.QUERY_PROGRESS, userGameInfo.getPlayerStreakWinNum(), userGameInfo.getPlayerLoseNum());
        return reply;
    }

    private String wordDesc(String gameUserKey) {
        String reply = "";
        UserGameInfo userGameInfo = checkInGame(gameUserKey);
        if (userGameInfo == null) {
            reply = GameMessage.NOT_BEGIN;
            return reply;
        }
        if (StringUtils.isBlank(userGameInfo.getLastWord())) {
            reply = GameMessage.NO_LAST_WORD;
            return reply;
        }
        Idiom idiom = wordIdiomMap.get(userGameInfo.getLastWord());
        if (idiom == null) {
            reply = String.format(GameMessage.NOT_FOUND_WORD, userGameInfo.getLastWord());
            return reply;
        }
        reply = printIdiom(idiom);
        return reply;
    }

    private String printIdiom(final Idiom idiom) {
        return String.format(GameMessage.IDIOM_DICT, idiom.getWord()
                , idiom.getPinyin(), idiom.getExplanation()
                , idiom.getDerivation(), idiom.getExample());
    }

    private String playingGame(String gameUserKey, String thisPlayerWord) {
        String reply = "";
        UserGameInfo userGameInfo = checkInGame(gameUserKey);
        if (userGameInfo == null) {
            reply = GameMessage.NOT_BEGIN;
            return reply;
        }
        if (GameCode.GamePhase.SELECT_DIFFICULTY.equals(userGameInfo.getPhase())) {
            reply = GameMessage.CHOOSE_DIFFICULTY_ALERT;
            return reply;
        }
        if (GameCode.GamePhase.SELECT_ORDER.equals(userGameInfo.getPhase())) {
            reply = GameMessage.CHOOSE_ORDER_ALERT;
            return reply;
        }
        GameCode.GameDifficulty gameDifficulty = userGameInfo.getDifficulty();
        //成语不合法
        if (!isValidIdiom(thisPlayerWord)) {
            userGameInfo.plusLoseNum();
            userGameInfo.setPlayerStreakWinNum(0);
            reply = String.format(GameMessage.NOT_IDIOM, thisPlayerWord);
            //判断答错次数
            if (playerLost(userGameInfo.getPlayerLoseNum(), gameDifficulty)) {
                reply += String.format(GameMessage.LOSE_FAILED, userGameInfo.getPlayerLoseNum());
                gameOver(gameUserKey);
            }
            return reply;
        }
        //成语不符合接龙规则
        if (!isSolitaire(thisPlayerWord, userGameInfo.getLastWord(), gameDifficulty.getAllowFurtherSearch())) {
            userGameInfo.plusLoseNum();
            userGameInfo.setPlayerStreakWinNum(0);
            reply = String.format(GameMessage.NOT_MATCH_RULE,thisPlayerWord);
            //判断答错次数
            if (playerLost(userGameInfo.getPlayerLoseNum(), gameDifficulty)) {
                reply += String.format(GameMessage.LOSE_FAILED, userGameInfo.getPlayerLoseNum());
                gameOver(gameUserKey);
            }
            return reply;
        }

        if (userGameInfo.getLastWord() != null) {
            // 玩家先手的情况下第一个成语不算答对
            userGameInfo.plusWinNum();
        }

        if (playerWon(userGameInfo.getPlayerStreakWinNum(), gameDifficulty)) {
            reply = String.format(GameMessage.RIGHT_WIN, userGameInfo.getPlayerStreakWinNum());
            gameOver(gameUserKey);
            return reply;
        }

        String thisComputerWord;
        Idiom idiom = getNextIdiom(thisPlayerWord, gameDifficulty.getAllowFurtherSearch());
        if (idiom == null) {
            reply = GameMessage.AI_FAILED;
            gameOver(gameUserKey);
            return reply;
        } else {
            thisComputerWord = idiom.getWord();
        }

        if (thisComputerWord == null) {
            reply = GameMessage.AI_FAILED;
            gameOver(gameUserKey);
            return reply;
        } else {
            reply = thisComputerWord;
        }
        userGameInfo.setLastWord(thisComputerWord);
        return reply;
    }


    /**
     * 开始游戏
     * 1、判断是否在游戏中
     * 2、选择难度
     */
    public String startGame(String gameUserKey) {
        String reply = "";
        if (checkInGame(gameUserKey) != null) {
            //已经在游戏当中
            reply = GameMessage.GAME_ALREADY_BEGIN;
        } else {
            //不在游戏当中
            userGameMap.put(gameUserKey, new UserGameInfo());
            //请选择游戏难度
            reply = String.format(GameMessage.CHOOSE_DIFFICULTY, GameCode.GameDifficulty.SIMPLE.getRightNum(), GameCode.GameDifficulty.SIMPLE.getFailNum(),
                    GameCode.GameDifficulty.MIDDLE.getRightNum(), GameCode.GameDifficulty.MIDDLE.getFailNum(),
                    GameCode.GameDifficulty.HARD.getRightNum(), GameCode.GameDifficulty.HARD.getFailNum());
        }
        return reply;
    }
    private String stopGame( String gameUserKey) {
        String reply = "";
        if (checkInGame(gameUserKey) != null) {
            //停止游戏
            userGameMap.remove(gameUserKey);
            GameHelper.userPlayGame.remove(gameUserKey);
            reply = GameMessage.GAME_ALREADY_STOP;
        } else {
            reply = GameMessage.NOT_BEGIN;
        }
        return reply;
    }

    private void gameOver( String gameUserKey) {
        if (checkInGame(gameUserKey) != null) {
            //停止游戏
            userGameMap.remove(gameUserKey);
            GameHelper.userPlayGame.remove(gameUserKey);
        }
    }

    public String selectDifficulty(String gameUserKey, GameCode.GameDifficulty difficulty) {
        String reply = "";
        UserGameInfo userGameInfo = checkInGame(gameUserKey);
        if (userGameInfo != null) {
            GameCode.GamePhase phase = userGameInfo.getPhase();
            if (GameCode.GamePhase.SELECT_DIFFICULTY.equals(phase)) {
                //选择难度
                userGameInfo.setDifficulty(difficulty);
                userGameInfo.setPhase(GameCode.GamePhase.SELECT_ORDER);
                reply = String.format(GameMessage.CHOOSE_ORDER, difficulty.getDesc());
            } else {
                reply = String.format(GameMessage.NO_CHOOSE_DIFFICULTY_AGAIN, userGameInfo.getDifficulty().getDesc());
            }
        } else {
            reply = GameMessage.NOT_BEGIN;
        }
        return reply;
    }

    private String selectOrder(String gameUserKey, Boolean playerFirst) {
        String reply = "";
        UserGameInfo userGameInfo = checkInGame(gameUserKey);
        if (userGameInfo != null) {
            GameCode.GamePhase phase = userGameInfo.getPhase();
            if (GameCode.GamePhase.SELECT_ORDER.equals(phase)) {
                //选择难度
                userGameInfo.setPhase(GameCode.GamePhase.PLAYING);
                if (playerFirst) {
                    //用户先出
                    reply = GameMessage.PLEASE_USER_BEGIN;
                } else {
                    //机器人先出
                    String idiom = pickRandomIdiom();
                    reply = String.format(GameMessage.BOT_GAME_START, idiom);
                    userGameInfo.setLastWord(idiom);
                }
            } else {
                reply = GameMessage.INVALID_CHOOSE;
            }
        } else {
            reply = GameMessage.NOT_BEGIN;
        }
        return reply;
    }

    private UserGameInfo checkInGame(String gameUserKey) {
        return userGameMap.get(gameUserKey);
    }


    private void loadData() {
        String data = DataUtil.readData();
        if (data == null) {
            throw new RuntimeException("无法读取数据文件");
        }

        List<Idiom> idioms = null;
        try {
            idioms = JSON.parseArray(data, Idiom.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (idioms == null || idioms.size() < 1) {
            System.err.println("数据文件非法!");
            return;
        }

        for (Idiom idiom : idioms) {
            String word = idiom.getWord();
            String pinyin = idiom.getPinyin();
            if (word == null || word.length() < 1 || pinyin == null || pinyin.length() < 1) {
                continue;
            }
            String[] pinyinArr = pinyin.split(" ");
            if (word.length() != pinyinArr.length) {
                continue;
            }

            Character initial = null;
            List<Hanzi> hanziList = new ArrayList<>();
            for (int i = 0; i < word.length(); i++) {
                Hanzi hanzi = new Hanzi();
                char zi = word.charAt(i);
                String pinyinWithoutTone = PinyinHelper.getPinyinWithoutTone(pinyinArr[i]);
                hanzi.setZi(zi);
                hanzi.setPinyin(pinyinWithoutTone);
                hanziList.add(hanzi);
                if (i == 0) {
                    initial = zi;
                }

                if (pinyinZiListMap.containsKey(pinyinWithoutTone)) {
                    pinyinZiListMap.get(pinyinWithoutTone).add(zi);
                } else {
                    Set<Character> ziSet = new HashSet<>();
                    ziSet.add(zi);
                    pinyinZiListMap.put(pinyinWithoutTone, ziSet);
                }
            }
            idiom.setHanziList(hanziList);
            wordIdiomMap.put(word, idiom);

            if (initialWordListMap.containsKey(initial)) {
                initialWordListMap.get(initial).add(word);
            } else {
                List<String> list = new ArrayList<>();
                list.add(word);
                initialWordListMap.put(initial, list);
            }
        }
    }

    private String pickRandomIdiom() {
        List<String> words = new ArrayList<>(wordIdiomMap.keySet());
        int idx = (int) (words.size() * Math.random());
        return words.get(idx);
    }

    private boolean isValidIdiom(final String word) {
        if (word == null) {
            return false;
        }
        return wordIdiomMap.get(word) != null;
    }

    /**
     * 连续答对次数超过设定，则玩家赢了
     **/
    private boolean playerWon(final int playerStreakWinNum, final GameCode.GameDifficulty gameDifficulty) {
        return playerStreakWinNum >= gameDifficulty.getRightNum();
    }

    /**
     * 累计答错次数超过设定，则玩家输了
     **/
    private boolean playerLost(final int playerLoseNum, final GameCode.GameDifficulty gameDifficulty) {

        return playerLoseNum >= gameDifficulty.getFailNum();
    }

    private boolean isSolitaire(final String thisWord, final String lastWord, final boolean allowFurtherSearch) {
        if (thisWord == null || thisWord.length() < 1) {
            return false;
        }
        if (lastWord == null) {
            return true;
        }
        if (lastWord.length() < 1) {
            return false;
        }
        if (thisWord.equals(lastWord)) {
            //不允许与电脑的成语相同
            return false;
        }

        char lastCharOfLastWord = lastWord.charAt(lastWord.length()-1);
        char firstCharOfThisWord = thisWord.charAt(0);
        if (lastCharOfLastWord == firstCharOfThisWord) {
            return true;
        }

        //不允许同音不同调
        if (!allowFurtherSearch) {
            return false;
        }
        //允许同音不同调
        Idiom thisIdiom = wordIdiomMap.get(thisWord);
        Idiom lastIdiom = wordIdiomMap.get(lastWord);
        if (thisIdiom == null || thisIdiom.getHanziList() == null
                || lastIdiom == null || lastIdiom.getHanziList() == null) {
            return false;
        }
        int thisIdiomHanziNum = thisIdiom.getHanziList().size();
        int lastIdiomHanziNum = lastIdiom.getHanziList().size();
        if (thisIdiomHanziNum < 1 || lastIdiomHanziNum < 1) {
            return false;
        }
        String lastPinyinOfLastIdiom = lastIdiom.getHanziList().get(lastIdiomHanziNum-1).getPinyin();
        String firstPinyinOfThisIdiom = thisIdiom.getHanziList().get(0).getPinyin();
        if (lastPinyinOfLastIdiom != null && lastPinyinOfLastIdiom.equals(firstPinyinOfThisIdiom)) {
            return true;
        }

        return false;
    }

    /** 查找以thisWord成语末字开头的成语。
     *  不能与thisWord重复，若找不到，则查找以与末字同音不同调的字为开头的成语，再找不到则返回null
     */
    private Idiom getNextIdiom(final String thisWord, boolean allowFurtherSearch) {
        if (thisWord == null || thisWord.length() < 1) {
            return null;
        }
        Idiom thisIdiom = wordIdiomMap.get(thisWord);
        if (thisIdiom == null) {
            return null;
        }

        char lastZi = thisWord.charAt(thisWord.length()-1);
        List<String> candidateWordList = initialWordListMap.get(lastZi);
        if (candidateWordList == null || candidateWordList.size() < 1) {
            //查找与last同音不同调的字
            return getNextIdiomFurther(lastZi, thisIdiom, allowFurtherSearch);
        }

        int candidateSize = candidateWordList.size();
        if (candidateSize == 1 && thisWord.equals(candidateWordList.get(0))) {
            //查找与last同音不同调的字
            return getNextIdiomFurther(lastZi, thisIdiom, allowFurtherSearch);
        }
        int idx = (int) (candidateSize * Math.random());
        String nextWord = candidateWordList.get(idx);
        int loopNum = 0;
        while (loopNum < 100) {
            //最多循环100次，防止死循环
            if (!thisWord.equals(nextWord)) {
                return wordIdiomMap.get(nextWord);
            }
            idx = (int) (candidateSize * Math.random());
            nextWord = candidateWordList.get(idx);
            loopNum++;
        }
        return null;
    }

    /** 查找与lastZi同音不同调的字为开头的成语
     */
    private Idiom getNextIdiomFurther(final char lastZi, final Idiom thisIdiom, boolean allowFurtherSearch) {
        if (!allowFurtherSearch) {
            return null;
        }
        if (thisIdiom == null) {
            return null;
        }
        List<Hanzi> hanziList = thisIdiom.getHanziList();
        if (hanziList == null || hanziList.size() < 1) {
            return null;
        }
        String pinyinOfLast = null;
        for (Hanzi hanzi : hanziList) {
            if (hanzi.getZi() == lastZi) {
                pinyinOfLast = hanzi.getPinyin();
                break;
            }
        }
        if (pinyinOfLast == null) {
            return null;
        }

        Set<Character> ziSet = pinyinZiListMap.get(pinyinOfLast);
        if (ziSet == null || ziSet.size() < 1) {
            return null;
        }
        int size = ziSet.size();
        if (size == 1 && ziSet.contains(lastZi)) {
            return null;
        }
        List<Character> ziList = new ArrayList<>(ziSet);
        int idx = (int) (size * Math.random());
        Character zi = ziList.get(idx);
        int loopNum = 0;
        while (loopNum < 100) {
            if (lastZi != zi) {
                List<String> wordList = initialWordListMap.get(zi);
                if (wordList != null && wordList.size() > 0) {
                    return wordIdiomMap.get(wordList.get(0));
                }
            }
            idx = (int) (size * Math.random());
            zi = ziList.get(idx);
            loopNum++;
        }

        return null;
    }
}
