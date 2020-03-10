package com.xusy.springbt.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface InsuranceManageMapper {
    void insertInfoInsuranceChecke(@Param("list") List<Map<String, Object>> data, @Param("type") String type,
                                   @Param("userName") String userName, @Param("userId") String userId);
}
