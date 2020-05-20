package com.koodo.eblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController extends BaseController{

    @GetMapping(value = {"/index","/",""})
    public String toIndex() {
        req.setAttribute("currentCategoryId",0);
        return "index";
    }
}
