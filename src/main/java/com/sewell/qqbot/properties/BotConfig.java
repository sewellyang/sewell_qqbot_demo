package com.sewell.qqbot.properties;

import com.sewell.qqbot.websocket.enums.Intent;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 机器身份信息
 */
@Data
@ConfigurationProperties(prefix = "bot.config")
@Component
public class BotConfig {
    /**
     * 管理端的 BotAppID
     */
    private Integer botAppId;
    /**
     * 管理端的 BotToken
     */
    private String botToken;
    /**
     * 管理端的 BotSecret
     */
    private String botSecret;

    /**
     * 是否使用沙箱模式
     */
    private Boolean useSandBoxMode = false;

    /**
     * 当前连接的分片数
     */
    private Integer shard;

    /**
     * 总分片数
     */
    private Integer totalShard;

    private List<Intent> intents;

    /**
     * 使用沙箱模式
     */
    public void useSandBoxMode() {
        this.useSandBoxMode = true;
    }

    /**
     * 设置沙箱模式
     *
     * @param useSandBoxMode 沙箱模式
     * @deprecated 使用 {@link #useSandBoxMode()} 替代
     */
    public void setUseSandBoxMode(Boolean useSandBoxMode) {
        this.useSandBoxMode = useSandBoxMode;
    }
}
