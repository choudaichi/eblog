package com.koodo.eblog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController extends BaseController{

    @GetMapping(value = {"/index","/",""})
    public String toIndex() {

        int pn = ServletRequestUtils.getIntParameter(req, "pn", 1);
        int size = ServletRequestUtils.getIntParameter(req, "size", 2);
        Page page = new Page(pn, size);


        // 1分页信息 2分类 3用户 4置顶  5精选 6排序
        IPage results = postService.paging(page, null, null, null, null, "created");

        req.setAttribute("pageData", results);
        req.setAttribute("currentCategoryId", 0);
        return "index";
    }
}
