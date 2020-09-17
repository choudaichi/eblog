package com.koodo.eblog.service;

import com.koodo.eblog.im.vo.ImMess;
import com.koodo.eblog.im.vo.ImUser;

import java.util.List;

public interface ChatService {
    ImUser getCurrentUser();

    void setGroupHistoryMsg(ImMess responseMess);

    List<Object> getGroupHistoryMsg(int count);

}
