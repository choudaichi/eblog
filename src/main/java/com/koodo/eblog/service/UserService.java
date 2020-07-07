package com.koodo.eblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.koodo.eblog.common.lang.Result;
import com.koodo.eblog.entity.User;
import com.koodo.eblog.shiro.AccountProfile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author choudaichi
 * @since 2020-05-20
 */
public interface UserService extends IService<User> {

    Result register(User user);

    AccountProfile login(String email, String password);
}
