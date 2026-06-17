package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.EmpExprMapper;
import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.*;
import com.itheima.service.EmpLogService;
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
    @Autowired
    EmpLogService empLogService;


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

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void save(Emp emp){

        try {
            //保存员工基本信息
            emp.setCreateTime(LocalDateTime.now());
            emp.setUpdateTime(LocalDateTime.now());
            empMapper.insert(emp);

            //保存员工工作经历
            Integer empId = emp.getId(); //从emp员工信息中获得员工id，赋值给员工经历的关联的员工id
            List<EmpExpr> exprList = emp.getExprList(); //从前端传入的员工信息中获得员工经历，封装到员工经理集合中
            if(!CollectionUtils.isEmpty(exprList)){ //调用CollectionUtils.isEmpty()，判断员工经历是否为空
                exprList.forEach(empExpr -> empExpr.setEmpId(empId));//将员工经历id赋值到员工经历id中
                empExprMapper.insertBatch(exprList);
            }
        } finally {
            //记录操作日志
            EmpLog empLog = new EmpLog(null,LocalDateTime.now(),"新增员工："+emp); //直接把新增员工对象传入
            empLogService.insertLog(empLog);
        }


    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void delete(List<Integer> ids) {
        //1.批量删除员工基本信息
        empMapper.deleteByIds(ids);
        //2.批量删除员工工作经历
        empExprMapper.deleteByEmpIds(ids);
    }
}

