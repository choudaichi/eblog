package com.koodo.eblog.vo;

import com.koodo.eblog.entity.UserMessage;
import lombok.Data;

@Data
public class UserMessageVo extends UserMessage {

    private String toUserName;

    private String fromUserName;

    private String postTitle;

    private String commentContent;

}
