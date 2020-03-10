package com.xusy.springbt.service.impl;

import com.xusy.springbt.mapper.InsuranceManageMapper;
import com.xusy.springbt.service.BigDataTestService;
import com.xusy.springbt.util.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@Service
public class BigDataTestServiceImpl implements BigDataTestService {
    private static Logger logger = LoggerFactory.getLogger(BigDataTestServiceImpl.class);
    @Autowired
    private InsuranceManageMapper insuranceManageMapper;

    private Semaphore semaphore = new Semaphore(20);

    @Override
    @Async
    public void storageMillionsData(List<Map<String, Object>> dataList) {
        logger.info("数据量===" + dataList.size());
        if (!CollectionUtils.isEmpty(dataList)) {
            int capacitySize = 5000;
            if (dataList.size() <= capacitySize) {
                insuranceManageMapper.insertInfoInsuranceChecke(dataList, "生鲜", "test", "123");
            } else {
                List<List<Map<String, Object>>> listList = ListUtils.groupList(dataList, capacitySize);
                ExecutorService executor = Executors.newFixedThreadPool(10);
                listList.forEach(list -> {
                    try {
                        semaphore.acquire();
                        executor.execute(() -> {
                            try {
                                insuranceManageMapper.insertInfoInsuranceChecke(list, "生鲜", "test", "123");
                            } catch (Exception e) {
                                logger.error("归档大数据异常", e);
                            }
                        });
                    } catch (InterruptedException e) {
                        logger.error("归档大数据异常", e);
                    } finally {
                        semaphore.release();
                    }
                });
                //关闭线程池
                executor.shutdown();

            }
        }
    }
}
