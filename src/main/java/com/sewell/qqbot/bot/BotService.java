package com.sewell.qqbot.bot;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sewell.qqbot.properties.BotConfig;
import com.sewell.qqbot.util.BotUtil;
import com.sewell.qqbot.websocket.BotWebsocketClient;
import com.sewell.qqbot.websocket.entity.Gateway;
import com.sewell.qqbot.websocket.entity.Hello;
import com.sewell.qqbot.websocket.entity.Identify;
import com.sewell.qqbot.websocket.entity.Payload;
import com.sewell.qqbot.websocket.enums.Intent;
import com.sewell.qqbot.websocket.enums.OpEnum;
import com.sewell.qqbot.websocket.subscribe.DataSubscriber;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.java_websocket.enums.ReadyState;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author sewell
 * @date 2022/10/6 4:26 PM
 */
@Slf4j
@Service
public class BotService implements AutoCloseable{
    @Getter
    private BotWebsocketClient client;

    private ScheduledThreadPoolExecutor timer;

    @Resource
    private BotConfig botConfig;

    @Resource
    private  List<DataSubscriber> dataSubscriberList;

    @PostConstruct
    public void initClient() {
        //启动进行连接
        start(botConfig, dataSubscriberList);
    }

    private void start(BotConfig botConfig, final List<DataSubscriber> dataSubscriberList) {
        Gateway gateway = BotUtil.getGateway(botConfig.getBotAppId(), botConfig.getBotToken(), botConfig.getUseSandBoxMode());

        if (gateway.getCode() == null) {
            String url = gateway.getUrl();
            log.info("网关地址: {}, 建议分片数: {}", url, gateway.getShards());
            try {
                this.client = new BotWebsocketClient(new URI(url),
                        botConfig,dataSubscriberList);
                boolean success = client.connectBlocking(3000, TimeUnit.MILLISECONDS);
                if (success) {
                    log.info("websocket connection is successful.....");
                } else {
                    log.error("websocket connection is error.....");
                }
            }catch (URISyntaxException e1){
                log.info("websocket url...exception....", e1);
            }catch (InterruptedException e2) {
                log.info("websocket connection...exception....", e2);
                System.exit(1);
            }
        } else {
            log.error("获取 Gateway 失败! {} {}", gateway.getCode(), gateway.getMessage());
            System.exit(gateway.getCode());
        }
    }


    @Override
    public void close() {
        if (Objects.nonNull(this.client) && !client.isClosed()) {
            client.close();
        }
    }

    public void helloHandle(Payload payload) {
        Hello hello = JSON.toJavaObject((JSONObject) payload.getD(), Hello.class);
        if (StringUtils.isBlank(client.getClientData().getSessionId())) {
            log.info("正在发送鉴权...");
            sendIdentify();
        } else {
            log.info("正在发送恢复...");
            sendResumed();
        }
        if (timer != null) {
            timer.shutdown();
        }
        startHeartbeatTimer(hello.getHeartbeatInterval());
    }

    private void sendIdentify() {
        Identify identify = new Identify();
        Integer shard = client.getClientData().getShard();
        Integer totalShard = client.getClientData().getTotalShard();
        if (shard != null && totalShard != null) {
            identify.setShard(Arrays.asList(shard, totalShard));
        }
        int intentsNum = 0;
        for (Intent intent : client.getClientData().getIntents()) {
            intentsNum = intentsNum | intent.getValue();
        }
        identify.setToken(client.getClientData().getToken());
        identify.setIntents(intentsNum);
        Payload identifyPayload = new Payload();
        identifyPayload.setOp(OpEnum.OP_2.getOpCode());
        identifyPayload.setD(identify);
        client.send(JSON.toJSONString(identifyPayload));
    }

    private void sendResumed() {
        JSONObject data = new JSONObject();
        data.put("token", client.getClientData().getToken());
        data.put("session_id", client.getClientData().getSessionId());
        data.put("seq", client.getClientData().getSeq());
        Payload payload = new Payload();
        payload.setOp(OpEnum.OP_6.getOpCode());
        payload.setD(data);
        client.send(JSON.toJSONString(payload));
    }

    private void startHeartbeatTimer(Integer i) {
        timer = new ScheduledThreadPoolExecutor(1);
        timer.scheduleAtFixedRate(() -> {
            if (client.getReadyState() == ReadyState.OPEN) {
                Payload payload = new Payload();
                payload.setOp(OpEnum.OP_1.getOpCode());
                payload.setD(client.getClientData().getSeq());
                log.debug("向服务端发送心跳.");
                client.send(JSON.toJSONString(payload));
            }
        }, i, i, TimeUnit.MILLISECONDS);
    }
}
