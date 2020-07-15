package com.koodo.eblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.koodo.eblog.entity.UserCollection;
import com.koodo.eblog.vo.PostVo;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author choudaichi
 * @since 2020-05-20
 */
public interface UserCollectionMapper extends BaseMapper<UserCollection> {

    IPage<PostVo> selectCollections(Page page, Long userId);
}
