package com.sewell.qqbot.game.idiom.entity;

import lombok.Data;

import java.util.List;

/**
 * @description 成语
 */
@Data
public class Idiom {
    //成语
    private String word;
    //拼音（带音调）
    private String pinyin;
    //英文缩写
    private String abbreviation;
    //出处
    private String derivation;
    //释义
    private String explanation;
    //举例
    private String example;
    //汉字列表
    private List<Hanzi> hanziList;
}
