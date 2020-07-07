package com.koodo.eblog.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.koodo.eblog.common.lang.Result;
import com.koodo.eblog.entity.User;
import com.koodo.eblog.mapper.UserMapper;
import com.koodo.eblog.service.UserService;
import com.koodo.eblog.shiro.AccountProfile;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author choudaichi
 * @since 2020-05-20
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public Result register(User user) {
        int count = this.count(new QueryWrapper<User>()
                .eq("email", user.getEmail())
                .or()
                .eq("username", user.getUsername()));
        if (count > 0) {
            return Result.fail("用户名或邮箱已被注册");
        }

        //防止传入的user中带有积分等信息，保护数据安全，重新new一个新的User
        User temp = new User();
        temp.setEmail(user.getEmail());
        temp.setUsername(user.getUsername());
        temp.setPassword(SecureUtil.md5(user.getPassword()));
        temp.setAvatar("/res/images/avatar/default.png");
        temp.setCreated(new Date());
        temp.setPoint(0);
        temp.setVipLevel(0);
        temp.setCommentCount(0);
        temp.setPostCount(0);
        temp.setGender("0");
        this.save(temp);

        return Result.success();
    }

    @Override
    public AccountProfile login(String email, String password) {

        User user = this.getOne(new QueryWrapper<User>().eq("email", email));
        if (user == null) {
            throw new UnknownAccountException();
        }

        if (!password.equals(user.getPassword())) {
            throw new IncorrectCredentialsException();
        }

        user.setLasted(new Date());
        this.updateById(user);

        AccountProfile profile = new AccountProfile();
        BeanUtils.copyProperties(user, profile);

        return profile;
    }
}
