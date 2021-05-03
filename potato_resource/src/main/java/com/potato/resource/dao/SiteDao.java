package com.potato.resource.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.potato.resource.pojo.Site;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface SiteDao extends JpaRepository<Site,String>,JpaSpecificationExecutor<Site>{

    /**
     * 根据state按照createTime倒序,取前面4条
     * @param state
     * @return
     */
    List<Site> findTop4ByStateOrderByCreatetimeDesc(String state);

    /**
     * 查询state不为0,按照createTime倒序,取前面10条
     * @param s
     * @return
     */
    List<Site> findTop10ByStateNotOrderByCreatetimeDesc(String s);
}
