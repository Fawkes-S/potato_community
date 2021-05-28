package com.potato.advice.service;

import com.potato.advice.dao.AdviceDao;
import com.potato.advice.pojo.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
     * @param whereMap 查询条件封装
     * @param page
     * @param size
     * @return
     */
    public Page<Advice> findSearch(Map whereMap, int page, int size) {
        PageRequest pageRequest =  PageRequest.of(page-1, size);
        String state = (String) whereMap.get("state");
        String parentid = (String) whereMap.get("parentid");
        System.out.println("@@"+whereMap);
        Page<Advice> pageList = adviceDao.findByParentidOrderByPublishtime(parentid,pageRequest);
        System.out.println("$$"+pageList);
//        return adviceDao.findByParentidOrderByPublishtime(parentid,pageRequest);
        return adviceDao.findByStateAndParentidOrderByPublishtime(state,parentid,pageRequest);

    }

    /**
     * 动态条件分页查询
     * @param whereMap
     * @return
     */
    public Page<Advice> backSearch(Map whereMap, int page, int size) {
        PageRequest pageRequest =  PageRequest.of(page-1, size);
        Query query = new Query();

        //MongoDB动态拼接查询条件
        if (whereMap.get("userid")!=null && !"".equals(whereMap.get("userid"))){
            query.addCriteria(Criteria.where("userid").is(whereMap.get("userid")));
        }
        if (whereMap.get("nickname")!=null && !"".equals(whereMap.get("nickname"))){
            query.addCriteria(Criteria.where("nickname").is(whereMap.get("nickname")));
        }
            query.addCriteria(Criteria.where("parentid").is(whereMap.get("parentid")));
        if (whereMap.get("state")!=null && !"".equals(whereMap.get("state"))){
            query.addCriteria(Criteria.where("state").is(whereMap.get("state")));
        }
        if (whereMap.get("visits")!=null && !"".equals(whereMap.get("visits"))){
            query.addCriteria(Criteria.where("visits").gte(Integer.parseInt(whereMap.get("visits").toString())));
        }
        if (whereMap.get("thumbup")!=null && !"".equals(whereMap.get("thumbup"))){
            query.addCriteria(Criteria.where("thumbup").gte(Integer.parseInt(whereMap.get("thumbup").toString())));
        }
        if (whereMap.get("share")!=null && !"".equals(whereMap.get("share"))){
            query.addCriteria(Criteria.where("share").gte(Integer.parseInt(whereMap.get("share").toString())));
        }
        if (whereMap.get("comment")!=null && !"".equals(whereMap.get("comment"))){
            query.addCriteria(Criteria.where("comment").gte(Integer.parseInt(whereMap.get("comment").toString())));
        }
        Date publishDate = null;
        if (whereMap.get("publishtime")!=null && !"".equals(whereMap.get("publishtime"))){
            try {                                                                           //mongoDB查询类型要一样，转为Date型
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                publishDate = format.parse(whereMap.get("publishtime").toString());
            }catch (Exception e){
                e.printStackTrace();
            }
            query.addCriteria(Criteria.where("publishtime").gte(publishDate));
        }


        long total = mongoTemplate.count(query, Advice.class);
        //查询结果集
        List<Advice> advicetList = mongoTemplate.find(query.with(pageRequest), Advice.class);
        Page<Advice> advicetPage = new PageImpl(advicetList, pageRequest, total);
        return advicetPage;
    }

    /**
     * 添加
     */
    public void add(Advice advice){

        //使用idWord获取一个id值
        advice.set_id(idWorker.nextId()+"");
        advice.setState("1");
        advice.setPublishtime(new Date());
        adviceDao.save(advice);

        //判断哪些是吐槽的评论，筛选出来(parent_id)
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