package com.potato.message.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.potato.message.pojo.Message;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface MessageDao extends JpaRepository<Message,String>,JpaSpecificationExecutor<Message>{

    /**
     * 文章审核
     * @param id
     */
    @Modifying //注意:如果@Query注解来进行更新,必须带上此注解
    @Query("update Message m set m.state ='1' where m.id=?1")
    public void examine(String id);


    /**
     * 资讯的查询
     */
    public Page<Message> findByStateAndTypeOrderByCreatetime(String state ,String type, Pageable pageable);
}