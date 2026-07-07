package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.EmpExprMapper;
import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.*;
import com.itheima.service.EmpLogService;
import com.itheima.service.EmpService;
import com.itheima.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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

    @Override
    public Emp getById(Integer id) {
        return empMapper.getById(id);
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void update(Emp emp) {
        //1.根据id修改员工信息
    emp.setUpdateTime(LocalDateTime.now());
    empMapper.updateById(emp);

        //2.根据id修改员工工作经历（包括删除员工经历再新增员工信息两步）
    empExprMapper.deleteByEmpIds(Arrays.asList(emp.getId()));

    //添加员工经历
        List<EmpExpr> exprList = emp.getExprList();
        if(!CollectionUtils.isEmpty(exprList)){
            exprList.forEach(empExpr->empExpr.setEmpId(emp.getId()));
        }
        empExprMapper.insertBatch(exprList);
    }

    @Override
    public LoginInfo login(Emp emp) {
        //根据用户名和密码查询用户信息
        Emp e = empMapper.selectByUsernameAndPassword(emp);

        //判断是否有用户信息，若有组装返回的员工信息，若无返回null给Controller层，用于info的null判定，再有Controller返回error给前端
        if(e!=null){
            //token是令牌，也是返回给前端的LoginInfo员工信息的属性，但是员工Emp里是没有的，sql是拿不到这个不存在的token的，所以返回的员工信息e里没有这个token的get方法
            //但是返回给前端的员工登录信息LofinInfo是需要这个属性的，所以暂时给他赋值为空字符串
            log.info("登录成功，员工信息：{}",e);
            Map<String,Object> dataMap = new HashMap<>();
            dataMap.put("id",e.getId());
            dataMap.put("username",e.getUsername());

            String jwt = JwtUtils.generateJwt(dataMap);
            return new LoginInfo(e.getId(),e.getUsername(),e.getName(),jwt);
        }
        return null;
    }
}

