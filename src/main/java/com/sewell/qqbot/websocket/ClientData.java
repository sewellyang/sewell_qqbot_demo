package com.sewell.qqbot.websocket;

import com.sewell.qqbot.entity.User;
import com.sewell.qqbot.websocket.enums.Intent;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author sewell
 * @date 2022/10/6 6:30 PM
 */
@Data
@Builder
public class ClientData {

    private String token;
    private List<Intent> intents;
    private Integer seq;
    private Integer shard;
    private Integer totalShard;

    private String sessionId;

    private User robotInfo;

}
