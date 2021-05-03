package com.potato.search.service;

import com.potato.search.dao.MessageDao;
import com.potato.search.pojo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import util.IdWorker;
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
     * 添加
     * @param message
     */
    public void add(Message message) {

        //message.setId(idWorker.nextId()+"");
        messageDao.save(message);
    }

    /**
     * 搜索资讯
     * @param keywords
     * @param page
     * @param size
     * @return
     */
    public Page<Message> findSearch(String keywords, int page, int size) {

        return messageDao.findByTitleOrContentLike(keywords,keywords, PageRequest.of(page-1,size));

    }
}