package com.koodo.eblog.search.mq;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class PostMqIndexMsg implements Serializable {

    public static final String CREATE_OR_UPDATE = "create_update";
    public static final String REMOVE = "remove";

    private Long postId;
    private String type;

}
