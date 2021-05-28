package com.potato.advice.dao;

import com.potato.advice.pojo.Advice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdviceDao extends MongoRepository<Advice,String>{

    /**
     * 根据上级ID查询建议评论
     */
    public Page<Advice> findByParentidOrderByPublishtime(String parentid, Pageable pageable); //Pageable分页
    /**
     * 建议首页的查询
     */
    public Page<Advice> findByStateAndParentidOrderByPublishtime(String state ,String parentid, Pageable pageable); //Pageable分页
}