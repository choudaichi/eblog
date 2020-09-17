package com.koodo.eblog.im.message;

import com.koodo.eblog.im.vo.ImTo;
import com.koodo.eblog.im.vo.ImUser;
import lombok.Data;

@Data
public class ChatImMess {

    private ImUser mine;
    private ImTo to;

}
