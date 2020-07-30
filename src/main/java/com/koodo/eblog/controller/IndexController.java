package com.koodo.eblog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController extends BaseController {

    @GetMapping(value = {"/index", "/", ""})
    public String toIndex() {

        // 1分页信息 2分类 3用户 4置顶  5精选 6排序
        IPage results = postService.paging(getPage(), null, null, null, null, "created");

        req.setAttribute("pageData", results);
        req.setAttribute("currentCategoryId", 0);
        return "index";
    }

    @GetMapping("/search")
    public String search(String q) {

        IPage pageData = searchService.search(getPage(), q);

        req.setAttribute("q", q);
        req.setAttribute("pageData", pageData);

        return "search";
    }


}
