package com.sewell.qqbot.websocket;

import com.alibaba.fastjson.JSON;
import com.sewell.qqbot.properties.BotConfig;
import com.sewell.qqbot.util.BotUtil;
import com.sewell.qqbot.websocket.entity.Payload;
import com.sewell.qqbot.websocket.enums.OpEnum;
import com.sewell.qqbot.websocket.subscribe.DataSubscriber;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.List;
import java.util.Optional;

/**
 * websocket client.
 */
@Slf4j
public final class BotWebsocketClient extends WebSocketClient {
    private final WebsocketDataHandler websocketDataHandler;
    @Getter
    private final ClientData clientData;
    private Boolean connected = false;
    public BotWebsocketClient(URI serverUri, BotConfig botConfig,final List<DataSubscriber> dataSubscriberList) {
        super(serverUri);
        this.websocketDataHandler = new WebsocketDataHandler(dataSubscriberList);
        clientData = ClientData.builder()
                .shard(botConfig.getShard())
                .totalShard(botConfig.getTotalShard())
                .token(BotUtil.getToken(botConfig.getBotAppId(),botConfig.getBotToken()))
                .intents(botConfig.getIntents())
                .build();
    }


    @Override
    public void onOpen(final ServerHandshake serverHandshake) {
        Thread.currentThread().setName("WebSocket");
        log.info("已连接至网关!");
        connected = true;
    }
    
    @Override
    public void onMessage(final String result) {
        handleResult(result);
    }
    
    @Override
    public void onClose(final int code, final String reason, final boolean remote) {
        this.onClientClose(code, reason, remote);
    }
    
    @Override
    public void onError(final Exception e) {
        this.close();
    }
    
    @SuppressWarnings("ALL")
    private void handleResult(final String message) {
        log.debug("收到消息：{}", message);
        Payload payload = JSON.parseObject(message, Payload.class);
        Optional.ofNullable(payload.getS())
                .ifPresent(o -> this.clientData.setSeq(o));

        OpEnum opEnum = OpEnum.acquireByName(payload.getOp());
        websocketDataHandler.executor(opEnum, payload);
    }


    public void onClientClose(int code, String reason, boolean remote) {
        switch (code) {
            case 4001:
                log.warn("服务端关闭连接, 原因 {} {}({})", code, "无效的 opcode", reason);
                break;
            case 4002:
                log.warn("服务端关闭连接, 原因 {} {}({})", code, "无效的 payload", reason);
                break;
            case 4007:
                log.warn("服务端关闭连接, 原因 {} {}({})", code, "seq 错误", reason);
                break;
            case 4008:
                log.warn("服务端关闭连接, 原因 {} {}({})", code, "发送 payload 过快，请重新连接，并遵守连接后返回的频控信息", reason);
                break;
            case 4009:
                log.warn("服务端关闭连接, 原因 {} {}({})", code, "连接过期，请重连", reason);
                break;
            case 4010:
                log.error("服务端关闭连接, 原因 {} {}({})", code, "无效的 shard", reason);
                System.exit(code);
                break;
            case 4011:
                log.error("服务端关闭连接, 原因 {} {}({})", code, "连接需要处理的频道过多，请进行合理的分片", reason);
                System.exit(code);
                break;
            case 4012:
                log.error("服务端关闭连接, 原因 {} {}({})", code, "无效的 version", reason);
                System.exit(code);
                break;
            case 4013:
                log.error("服务端关闭连接, 原因 {} {}({})", code, "无效的 intent", reason);
                System.exit(code);
                break;
            case 4014:
                log.error("服务端关闭连接, 原因 {} {}({})", code, "intent 无权限", reason);
                System.exit(code);
                break;
            case 4914:
                log.error("服务端关闭连接, 原因 {} {}({})", code, "机器人已下架,只允许连接沙箱环境,请断开连接,检验当前连接环境", reason);
                System.exit(code);
                break;
            case 4915:
                log.error("服务端关闭连接, 原因 {} {}({})", code, "机器人已封禁,不允许连接,请断开连接,申请解封后再连接", reason);
                System.exit(code);
                break;
            default:
                this.getClientData().setSessionId(null);
                if (remote) {
                    log.warn("服务端关闭连接, 原因 {} {}", code, reason);
                } else {
                    log.warn("客户端关闭连接, 原因 {} {}", code, reason);
                }
        }

        log.info("5秒后开始尝试恢复连接...");
        try {
            Thread.sleep(5000);
            new Thread(() -> reconnect(code)).start();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("重新连接失败,请检查网络!");
        }
    }

    private void reconnect(Integer code) {
        log.info("正在重新连接...");
        if (code != 4009) {
            this.getClientData().setSessionId(null);
        }
        reconnect();
    }
}
