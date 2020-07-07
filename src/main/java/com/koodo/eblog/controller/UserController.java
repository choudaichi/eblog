package com.koodo.eblog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.koodo.eblog.entity.Post;
import com.koodo.eblog.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @GetMapping("home")
    public String home() {

        User user = userService.getById(getProfileId());

        List<Post> posts = postService.list(new QueryWrapper<Post>()
                .eq("user_id", getProfileId())
                //30天内
                //.gt("created", DateUtil.offsetDay(new Date(),-30))
                .orderByDesc("created")
        );

        req.setAttribute("user", user);
        req.setAttribute("posts", posts);

        return "/user/home";
    }
}
