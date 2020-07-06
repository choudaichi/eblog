package com.koodo.eblog.controller;

import com.google.code.kaptcha.Producer;
import com.koodo.eblog.common.lang.Result;
import com.koodo.eblog.entity.User;
import com.koodo.eblog.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
public class AuthController extends BaseController {

    private static final String KAPTCHA_SESSION_KEY = "KAPTCHA_SESSION_KEY";

    @Autowired
    Producer producer;

    @GetMapping("/capthca.jpg")
    public void kaptcha(HttpServletResponse resp) throws IOException {

        //验证码
        String text = producer.createText();
        BufferedImage image = producer.createImage(text);

        resp.setHeader("Cache-Control", "no-store, no-cache");
        resp.setContentType("image/jpeg");
        req.getSession().setAttribute(KAPTCHA_SESSION_KEY, text);
        try (ServletOutputStream outputStream = resp.getOutputStream()) {
            ImageIO.write(image, "jpg", outputStream);
        }

    }


    @GetMapping("/login")
    public String login() {
        return "/auth/login";
    }

    @ResponseBody
    @PostMapping("/login")
    public Result doLogin() {
        return Result.success().action("/");
    }

    @GetMapping("/register")
    public String register() {
        return "/auth/reg";
    }


    @ResponseBody
    @PostMapping("/register")
    public Result doRegister(User user, String repass, String vercode) {
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(user);
        if (validResult.hasErrors()) {
            return Result.fail(validResult.getErrors());
        }

        if (!user.getPassword().equals(repass)) {
            return Result.fail("两次输入密码不相同");
        }

        String capthca = (String) req.getSession().getAttribute(KAPTCHA_SESSION_KEY);
        if (!vercode.equals(capthca)) {
            return Result.fail("验证码输入不正确");
        }

        Result result = userService.register(user);
        return result.action("/login");
    }
}
