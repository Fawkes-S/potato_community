package com.potato.base.service;

import com.potato.base.dao.LabelDao;
import com.potato.base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class LabelService {
    @Autowired
    private LabelDao labelDao;
    @Autowired
    private IdWorker idWorker;

    /**
     * 查询所有
     * @return
     */
    public List<Label> findAll() {
        return labelDao.findAll();
    }

    public Label findById(String id) {
        return labelDao.findById(id).orElse(null);
    }

    public void save(Label label) {
        // 根据雪花算法生成分布式id
        label.setId(String.valueOf(idWorker.nextId()));
        labelDao.save(label);
    }

    public void update(Label label) {
        labelDao.save(label);
    }

    public void deleteById(String id) {
        labelDao.deleteById(id);
    }


    private Specification<Label> createSpecification(Label label){
        return (Specification<Label>) (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            if(label.getLabelName()!=null&&!label.getLabelName().equals("")){
                Predicate predicate = cb.like(root.get("labelname").as(String.class),"%"+label.getLabelName()+"%");
                list.add(predicate);
            }
            if(label.getState()!=null&&!label.getState().equals("")){
                Predicate predicate = cb.equal(root.get("state").as(String.class),label.getState());
                list.add(predicate);
            }
            //new 一个数组作为最终返回值的条件
            Predicate[] array = new Predicate[list.size()];
            list.toArray(array);
            return cb.and(array);
        };

    }

    /**
     * 条件查询
     * @param label
     * @return
     */
    public List<Label> findSearch(Label label) {
        //创建Specification对象
        Specification<Label> spec = createSpecification(label);
        List<Label> list = labelDao.findAll(spec);
        return list;
    }

    /**
     * 分页查询
     * @param label
     * @param currentPage
     * @param pageSize
     * @return
     */
    public Page<Label> pageQuery(Label label, int currentPage, int pageSize) {
        Specification<Label> spec = createSpecification(label);
        return labelDao.findAll( spec, PageRequest.of(currentPage - 1, pageSize));//框架从0开始
    }
}