package com.koodo.eblog.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.koodo.eblog.common.lang.Result;
import com.koodo.eblog.entity.Post;
import com.koodo.eblog.entity.UserCollection;
import com.koodo.eblog.service.PostService;
import com.koodo.eblog.util.ValidationUtil;
import com.koodo.eblog.vo.CommentVo;
import com.koodo.eblog.vo.PostVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class PostController extends BaseController {

    @Autowired
    private PostService postService;

    @GetMapping("/category/{id:\\d*}")
    public String category(@PathVariable(name = "id") Long id) {

        int pn = ServletRequestUtils.getIntParameter(req, "pn", 1);

        req.setAttribute("currentCategoryId", id);
        req.setAttribute("pn", pn);
        return "post/category";
    }

    @GetMapping("/post/{id:\\d*}")
    public String detail(@PathVariable(name = "id") Long id) {

        PostVo vo = postService.selectOnePost(new QueryWrapper<Post>().eq("p.id", id));
        Assert.notNull(vo, "文章已被删除");

        postService.putViewCount(vo);

        //1.分页 2.文章id 3.用户id 4.排序
        IPage<CommentVo> results = commentService.paging(getPage(), vo.getId(), null, "created");

        req.setAttribute("currentCategoryId", vo.getCategoryId());
        req.setAttribute("post", vo);
        req.setAttribute("pageData", results);

        return "post/detail";
    }

    @GetMapping("/post/edit")
    public String edit() {

        String id = req.getParameter("id");
        if (!StringUtils.isEmpty(id)) {
            Post post = postService.getById(id);
            Assert.isTrue(post != null, "文章已被删除");
            Assert.isTrue(getProfileId().equals(post.getUserId()), "没有操作权限");
            req.setAttribute("post", post);
        }

        return "post/edit";
    }

    @ResponseBody
    @PostMapping("/post/submit")
    public Result submit(Post post) {

        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(post);
        if (validResult.hasErrors()) {
            return Result.fail(validResult.getErrors());
        }

        if (post.getId() == null) {
            post.setUserId(getProfileId());

            post.setModified(new Date());
            post.setCreated(new Date());
            post.setCommentCount(0);
            post.setEditMode(null);
            post.setLevel(0);
            post.setRecommend(false);
            post.setViewCount(0);
            post.setVoteDown(0);
            post.setVoteUp(0);
            postService.save(post);

        } else {
            Post tempPost = postService.getById(post.getId());
            Assert.isTrue(tempPost.getUserId().longValue() == getProfileId().longValue(), "无权限编辑此文章！");

            tempPost.setTitle(post.getTitle());
            tempPost.setContent(post.getContent());
            tempPost.setCategoryId(post.getCategoryId());
            postService.updateById(tempPost);
        }

        return Result.success().action("/post/" + post.getId());
    }

    /**
     * 判断用户是否收藏文章
     *
     * @param pid
     * @return
     */
    @ResponseBody
    @PostMapping("/collection/find")
    public Result collectionFind(Long pid) {

        int count = collectionService.count(new QueryWrapper<UserCollection>()
                .eq("user_id", getProfileId())
                .eq("post_id", pid)
        );

        return Result.success(MapUtil.of("collection", count > 0));
    }

    @ResponseBody
    @PostMapping("/collection/add")
    public Result collectionAdd(Long pid) {

        Post post = postService.getById(pid);
        Assert.isTrue(post != null, "该文章不存在");

        int count = collectionService.count(new QueryWrapper<UserCollection>()
                .eq("user_id", getProfileId())
                .eq("post_id", pid)
        );

        if (count > 0) {
            return Result.fail("你已经收藏");
        }

        UserCollection collection = new UserCollection();
        collection.setPostId(pid);
        collection.setPostUserId(post.getUserId());
        collection.setUserId(getProfileId());
        collection.setCreated(new Date());
        collection.setModified(new Date());
        collectionService.save(collection);

        return Result.success();
    }

    @ResponseBody
    @PostMapping("/collection/remove")
    public Result collectionRemove(Long pid) {

        Post post = postService.getById(pid);
        Assert.isTrue(post != null, "该文章不存在");

        collectionService.remove(new QueryWrapper<UserCollection>()
                .eq("user_id", getProfileId())
                .eq("post_id", pid)
        );

        return Result.success();
    }


}
