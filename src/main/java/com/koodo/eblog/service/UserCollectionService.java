package com.koodo.eblog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.koodo.eblog.entity.UserCollection;
import com.koodo.eblog.vo.PostVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author choudaichi
 * @since 2020-05-20
 */
public interface UserCollectionService extends IService<UserCollection> {

    IPage<PostVo> collectionPage(Page page, Long userId);
}
