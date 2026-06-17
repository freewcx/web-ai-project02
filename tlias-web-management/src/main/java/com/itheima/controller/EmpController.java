package com.itheima.controller;

import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Result;
import com.itheima.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpController {

    @Autowired
    private EmpService empService;

 /*   @GetMapping()
    public Result page(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize){
        log.info("page: {}, pageSize: {}", page, pageSize);
        PageResult<Emp> pageResult = empService.page(page, pageSize);
        return Result.success(pageResult);
    }*/

    @GetMapping()
    public Result page(EmpQueryParam empQueryParam){
        log.info("条件分页查询：{}" , empQueryParam);
        PageResult<Emp> pageResult = empService.page(empQueryParam);
        return Result.success(pageResult);
    }


    @PostMapping()
    public Result save(@RequestBody Emp emp){
        log.info("新增员工：{}", emp);
        empService.save(emp);
        return Result.success();
    }

    /*用数组接收请求参数不需要加@RequestParam注解*/
    /*@DeleteMapping()
    public Result delete(Integer[] ids){
        log.info("删除员工“{}", Arrays.toString(ids));
        return Result.success();
    }*/

    //用集合需要加注解
    @DeleteMapping()
    public Result delete(@RequestParam List<Integer> ids){
        log.info("删除员工“{}", ids);
        empService.delete(ids);
        return Result.success();
    }
}
