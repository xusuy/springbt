package com.xusy.springbt.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xsy
 * @create 2019-08-13 17:48
 * @desc
 **/
public class ZtreeModel {
    private String id;
    private String name;
    private String type;
    private String open;
    private List<ZtreeModel> children;
    private String parentId;
    private Boolean selected;

    public static final String DEPARTMENT_TYPE = "dep";
    public static final String EMPLOYEE_TYPE = "emp";

    public ZtreeModel() {
        children = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIconSkin() {
        return type;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public List<ZtreeModel> getChildren() {
        return children;
    }

    public void setChildren(List<ZtreeModel> children) {
        this.children = children;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "ZtreeModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", open='" + open + '\'' +
                ", children=" + children +
                ", parentId='" + parentId + '\'' +
                ", selected=" + selected +
                '}';
    }
}
