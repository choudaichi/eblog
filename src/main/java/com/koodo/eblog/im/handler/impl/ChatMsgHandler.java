package com.koodo.eblog.im.handler.impl;

import cn.hutool.json.JSONUtil;
import com.koodo.eblog.common.lang.Consts;
import com.koodo.eblog.im.handler.MsgHandler;
import com.koodo.eblog.im.handler.filter.ExcludeMineChannelContextFilter;
import com.koodo.eblog.im.message.ChatImMess;
import com.koodo.eblog.im.message.ChatOutMess;
import com.koodo.eblog.im.vo.ImMess;
import com.koodo.eblog.im.vo.ImTo;
import com.koodo.eblog.im.vo.ImUser;
import com.koodo.eblog.service.ChatService;
import com.koodo.eblog.util.SpringUtil;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.util.Date;

public class ChatMsgHandler implements MsgHandler {
    @Override
    public void handler(String data, WsRequest wsRequest, ChannelContext channelContext) {

        ChatImMess chatImMess = JSONUtil.toBean(data, ChatImMess.class);
        ImUser mine = chatImMess.getMine();
        ImTo to = chatImMess.getTo();

        ImMess imMess = new ImMess();
        imMess.setUsername(mine.getUsername());
        imMess.setContent(mine.getContent());
        imMess.setAvatar(mine.getAvatar());
        imMess.setFromid(mine.getId());
        imMess.setId(Consts.IM_GROUP_ID);
        imMess.setTimestamp(new Date());
        imMess.setType(to.getType());
        imMess.setMine(false);

        ChatOutMess chatOutMess = new ChatOutMess();
        chatOutMess.setEmit("chatMessage");
        chatOutMess.setData(imMess);

        ExcludeMineChannelContextFilter filter = new ExcludeMineChannelContextFilter(channelContext);

        WsResponse wsResponse = WsResponse.fromText(JSONUtil.toJsonStr(chatOutMess), "utf-8");

        Tio.sendToGroup(channelContext.getGroupContext(), String.valueOf(Consts.IM_GROUP_ID), wsResponse, filter);

        //保存群聊信息
        ChatService chatService = (ChatService) SpringUtil.getBean("chatService");
        chatService.setGroupHistoryMsg(imMess);

    }
}
