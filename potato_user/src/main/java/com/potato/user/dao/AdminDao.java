package com.potato.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.potato.user.pojo.Admin;
/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface AdminDao extends JpaRepository<Admin,String>,JpaSpecificationExecutor<Admin>{

    /**
     * 根据loginname查询管理员
     * @param username
     * @return
     */
    public Admin findByUsername(String username);
}