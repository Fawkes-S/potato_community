package com.potato.search.dao;

import com.potato.search.pojo.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MessageDao extends ElasticsearchRepository<Message,String>{

    Page<Message> findByTitleOrContentLike(String title, String content, Pageable pageable);
}