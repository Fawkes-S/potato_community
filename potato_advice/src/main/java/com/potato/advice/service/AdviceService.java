package com.potato.advice.service;

import com.potato.advice.dao.AdviceDao;
import com.potato.advice.pojo.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 服务层
 *
 * @author Administrator
 *
 */
@Service
@Transactional
public class AdviceService {

    @Autowired
    private AdviceDao adviceDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 查询所有
     */
    public List<Advice> findAll(){
        return adviceDao.findAll();
    }

    /**
     * 查询一个
     */
    public Advice findById(String id){
        return adviceDao.findById(id).get();
    }

    /**
     * 条件查询+分页
     * @param page
     * @param size
     * @return
     */
    public Page<Advice> findSearch(int page, int size) {
        PageRequest pageRequest =  PageRequest.of(page-1, size);
        return adviceDao.findAll(pageRequest);
    }

    /**
     * 添加
     */
    public void add(Advice advice){

        //使用idWord获取一个id值
        advice.set_id(idWorker.nextId()+"");
        advice.setState("1");
        adviceDao.save(advice);

        //判断哪些是吐槽的评论，筛选出来(key:parent_id)
        if(advice.getParentid()!=null && !advice.getParentid().equals("")){
            //更新该评论对应的吐槽的回复数+1

            //1.创建查询对象
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(advice.getParentid()));

            //2.创建修改对象
            Update update = new Update();
            update.inc("comment",1);

            mongoTemplate.updateFirst(query,update,"advice");
        }
    }

    /**
     * 修改
     */
    public void update(Advice advice){
        adviceDao.save(advice);
    }

    /**
     * 删除
     */
    public void deleteById(String id){
        adviceDao.deleteById(id);
    }

    /**
     * 根据上级ID查询吐槽数据
     * @param parentid
     * @param page
     * @param size
     * @return
     */
    public Page<Advice> findByParentId(String parentid, int page, int size) {
        return adviceDao.findByParentidOrderByPublishtime(parentid, PageRequest.of(page-1,size));
    }



    /**
     * 点赞只修改对应的字段
     */
    public void thumbUp(String adviceid){

        //条件
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(adviceid));

        //修改对象
        Update update = new Update();
        //增加字段值
        update.inc("thumbup",1);

        mongoTemplate.updateFirst(query,update,"advice");
    }


}