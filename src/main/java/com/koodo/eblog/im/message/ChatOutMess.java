package com.koodo.eblog.im.message;

import com.koodo.eblog.im.vo.ImMess;
import lombok.Data;

@Data
public class ChatOutMess {

    private String emit;
    private ImMess data;

}