package com.koodo.eblog.im.server;

import lombok.extern.slf4j.Slf4j;
import org.tio.server.ServerGroupContext;
import org.tio.websocket.server.WsServerStarter;
import org.tio.websocket.server.handler.IWsMsgHandler;

import java.io.IOException;

@Slf4j
public class ImServerStarter {

    private WsServerStarter starter;

    public ImServerStarter(int port) throws IOException {

        IWsMsgHandler handler = new ImWsMsgHandler();

        starter = new WsServerStarter(port, handler);

        // 获取stater配置上下文
        ServerGroupContext serverGroupContext = starter.getServerGroupContext();
        serverGroupContext.setHeartbeatTimeout(5000);
    }

    public void start() throws IOException {
        starter.start();
        log.info("tio server 启动！！");
    }
}
