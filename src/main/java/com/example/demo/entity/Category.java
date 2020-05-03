package com.example.demo.entity;

import org.apache.ibatis.type.Alias;

import java.util.Date;

@Alias("category")
public class Category {
    private Integer id;
    private String name;
    private Date createTime;
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Category(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
