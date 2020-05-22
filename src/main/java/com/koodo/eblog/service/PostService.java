package com.koodo.eblog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.koodo.eblog.entity.Post;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author choudaichi
 * @since 2020-05-20
 */
public interface PostService extends IService<Post> {

    IPage paging(Page page, Long categoryId, Long userId, Integer level, Boolean recommend, String order);
}
