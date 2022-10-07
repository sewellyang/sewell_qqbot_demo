package com.sewell.qqbot.websocket.subscribe.dispatch;

import com.sewell.qqbot.websocket.enums.OpEnum;
import com.sewell.qqbot.websocket.subscribe.DataSubscriber;
import lombok.extern.slf4j.Slf4j;


/**
 * @author sewell
 * @date 2022/10/6 8:13 PM
 */
@Slf4j
public abstract class AbstractDispatchSubscriber implements DataSubscriber {



    @Override
    public OpEnum eventType () {
        return OpEnum.DISPATCH;
    }

}

