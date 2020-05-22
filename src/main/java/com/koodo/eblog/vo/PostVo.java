package com.koodo.eblog.vo;

import com.koodo.eblog.entity.Post;
import lombok.Data;

@Data
public class PostVo extends Post {

    private Long authorId;
    private String authorName;
    private String authorAvatar;

    private String categoryName;

}
