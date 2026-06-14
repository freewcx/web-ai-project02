package com.itheima.mapper;

import com.itheima.pojo.EmpExpr;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmpExprMapper {

    //批量插入员工信息，在xml文件中配置，因为SQL语句比较复杂，需要插入动态插入多个员工经历
    void insertBatch(List<EmpExpr> exprList);
}
