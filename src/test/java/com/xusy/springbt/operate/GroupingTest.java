package com.xusy.springbt.operate;

import com.xusy.springbt.domain.FlowNodeExecutor;
import com.xusy.springbt.domain.OrderInfoModel;
import com.xusy.springbt.domain.OrderItem;
import com.xusy.springbt.domain.ZtreeModel;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xsy
 * @create 2019-08-13 17:49
 * @desc 测试分组
 **/
public class GroupingTest {

    private static List<FlowNodeExecutor> flowNodeExecutorList = new ArrayList<>();
    private static List<OrderInfoModel> orderInfoModelList = new ArrayList<>();

    static {
        ZtreeModel ztreeModel = new ZtreeModel();
        ztreeModel.setId("1");
        ztreeModel.setName("name1");
        FlowNodeExecutor flowNodeExecutor = new FlowNodeExecutor();
        flowNodeExecutor.setId("11");
        flowNodeExecutor.setNodeId("123123123");
        List<ZtreeModel> executorList = new ArrayList<>();
        executorList.add(ztreeModel);
        flowNodeExecutor.setExecutorList(executorList);
        flowNodeExecutorList.add(flowNodeExecutor);

        ztreeModel = new ZtreeModel();
        ztreeModel.setId("2");
        ztreeModel.setName("name2");
        flowNodeExecutor = new FlowNodeExecutor();
        flowNodeExecutor.setId("22");
        flowNodeExecutor.setNodeId("456456456");
        executorList = new ArrayList<>();
        executorList.add(ztreeModel);
        flowNodeExecutor.setExecutorList(executorList);
        flowNodeExecutorList.add(flowNodeExecutor);

        //order
        Date now = new Date();
        Date nowAfter1 = DateUtils.addDays(now, 1);
        List<OrderItem> orderItems = new ArrayList<>();
        OrderInfoModel.builder()
                .date(now)
                .orderItems(orderItems);
    }

    @Test
    public void test1() {
        //分组
        Map<String, Set<List<ZtreeModel>>> nodeExecutorMap = flowNodeExecutorList.stream().collect(
                Collectors.groupingBy(FlowNodeExecutor::getNodeId,
                        Collectors.mapping(FlowNodeExecutor::getExecutorList,
                                Collectors.toSet())));
        for (Map.Entry<String, Set<List<ZtreeModel>>> entry : nodeExecutorMap.entrySet()) {
            System.out.println("key=" + entry.getKey() + "；value=" + entry.getValue());
        }
        System.out.println("========");
        //排序
        Map<String, Set<List<ZtreeModel>>> nodeExecutorSortMap = new LinkedHashMap<>();
        nodeExecutorMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).
                forEachOrdered(x -> nodeExecutorSortMap.put(x.getKey(), x.getValue()));
        nodeExecutorSortMap.entrySet().forEach(e -> System.out.println("key=" + e.getKey() + "；value=" + e.getValue()));

        System.out.println("========");
        Map<String, List<FlowNodeExecutor>> collect = flowNodeExecutorList.stream().collect(
                Collectors.groupingBy(FlowNodeExecutor::getNodeId, Collectors.toList()));
        collect.entrySet().forEach(e -> System.out.println("key=" + e.getKey() + "；value=" + e.getValue()));
    }
}
