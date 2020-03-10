package com.xusy.springbt.util;

import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListUtils {

    /**
     * 分组组装list
     * @param mapList
     * @param capacitySize 容量大小
     * @return
     */
    public static List<List<Map<String, Object>>> groupList(List<Map<String, Object>> mapList, int capacitySize) {
        List<List<Map<String, Object>>> mapLists = new ArrayList<>();
        int length = mapList.size();
        int groupSize = (length + capacitySize - 1) / capacitySize;
        for (int i = 0; i < groupSize; i++) {
            List<Map<String, Object>> maps = new ArrayList<>(groupSize);
            // 开始位置
            int fromIndex = i * capacitySize;
            // 结束位置
            int toIndex = (i + 1) * capacitySize < length ? (i + 1) * capacitySize : length;
            maps.addAll(mapList.subList(fromIndex, toIndex));
            mapLists.add(maps);
        }
        return mapLists;
    }
}
