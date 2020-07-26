package com.koodo.eblog.controller;

import com.koodo.eblog.common.lang.Result;
import com.koodo.eblog.entity.Post;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("admin")
public class AdminController extends BaseController {

    /**
     * 管理员对文章操作
     *
     * @param id
     * @param rank  0表示取消 1表示执行
     * @param field delete删除 status加精 stick置顶
     * @return
     */
    @ResponseBody
    @PostMapping("jie-set")
    public Result jieSet(Long id, Integer rank, String field) {

        Post post = postService.getById(id);
        Assert.isTrue(post != null, "文章已被删除");

        if ("delete".equals(field)) {
            postService.removeById(id);
            return Result.success();
        } else if ("status".equals(field)) {
            post.setRecommend(rank == 1);
        } else if ("stick".equals(field)) {
            post.setLevel(rank);
        }

        postService.updateById(post);

        return Result.success();
    }

}
