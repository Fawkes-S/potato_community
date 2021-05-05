package com.potato.message.service;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import com.potato.message.dao.MessageDao;
import com.potato.message.pojo.Message;

/**
 * 服务层
 *
 * @author Administrator
 *
 */
@Service
public class MessageService {

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private IdWorker idWorker;


    /**
     * 查询全部列表
     * @return
     */
    public List<Message> findAll() {
        return messageDao.findAll();
    }


    /**
     * 条件查询+分页
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<Message> findSearch(Map whereMap, int page, int size) {
        Specification<Message> specification = createSpecification(whereMap);
        PageRequest pageRequest =  PageRequest.of(page-1, size);
        return messageDao.findAll(specification, pageRequest);
    }


    /**
     * 条件查询
     * @param whereMap
     * @return
     */
    public List<Message> findSearch(Map whereMap) {
        Specification<Message> specification = createSpecification(whereMap);
        return messageDao.findAll(specification);
    }

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 根据ID查询实体
     * @param id
     * @return
     */
    public Message findById(String id) {

        //从redis查询该资讯是否存在
        Message message = (Message) redisTemplate.opsForValue().get("message_"+id);

        //不存在,从数据库查询出来,放入redis
        if(message == null){
            message = messageDao.findById(id).get();
            //存入redis
            redisTemplate.opsForValue().set("message_"+id, message,120, TimeUnit.SECONDS);
        }
        return message;
    }

    /**
     * 增加
     * @param message
     */
    public void add(Message message) {
        message.setId( idWorker.nextId()+"" );
        messageDao.save(message);
    }

    /**
     * 修改
     * @param message
     */
    public void update(Message message) {
        redisTemplate.delete("message_"+message.getId());
        messageDao.save(message);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteById(String id) {
        redisTemplate.delete("message_"+id);
        messageDao.deleteById(id);
    }

    /**
     * 动态条件构建
     * @param searchMap
     * @return
     */
    private Specification<Message> createSpecification(Map searchMap) {

        return new Specification<Message>() {

            @Override
            public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 用户ID
                if (searchMap.get("userid")!=null && !"".equals(searchMap.get("userid"))) {
                    predicateList.add(cb.like(root.get("userid").as(String.class), "%"+(String)searchMap.get("userid")+"%"));
                }
                // 资讯标题
                if (searchMap.get("title")!=null && !"".equals(searchMap.get("title"))) {
                    predicateList.add(cb.like(root.get("title").as(String.class), "%"+(String)searchMap.get("title")+"%"));
                }
                // 资讯正文
                if (searchMap.get("content")!=null && !"".equals(searchMap.get("content"))) {
                    predicateList.add(cb.like(root.get("content").as(String.class), "%"+(String)searchMap.get("content")+"%"));
                }
                // 资讯封面
                if (searchMap.get("image")!=null && !"".equals(searchMap.get("image"))) {
                    predicateList.add(cb.like(root.get("image").as(String.class), "%"+(String)searchMap.get("image")+"%"));
                }
                // 是否公开
                if (searchMap.get("ispublic")!=null && !"".equals(searchMap.get("ispublic"))) {
                    predicateList.add(cb.like(root.get("ispublic").as(String.class), "%"+(String)searchMap.get("ispublic")+"%"));
                }
                // 是否置顶
                if (searchMap.get("istop")!=null && !"".equals(searchMap.get("istop"))) {
                    predicateList.add(cb.like(root.get("istop").as(String.class), "%"+(String)searchMap.get("istop")+"%"));
                }
                // 审核状态
                if (searchMap.get("state")!=null && !"".equals(searchMap.get("state"))) {
                    predicateList.add(cb.like(root.get("state").as(String.class), "%"+(String)searchMap.get("state")+"%"));
                }
                // URL
                if (searchMap.get("url")!=null && !"".equals(searchMap.get("url"))) {
                    predicateList.add(cb.like(root.get("url").as(String.class), "%"+(String)searchMap.get("url")+"%"));
                }
                // 类型
                if (searchMap.get("type")!=null && !"".equals(searchMap.get("type"))) {
                    predicateList.add(cb.like(root.get("type").as(String.class), "%"+(String)searchMap.get("type")+"%"));
                }

                return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }

    /**
     * 审核
     * @param id
     */
    @Transactional
    public void examine(String id) {
        messageDao.examine(id);
    }

    /**
     * 点赞
     * @param id
     */
    @Transactional
    public void thumbUp(String id) {
        messageDao.thumbUp(id);
    }
}

