package com.potato.search.dao;

import com.potato.search.pojo.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MessageDao extends ElasticsearchRepository<Message,String>{

    /**
     * 检索
     * @param title
     * @param content
     * @param pageable
     * @return
     */
    public Page<Message> findByTitleOrContentLike(String title, String content, Pageable pageable);
}