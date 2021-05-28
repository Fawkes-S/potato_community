package com.potato.user.service;

import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import entity.Result;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import util.IdWorker;
import javax.servlet.http.HttpServletRequest;
import com.potato.user.dao.UserDao;
import com.potato.user.pojo.User;
import util.JwtUtil;

/**
 * 服务层
 *
 * @author Administrator
 *
 */
@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitMessagingTemplate rabbitMessagingTemplate;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private HttpServletRequest request;


    /**
     * 查询全部列表
     * @return
     */
    public List<User> findAll() {
        return userDao.findAll();
    }

    /**
     * 条件查询+分页
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<User> findSearch(Map whereMap, int page, int size) {
        Specification<User> specification = createSpecification(whereMap);
        PageRequest pageRequest =  PageRequest.of(page-1, size);
        return userDao.findAll(specification, pageRequest);
    }


    /**
     * 条件查询
     * @param whereMap
     * @return
     */
    public List<User> findSearch(Map whereMap) {
        Specification<User> specification = createSpecification(whereMap);
        return userDao.findAll(specification);
    }


    /**
     * 根据ID查询实体
     * @param id
     * @return
     */
    public User findById(String id) {
        System.out.println("serviceID:"+id);
        return userDao.findById(id).get();
    }

    /**
     * 报名活动
     * @param gatheringid
     * @param id
     * @return
     */
    public void addGathering(String gatheringid, String id) {
        User user  = userDao.findById(id).get();
        String gid = null;
        if(user.getGatheringid()==null||(user.getGatheringid()).equals("")){
            gid = gatheringid;
            user.setGatheringid(gid);
            userDao.save(user);
        }else {
            gid = ",".concat(gatheringid);
            userDao.addGathering(gid,id);
        }

    }

    /**
     * 增加（注册）
     * @param user
     */
    public void add(User user) {
        user.setId( idWorker.nextId()+"" );
        //密码加密
        user.setPassword(encoder.encode(user.getPassword()));

        user.setRegdate(new Date());
        user.setUpdatedate(new Date());
        user.setLastdate(new Date());

        userDao.save(user);
    }


    /**
     * 修改
     * @param user
     */
    public void update(User user) {
        userDao.save(user);
    }

    /**
     *
     * 删除
     * @param id
     */
    public void deleteById(String id) {
        userDao.deleteById(id);
    }


    /**
     * 用户登录
     * @param map
     * @return
     */
    public User login(Map<String, String> map) {

        //1.判断账户是否存在
        User user = userDao.findByMobile(map.get("mobile"));

        //2.判断密码是否正确
        if(user!=null && encoder.matches(map.get("password"),user.getPassword())){
            return user;
        }else {
            return null;
        }
    }

    /**
     * 动态条件构建
     * @param searchMap
     * @return
     */
    private Specification<User> createSpecification(Map searchMap) {

        return new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 手机号
                if (searchMap.get("mobile")!=null && !"".equals(searchMap.get("mobile"))) {
                    predicateList.add(cb.equal(root.get("mobile").as(String.class), (String)searchMap.get("mobile")));
                }
                // 手机号模糊查询
                if (searchMap.get("mob")!=null && !"".equals(searchMap.get("mob"))) {
                    predicateList.add(cb.like(root.get("mobile").as(String.class), "%"+(String)searchMap.get("mob")+"%"));
                }
                // 昵称
                if (searchMap.get("nickname")!=null && !"".equals(searchMap.get("nickname"))) {
                    predicateList.add(cb.like(root.get("nickname").as(String.class), "%"+(String)searchMap.get("nickname")+"%"));
                }
                // 性别
                if (searchMap.get("sex")!=null && !"".equals(searchMap.get("sex"))) {
                    predicateList.add(cb.like(root.get("sex").as(String.class), "%"+(String)searchMap.get("sex")+"%"));
                }
                // 出生日期之后
                if (searchMap.get("birthday")!=null && !"".equals(searchMap.get("birthday"))) {
                    predicateList.add(cb.greaterThanOrEqualTo(root.get("birthday").as(String.class), (String)searchMap.get("birthday")));
                }
                // 注册日期之后
                if (searchMap.get("regdate")!=null && !"".equals(searchMap.get("regdate"))) {
                    predicateList.add(cb.greaterThanOrEqualTo(root.get("regdate").as(String.class), (String)searchMap.get("regdate")));
                }
                // 修改日期之后
                if (searchMap.get("updatedate")!=null && !"".equals(searchMap.get("updatedate"))) {
                    predicateList.add(cb.greaterThanOrEqualTo(root.get("updatedate").as(String.class), (String)searchMap.get("updatedate")));
                }
                // 水费不为空
                if (searchMap.get("water")!=null && !"".equals(searchMap.get("water"))) {
                    predicateList.add(cb.notEqual(root.get("water").as(String.class),0));
                }
                // 电费不为空
                if (searchMap.get("electric")!=null && !"".equals(searchMap.get("electric"))) {
                    predicateList.add(cb.notEqual(root.get("electric").as(String.class),0));
                }
                // 网费不为空
                if (searchMap.get("network")!=null && !"".equals(searchMap.get("network"))) {
                    predicateList.add(cb.notEqual(root.get("network").as(String.class),0));
                }
                // 物业费不为空
                if (searchMap.get("property")!=null && !"".equals(searchMap.get("property"))) {
                    predicateList.add(cb.notEqual(root.get("property").as(String.class),0));
                }
                //isdispose已处理
                if (searchMap.get("isdispose")!=null && !"".equals(searchMap.get("isdispose"))) {
                    predicateList.add(cb.equal(root.get("isdispose").as(String.class), (String)searchMap.get("isdispose")));
                }
                // aid不为空
                if (searchMap.get("aid")!=null && !"".equals(searchMap.get("aid"))) {
                    predicateList.add(cb.notEqual(root.get("aid").as(String.class), ""));
                }


                return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }



}
