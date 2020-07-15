package com.koodo.eblog.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.koodo.eblog.entity.UserCollection;
import com.koodo.eblog.mapper.UserCollectionMapper;
import com.koodo.eblog.service.UserCollectionService;
import com.koodo.eblog.vo.PostVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author choudaichi
 * @since 2020-05-20
 */
@Service
public class UserCollectionServiceImpl extends ServiceImpl<UserCollectionMapper, UserCollection> implements UserCollectionService {

    @Autowired
    UserCollectionMapper userCollectionMapper;

    @Override
    public IPage<PostVo> collectionPage(Page page, Long userId) {
        return userCollectionMapper.selectCollections(page, userId);
    }

}
