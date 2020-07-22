package com.koodo.eblog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.koodo.eblog.entity.UserMessage;
import com.koodo.eblog.vo.UserMessageVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author choudaichi
 * @since 2020-05-20
 */
public interface UserMessageService extends IService<UserMessage> {

    IPage<UserMessageVo> paging(Page page, QueryWrapper<UserMessageVo> wrapper);
}
