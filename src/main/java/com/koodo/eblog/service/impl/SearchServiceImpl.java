package com.koodo.eblog.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.koodo.eblog.entity.Post;
import com.koodo.eblog.search.model.PostDocument;
import com.koodo.eblog.search.mq.PostMqIndexMsg;
import com.koodo.eblog.search.repository.PostRepository;
import com.koodo.eblog.service.PostService;
import com.koodo.eblog.service.SearchService;
import com.koodo.eblog.vo.PostVo;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostService postService;


    @Override
    public IPage search(Page page, String keyword) {

        Long current = page.getCurrent() - 1;
        Long size = page.getSize();
        Pageable pageable = PageRequest.of(current.intValue(), size.intValue());

        MultiMatchQueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(keyword, "title", "authorName", "categoryName");
        org.springframework.data.domain.Page<PostDocument> documents = postRepository.search(queryBuilder, pageable);

        Page pageData = new Page(page.getCurrent(), page.getSize(), documents.getTotalElements());
        pageData.setRecords(documents.getContent());

        return pageData;
    }

    @Override
    public int initEsData(List<PostVo> records) {

        if (CollUtil.isEmpty(records)) {
            return 0;
        }

        List<PostDocument> documents = new ArrayList<>();
        for (PostVo vo : records) {
            PostDocument document = new PostDocument();
            BeanUtils.copyProperties(vo, document);
            documents.add(document);
        }

        postRepository.saveAll(documents);

        return documents.size();

    }

    @Override
    public void createOrUpdateIndex(PostMqIndexMsg message) {

        PostVo postVo = postService.selectOnePost(new QueryWrapper<Post>().eq("p.id", message.getPostId()));
        PostDocument postDocument = new PostDocument();
        BeanUtils.copyProperties(postVo, postDocument);

        postRepository.save(postDocument);

        log.info("es索引更新成功！ -----> {}", postDocument.toString());
    }

    @Override
    public void removeIndex(PostMqIndexMsg message) {
        postRepository.deleteById(message.getPostId());
        log.info("es索引删除成功！ -----> {}", message.toString());
    }
}
