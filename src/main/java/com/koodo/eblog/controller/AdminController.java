package com.koodo.eblog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.koodo.eblog.common.lang.Result;
import com.koodo.eblog.entity.Post;
import com.koodo.eblog.vo.PostVo;
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

    @ResponseBody
    @PostMapping("initEsData")
    public Result initEsData() {

        int size = 10000;
        Page page = new Page();
        page.setSize(size);

        long total = 0;

        for (int i = 1; i < 1000; i++) {
            page.setCurrent(i);

            IPage<PostVo> paging = postService.paging(page, null, null, null, false, null);

            int num = searchService.initEsData(paging.getRecords());

            total += num;

            if (paging.getRecords().size() < size) {
                break;
            }

        }

        return Result.success("ES索引初始化成功，共 " + total + " 条记录！", null);
    }

}
