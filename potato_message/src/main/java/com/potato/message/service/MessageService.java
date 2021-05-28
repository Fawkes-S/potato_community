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

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 查询全部列表
     * @return
     */
    public List<Message> findAll() {
        return messageDao.findAll();
    }


    /**
     * 查询分页(state,type)
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<Message> findSearch(Map whereMap, int page, int size) {
        PageRequest pageRequest =  PageRequest.of(page-1, size);
        System.out.println(whereMap);
        String state = (String) whereMap.get("state");
        String type = (String) whereMap.get("type");
        if(state.equals("")&&type.equals("")){
            throw new RuntimeException("state and type are null");
        }

        return messageDao.findByStateAndTypeOrderByCreatetime(state,type,pageRequest);


    }


    /**
     * 动态条件分页查询
     * @param whereMap
     * @return
     */
    public Page<Message> backSearch(Map whereMap, int page, int size) {
        Specification<Message> specification = createSpecification(whereMap);
        PageRequest pageRequest =  PageRequest.of(page-1, size);
        return messageDao.findAll(specification, pageRequest);
    }

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
                // 状态
                if (searchMap.get("state")!=null && !"".equals(searchMap.get("state"))) {
                    predicateList.add(cb.like(root.get("state").as(String.class), "%"+(String)searchMap.get("state")+"%"));
                }
                // 类型
                if (searchMap.get("type")!=null && !"".equals(searchMap.get("type"))) {
                    predicateList.add(cb.like(root.get("type").as(String.class), "%"+(String)searchMap.get("type")+"%"));
                }
                // 发表日期之后
                if (searchMap.get("createtime")!=null && !"".equals(searchMap.get("createtime"))) {
                    predicateList.add(cb.greaterThanOrEqualTo(root.get("createtime").as(String.class), (String)searchMap.get("createtime")));
                }
                // 修改时间之后
                if (searchMap.get("updatetime")!=null && !"".equals(searchMap.get("updatetime"))) {
                    predicateList.add(cb.greaterThanOrEqualTo(root.get("updatetime").as(String.class), (String)searchMap.get("updatetime")));
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

//    /**
//     * 点赞
//     * @param id
//     */
//    @Transactional
//    public void thumbUp(String id) {
//        messageDao.thumbUp(id);
//    }
}

