package com.potato.resource.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
import org.springframework.stereotype.Service;

import util.IdWorker;

import com.potato.resource.dao.SiteDao;
import com.potato.resource.pojo.Site;

/**
 * 服务层
 *
 * @author Administrator
 *
 */
@Service
public class SiteService {

    @Autowired
    private SiteDao siteDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 查询全部列表
     * @return
     */
    public List<Site> findAll() {
        return siteDao.findAll();
    }


    /**
     * 条件查询+分页
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<Site> findSearch(Map whereMap, int page, int size) {
        Specification<Site> specification = createSpecification(whereMap);
        PageRequest pageRequest =  PageRequest.of(page-1, size);
        return siteDao.findAll(specification, pageRequest);
    }


    /**
     * 条件查询
     * @param whereMap
     * @return
     */
    public List<Site> findSearch(Map whereMap) {
        Specification<Site> specification = createSpecification(whereMap);
        return siteDao.findAll(specification);
    }

    /**
     * 根据ID查询实体
     * @param id
     * @return
     */
    public Site findById(String id) {
        return siteDao.findById(id).get();
    }

    /**
     * 增加
     * @param site
     */
    public void add(Site site) {
        site.setId( idWorker.nextId()+"" );
        siteDao.save(site);
    }

    /**
     * 修改
     * @param site
     */
    public void update(Site site) {
        siteDao.save(site);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteById(String id) {
        siteDao.deleteById(id);
    }

    /**
     * 动态条件构建
     * @param searchMap
     * @return
     */
    private Specification<Site> createSpecification(Map searchMap) {

        return new Specification<Site>() {

            @Override
            public Predicate toPredicate(Root<Site> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 场地名称
                if (searchMap.get("sitename")!=null && !"".equals(searchMap.get("sitename"))) {
                    predicateList.add(cb.like(root.get("sitename").as(String.class), "%"+(String)searchMap.get("sitename")+"%"));
                }
                // 场地类型
                if (searchMap.get("type")!=null && !"".equals(searchMap.get("type"))) {
                    predicateList.add(cb.like(root.get("type").as(String.class), "%"+(String)searchMap.get("type")+"%"));
                }
                // 场地地址
                if (searchMap.get("address")!=null && !"".equals(searchMap.get("address"))) {
                    predicateList.add(cb.like(root.get("address").as(String.class), "%"+(String)searchMap.get("address")+"%"));
                }
                // 场地描述
                if (searchMap.get("content")!=null && !"".equals(searchMap.get("content"))) {
                    predicateList.add(cb.like(root.get("content").as(String.class), "%"+(String)searchMap.get("content")+"%"));
                }
                // 联系人姓名
                if (searchMap.get("telename")!=null && !"".equals(searchMap.get("telename"))) {
                    predicateList.add(cb.like(root.get("telename").as(String.class), "%"+(String)searchMap.get("telename")+"%"));
                }
                // 联系电话
                if (searchMap.get("tele")!=null && !"".equals(searchMap.get("tele"))) {
                    predicateList.add(cb.like(root.get("tele").as(String.class), "%"+(String)searchMap.get("tele")+"%"));
                }
                // 状态
                if (searchMap.get("state")!=null && !"".equals(searchMap.get("state"))) {
                    predicateList.add(cb.like(root.get("state").as(String.class), "%"+(String)searchMap.get("state")+"%"));
                }
                // 网址
                if (searchMap.get("url")!=null && !"".equals(searchMap.get("url"))) {
                    predicateList.add(cb.like(root.get("url").as(String.class), "%"+(String)searchMap.get("url")+"%"));
                }



                return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }

    /**
     * 推荐场地
     * @return
     */
    public List<Site> recommend() {
        /**
         *  Top4: 取前面4条
         *  ByState: 根据state属性查询
         *  OrderByCreateTimeDesc: 按照倒序查询createTime
         */

        List<Site> list = siteDao.findTop4ByStateOrderByCreatetimeDesc("2");
        return list;
    }

    /**
     * 最新职位
     * @return
     */
    public List<Site> newList() {

        /**
         * Top12: 取前面10条件
         * ByStateNot: 查询state不为xx的值
         * OrderByCreateTimeDesc: 按照倒序查询createTime
         */
        List<Site> list = siteDao.findTop10ByStateNotOrderByCreatetimeDesc("0");
        return list;
    }
}