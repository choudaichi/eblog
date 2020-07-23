package com.koodo.eblog.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.koodo.eblog.common.lang.Result;
import com.koodo.eblog.entity.Post;
import com.koodo.eblog.entity.User;
import com.koodo.eblog.entity.UserCollection;
import com.koodo.eblog.entity.UserMessage;
import com.koodo.eblog.service.UserCollectionService;
import com.koodo.eblog.shiro.AccountProfile;
import com.koodo.eblog.util.UploadUtil;
import com.koodo.eblog.vo.PostVo;
import com.koodo.eblog.vo.UserMessageVo;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    UploadUtil uploadUtil;

    @Autowired
    UserCollectionService userCollectionService;

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

    @GetMapping("home/{id:\\d*}")
    public String visit(@PathVariable(name = "id") Long id) {

        User user = userService.getById(id);

        List<Post> posts = postService.list(new QueryWrapper<Post>()
                .eq("user_id", id)
                //30天内
                //.gt("created", DateUtil.offsetDay(new Date(),-30))
                .orderByDesc("created")
        );

        req.setAttribute("user", user);
        req.setAttribute("posts", posts);

        return "/user/home";
    }

    @GetMapping("set")
    public String set() {

        User user = userService.getById(getProfileId());
        req.setAttribute("user", user);

        return "/user/set";
    }


    @GetMapping("index")
    public String index() {

        int postCount = postService.count(new QueryWrapper<Post>()
                .eq("user_id", getProfileId()));
        req.setAttribute("user_postCount", postCount);

        int collectionCount = userCollectionService.count(new QueryWrapper<UserCollection>()
                .eq("user_id", getProfileId()));
        req.setAttribute("user_collectionCount", collectionCount);

        return "/user/index";
    }

    @GetMapping("mess")
    public String mess() {

        IPage<UserMessageVo> page = messageService.paging(getPage(), new QueryWrapper<UserMessageVo>()
                .eq("to_user_id", getProfileId())
                .orderByAsc("created"));

        req.setAttribute("pageData", page);

        return "/user/mess";
    }


    @ResponseBody
    @PostMapping("set")
    public Result doSet(User user) {

        // 更改头像也走这个接口，先上传头像
        // 再发个请求更新数据库，如果上传了头像就直接改头像，否则更改其他信息
        if (StrUtil.isNotBlank(user.getAvatar())) {
            User temp = userService.getById(getProfileId());
            temp.setAvatar(user.getAvatar());
            userService.updateById(temp);

            AccountProfile profile = getProfile();
            profile.setAvatar(temp.getAvatar());
            SecurityUtils.getSubject().getSession().setAttribute("profile", profile);

            return Result.success().action("/user/set#avatar");
        }

        if (StrUtil.isBlank(user.getUsername())) {
            return Result.fail("昵称不能为空");
        }

        int count = userService.count(new QueryWrapper<User>()
                .eq("username", user.getUsername())
                .ne("id", getProfileId()));
        if (count > 0) {
            return Result.fail("昵称已经存在");
        }

        User temp = userService.getById(getProfileId());
        temp.setUsername(user.getUsername());
        temp.setSign(user.getSign());
        temp.setGender(user.getGender());
        userService.updateById(temp);

        AccountProfile profile = getProfile();
        profile.setUsername(temp.getUsername());
        profile.setSign(temp.getSign());
        SecurityUtils.getSubject().getSession().setAttribute("profile", profile);

        return Result.success().action("/user/set#info");
    }


    @ResponseBody
    @PostMapping("upload")
    public Result uploadAvatar(@RequestParam("file") MultipartFile file) throws IOException {
        return uploadUtil.upload(UploadUtil.type_avatar, file);
    }

    @ResponseBody
    @PostMapping("repass")
    public Result repass(String nowpass, String pass, String repass) {
        if (!pass.equals(repass)) {
            return Result.fail("两次密码不相同");
        }

        User user = userService.getById(getProfileId());

        String nowPassMd5 = SecureUtil.md5(nowpass);
        if (!nowPassMd5.equals(user.getPassword())) {
            return Result.fail("密码不正确");
        }

        user.setPassword(SecureUtil.md5(pass));
        userService.updateById(user);

        return Result.success().action("/user/set#pass");

    }

    @ResponseBody
    @GetMapping("public")
    public Result userP() {
        IPage page = postService.page(getPage(), new QueryWrapper<Post>()
                .eq("user_id", getProfileId())
                .orderByDesc("created"));

        return Result.success(page);
    }

    @ResponseBody
    @GetMapping("collection")
    public Result collection() {

        IPage<PostVo> page = userCollectionService.collectionPage(getPage(), getProfileId());

        return Result.success(page);
    }

    @ResponseBody
    @PostMapping("msg/remove")
    public Result msgRemove(Long id, @RequestParam(defaultValue = "false") Boolean all) {

        boolean remove = messageService.remove(new QueryWrapper<UserMessage>()
                .eq("to_user_id", getProfileId())
                .eq(!all, "id", id));

        if (remove) {
            return Result.success();
        } else {
            return Result.fail("删除失败");
        }
    }

    @ResponseBody
    @PostMapping("msg/nums")
    public Map msgNums() {

        int count = messageService.count(new QueryWrapper<UserMessage>()
                .eq("to_user_id", getProfileId())
                .eq("status", "0"));
        return MapUtil.builder("status", 0).put("count", count).build();
    }


}
