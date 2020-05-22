package com.koodo.eblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.koodo.eblog.entity.Post;
import com.koodo.eblog.mapper.PostMapper;
import com.koodo.eblog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author choudaichi
 * @since 2020-05-20
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Override
    public IPage paging(Page page, Long categoryId, Long userId, Integer level, Boolean recommend, String order) {

        if (level == null) {
            level = -1;
        }

        QueryWrapper<Post> wrapper = new QueryWrapper<Post>()
                .eq(categoryId != null, "category_id", categoryId)
                .eq(userId != null, "user_id", userId)
                .eq(level == 0, "level", 0)
                .gt(level > 0, "level", 0)
                .orderByDesc(order != null, order);
        return postMapper.selectPosts(page, wrapper);
    }
}
