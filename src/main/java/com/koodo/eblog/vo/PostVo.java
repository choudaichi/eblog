package com.koodo.eblog.vo;

import com.koodo.eblog.entity.Post;
import lombok.Data;

import java.util.Date;

@Data
public class PostVo extends Post {

    private Long authorId;
    private String authorName;
    private String authorAvatar;

    private String categoryName;

    private Date collectionCreated;

}
