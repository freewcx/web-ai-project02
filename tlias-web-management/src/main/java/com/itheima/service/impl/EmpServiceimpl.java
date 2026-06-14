package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.EmpExprMapper;
import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpExpr;
import com.itheima.pojo.EmpQueryParam;
import com.itheima.pojo.PageResult;
import com.itheima.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmpServiceimpl implements EmpService {

    @Autowired
    EmpMapper empMapper;
    @Autowired
    EmpExprMapper empExprMapper;


    @Override
    public PageResult<Emp> page(EmpQueryParam empQueryParam){
/*        Long total = empMapper.count();

        Integer start = (page - 1) * pageSize;
        List<Emp> rows = empMapper.list(start, pageSize);

        return new PageResult<Emp>(total,  rows);*/

        PageHelper.startPage(empQueryParam.getPage(), empQueryParam.getPageSize());

        List<Emp> empList = empMapper.list(empQueryParam);

        Page<Emp> p = (Page<Emp>) empList;

        return new PageResult(p.getTotal(),p.getResult());

    }

    @Transactional
    @Override
    public void save(Emp emp){

        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.insert(emp);

        Integer empId = emp.getId(); //从emp员工信息中获得员工id，赋值给员工经历的关联的员工id
        List<EmpExpr> exprList = emp.getExprList(); //从前端传入的员工信息中获得员工经历，封装到员工经理集合中
        if(!CollectionUtils.isEmpty(exprList)){ //调用CollecttionUtils.isEmpty()，判断员工经历是否为空
            exprList.forEach(empExpr -> empExpr.setEmpId(empId));//将员工经历id赋值到员工经历id中
            empExprMapper.insertBatch(exprList);
        }
    }



}

