package com.xusy.springbt.domain;

import com.xusy.springbt.util.DateHelper;

import java.util.Date;
import java.util.List;

/**
 * @author xsy
 * @create 2019-08-13 17:48
 * @desc
 **/
public class FlowNodeExecutor {
    private String id;
    private String nodeId;
    private String executorType;
    private String executorId;
    private String processId;
    private List<ZtreeModel> executorList;
    private Date createTime;
    private String createDateItem;

    public List<ZtreeModel> getExecutorList() {
        return executorList;
    }

    public void setExecutorList(List<ZtreeModel> executorList) {
        this.executorList = executorList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getExecutorType() {
        return executorType;
    }

    public void setExecutorType(String executorType) {
        this.executorType = executorType;
    }

    public String getExecutorId() {
        return executorId;
    }

    public void setExecutorId(String executorId) {
        this.executorId = executorId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateDateItem() {
        return createDateItem;
    }

    public void setCreateDateItem(String createDateItem) {
        this.createDateItem = createDateItem;
    }

    //用来分组
    public String getCreateTimeDate() {
        return DateHelper.date2String(new Date(), DateHelper.DATE_PATTERN);
    }

    @Override
    public String toString() {
        return "FlowNodeExecutor{" +
                "id='" + id + '\'' +
                ", nodeId='" + nodeId + '\'' +
                ", executorType='" + executorType + '\'' +
                ", executorId='" + executorId + '\'' +
                ", processId='" + processId + '\'' +
                ", executorList=" + executorList +
                ", createTime=" + createTime +
                ", createDateItem='" + createDateItem + '\'' +
                '}';
    }
}
