package com.potato.user.dao;

import entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.potato.user.pojo.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据访问接口
 *
 * @author Administrator
 */
@Transactional
public interface UserDao extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    /**
     * 根据手机查询用户
     *
     * @param mobile
     * @return
     */
    public User findByMobile(String mobile);


    /**
     * 报名
     * @param gatheringid
     * @param id
     */
    @Modifying
    @Query("update User u set u.gatheringid = concat(u.gatheringid, ?1) where u.id=?2")
    public void addGathering(String gatheringid,String id);
}
