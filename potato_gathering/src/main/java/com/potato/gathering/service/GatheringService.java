package com.potato.gathering.service;

import java.util.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import util.IdWorker;

import com.potato.gathering.dao.GatheringDao;
import com.potato.gathering.pojo.Gathering;

/**
 * 服务层
 *
 * @author Administrator
 *
 */
@Service
public class GatheringService {

    @Autowired
    private GatheringDao gatheringDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 查询全部列表
     * @return
     */
    public List<Gathering> findAll() {
        return gatheringDao.findAll();
    }


    /**
     * 条件查询+分页
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<Gathering> findSearch(Map whereMap, int page, int size) {
        Specification<Gathering> specification = createSpecification(whereMap);
        PageRequest pageRequest =  PageRequest.of(page-1, size);
        return gatheringDao.findAll(specification, pageRequest);
    }

//    /**
//     * 条件查询+分页(个人中心)
//     * @param whereMap
//     * @param page
//     * @param size
//     * @return
//     */
//    public Page<Gathering> findSearchManger(Map whereMap, int page, int size) {
//        String gatheringid = whereMap.get("gatheringid").toString();
//        gatheringid.split(",");
//        Specification<Gathering> specification = createSpecification(whereMap);
//        PageRequest pageRequest =  PageRequest.of(page-1, size);
//        Page<Gathering> pageList = new
//        return gatheringDao.findAll(specification, pageRequest);
//    }


    /**
     * 条件查询(个人中心)
     * @param whereMap
     * @return
     */
    public List<Gathering> findSearchM(Map whereMap) {
        String gatheringid = whereMap.get("id").toString();
        String[] glist =gatheringid.split(",");
        System.out.println("glist:"+ Arrays.toString(glist));
        List<Gathering> list = new ArrayList<>();
        for (int i = 0; i<glist.length; i++){
            Specification<Gathering> specification = createSpecification(whereMap);
            list.add(gatheringDao.findAll(specification).get(0));
        }
        return list;
    }

    /**
     * value参数，给该缓存数据一个别名，用在删除缓存数据的时候
     * #id: 把参数id值作为缓存的key （SpringCache自动把方法返回值放入key对应的value中）
     * 根据ID查询实体
     * @param id
     * @return
     */


    @Cacheable(value ="gathering" ,key ="#id")  //#取到方法中的id，优先从SpringCache中找
    public Gathering findById(String id) {
        return gatheringDao.findById(id).get();
    }

    /**
     * 增加
     * @param gathering
     */
    public void add(Gathering gathering) {
        gathering.setId( idWorker.nextId()+"" );
        gatheringDao.save(gathering);
    }

    /**
     * 修改
     * @param gathering
     */
    @CacheEvict(value = "gathering",key = "#gathering.id")
    public void update(Gathering gathering) {
        gatheringDao.save(gathering);
    }

    /**
     * 删除
     * @param id
     */
    @CacheEvict(value = "gathering",key = "#id")
    public void deleteById(String id) {
        gatheringDao.deleteById(id);
    }

    /**
     * 动态条件构建
     * @param searchMap
     * @return
     */
    private Specification<Gathering> createSpecification(Map searchMap) {

        return new Specification<Gathering>() {

            @Override
            public Predicate toPredicate(Root<Gathering> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // id
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 活动名称
                if (searchMap.get("name")!=null && !"".equals(searchMap.get("name"))) {
                    predicateList.add(cb.like(root.get("name").as(String.class), "%"+(String)searchMap.get("name")+"%"));
                }
                // 活动简介
                if (searchMap.get("summary")!=null && !"".equals(searchMap.get("summary"))) {
                    predicateList.add(cb.like(root.get("summary").as(String.class), "%"+(String)searchMap.get("summary")+"%"));
                }
                // 详细内容
                if (searchMap.get("detail")!=null && !"".equals(searchMap.get("detail"))) {
                    predicateList.add(cb.like(root.get("detail").as(String.class), "%"+(String)searchMap.get("detail")+"%"));
                }
                // 主办方
                if (searchMap.get("sponsor")!=null && !"".equals(searchMap.get("sponsor"))) {
                    predicateList.add(cb.like(root.get("sponsor").as(String.class), "%"+(String)searchMap.get("sponsor")+"%"));
                }
                // 举办地点
                if (searchMap.get("address")!=null && !"".equals(searchMap.get("address"))) {
                    predicateList.add(cb.like(root.get("address").as(String.class), "%"+(String)searchMap.get("address")+"%"));
                }
                // 是否可见
                if (searchMap.get("state")!=null && !"".equals(searchMap.get("state"))) {
                    predicateList.add(cb.like(root.get("state").as(String.class), "%"+(String)searchMap.get("state")+"%"));
                }
                // 开始时间之后
                if (searchMap.get("starttime")!=null && !"".equals(searchMap.get("starttime"))) {
                    predicateList.add(cb.greaterThanOrEqualTo(root.get("starttime").as(String.class), (String)searchMap.get("starttime")));
                }
                // 结束时间之前
                if (searchMap.get("endtime")!=null && !"".equals(searchMap.get("endtime"))) {
                    predicateList.add(cb.lessThanOrEqualTo(root.get("endtime").as(String.class), (String)searchMap.get("endtime")));
                }

                return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }

}