package com.sewell.qqbot.util;

import com.alibaba.fastjson.JSON;
import com.sewell.qqbot.websocket.entity.Gateway;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

/**
 * @author sewell
 * @date 2022/10/6 5:00 PM
 */
@Slf4j
public class BotUtil {

    public static String getToken(Integer botAppId, String botToken) {
        return "Bot " + botAppId + "." + botToken;
    }

    public static String getApiBase(Boolean useSandBoxMode) {
        String apiBase = "https://api.sgroup.qq.com";
        if (useSandBoxMode) {
            apiBase = "https://sandbox.api.sgroup.qq.com";
        }
        return apiBase;
    }

    public static Gateway getGateway(Integer botAppId, String botToken, Boolean useSandBoxMode) {
        String token = getToken(botAppId,botToken);
        String apiBase = getApiBase(useSandBoxMode);

        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder()
                .url(apiBase + "/gateway/bot")
                .header("Authorization", token)
                .get()
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            ResponseBody body = response.body();
            if (body == null) {
                System.exit(1);
            }
            String result = body.string();
            log.debug(result);
            return JSON.parseObject(result, Gateway.class);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }
}
