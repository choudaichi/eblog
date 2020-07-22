package com.koodo.eblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.koodo.eblog.entity.UserMessage;
import com.koodo.eblog.mapper.UserMessageMapper;
import com.koodo.eblog.service.UserMessageService;
import com.koodo.eblog.vo.UserMessageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author choudaichi
 * @since 2020-05-20
 */
@Service
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessage> implements UserMessageService {

    @Autowired
    UserMessageMapper userMessageMapper;

    @Override
    public IPage<UserMessageVo> paging(Page page, QueryWrapper<UserMessageVo> wrapper) {
        return userMessageMapper.selectMessages(page, wrapper);
    }
}
