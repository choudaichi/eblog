package com.koodo.eblog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.koodo.eblog.search.mq.PostMqIndexMsg;
import com.koodo.eblog.vo.PostVo;

import java.util.List;

public interface SearchService {
    IPage search(Page page, String keyword);

    int initEsData(List<PostVo> records);

    void createOrUpdateIndex(PostMqIndexMsg message);

    void removeIndex(PostMqIndexMsg message);
}
