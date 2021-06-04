package com.potato.user.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
/**
 * 实体类
 * @author Administrator
 *
 */
@Entity
@Table(name="tb_user")
public class User implements Serializable{

    @Id
    private String id;//ID



    private String mobile;//手机号码
    private String password;//密码
    private String nickname;//昵称
    private String sex;//性别

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm")
    private Date birthday;//出生日期

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm")
    private java.util.Date regdate;//注册日期
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm")
    private java.util.Date updatedate;//修改日期
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm")
    private java.util.Date lastdate;//最后登陆日期
    private String avatar;//头像
    private Float water;//水费
    private Float electric;//电费
    private Float network;//网费
    private Float property;//物业费
    private String aid;//发给管家的消息
    private String isdispose;//管家是否处理
    private String gatheringid;//参加的活动id

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }

    public java.util.Date getBirthday() {
        return birthday;
    }
    public void setBirthday(java.util.Date birthday) {
        this.birthday = birthday;
    }

    public java.util.Date getRegdate() {
        return regdate;
    }
    public void setRegdate(java.util.Date regdate) {
        this.regdate = regdate;
    }

    public java.util.Date getUpdatedate() {
        return updatedate;
    }
    public void setUpdatedate(java.util.Date updatedate) {
        this.updatedate = updatedate;
    }

    public java.util.Date getLastdate() {
        return lastdate;
    }
    public void setLastdate(java.util.Date lastdate) {
        this.lastdate = lastdate;
    }

    public Float getWater() {
        return water;
    }

    public void setWater(Float water) {
        this.water = water;
    }

    public Float getElectric() {
        return electric;
    }

    public void setElectric(Float electric) {
        this.electric = electric;
    }

    public Float getNetwork() {
        return network;
    }

    public void setNetwork(Float network) {
        this.network = network;
    }

    public Float getProperty() {
        return property;
    }

    public void setProperty(Float property) {
        this.property = property;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getIsdispose() {
        return isdispose;
    }

    public void setIsdispose(String isdispose) {
        this.isdispose = isdispose;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGatheringid() {
        return gatheringid;
    }

    public void setGatheringid(String gatheringid) {
        this.gatheringid = gatheringid;
    }

    public User() {
    }
}
