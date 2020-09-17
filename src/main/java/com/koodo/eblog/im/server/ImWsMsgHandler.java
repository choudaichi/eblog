package com.koodo.eblog.im.server;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.koodo.eblog.common.lang.Consts;
import com.koodo.eblog.im.handler.MsgHandler;
import com.koodo.eblog.im.handler.MsgHandlerFactory;
import lombok.extern.slf4j.Slf4j;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.server.handler.IWsMsgHandler;

import java.util.Map;


@Slf4j
public class ImWsMsgHandler implements IWsMsgHandler {

    /**
     * 握手时
     *
     * @param httpRequest
     * @param httpResponse
     * @param channelContext
     * @return
     * @throws Exception
     */
    @Override
    public HttpResponse handshake(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        //绑定个人通道
        String userId = httpRequest.getParam("userId");
        log.info("{}---------------->正在握手", userId);
        Tio.bindUser(channelContext, userId);

        return httpResponse;
    }

    /**
     * 握手之后方法
     *
     * @param httpRequest
     * @param httpResponse
     * @param channelContext
     * @throws Exception
     */
    @Override
    public void onAfterHandshaked(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        //绑定群聊通道
        Tio.bindGroup(channelContext, String.valueOf(Consts.IM_GROUP_ID));
        log.info("{}----------------->已绑定群", channelContext.getId());
    }

    /**
     * 接收字节类型
     *
     * @param wsRequest
     * @param bytes
     * @param channelContext
     * @return
     * @throws Exception
     */
    @Override
    public Object onBytes(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        return null;
    }

    /**
     * 断开连接时
     *
     * @param wsRequest
     * @param bytes
     * @param channelContext
     * @return
     * @throws Exception
     */
    @Override
    public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        return null;
    }

    /**
     * 接收字符类型
     *
     * @param wsRequest
     * @param text
     * @param channelContext
     * @return
     * @throws Exception
     */
    @Override
    public Object onText(WsRequest wsRequest, String text, ChannelContext channelContext) throws Exception {

        log.info("接收到消息-------------------> {}", text);

        Map map = JSONUtil.toBean(text, Map.class);

        String type = MapUtil.getStr(map, "type");
        String data = MapUtil.getStr(map, "data");

        MsgHandler handler = MsgHandlerFactory.getMsgHandler(type);

        handler.handler(data, wsRequest, channelContext);

        return null;
    }

}
