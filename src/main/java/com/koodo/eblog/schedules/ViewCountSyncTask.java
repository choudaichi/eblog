package com.koodo.eblog.schedules;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.koodo.eblog.entity.Post;
import com.koodo.eblog.service.PostService;
import com.koodo.eblog.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class ViewCountSyncTask {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    PostService postService;

    //每分钟同步一次
    @Scheduled(cron = "0 0/1 * * * *")
    public void task() {
        Set<String> keys = redisTemplate.keys("rank:post:*");

        List<String> ids = new ArrayList<>();
        assert keys != null;
        for (String key : keys) {
            if (redisUtil.hHasKey(key, "post:viewCount")) {
                ids.add(key.substring("rank:post:".length()));
            }
        }

        if (ids.isEmpty()) {
            return;
        }

        //需要更新阅读量
        List<Post> posts = postService.list(new QueryWrapper<Post>().in("id", ids));
        for (Post post : posts) {
            Integer viewCount = (Integer) redisUtil.hget("rank:post:" + post.getId(), "post:viewCount");
            post.setViewCount(viewCount);
        }

        if (posts.isEmpty()) {
            return;
        }

        boolean isSucc = postService.updateBatchById(posts);

        if (isSucc) {
            for (String id : ids) {
                redisUtil.hdel("rank:post:" + id, "post:viewCount");
                System.out.println(id + "-------------同步成功");
            }
        }
    }
}
