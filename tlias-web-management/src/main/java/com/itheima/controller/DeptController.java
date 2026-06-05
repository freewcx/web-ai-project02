package com.itheima.controller;

import com.itheima.pojo.Dept;
import com.itheima.pojo.Result;
import com.itheima.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/depts")
@RestController
public class DeptController {

    @Autowired
    private  DeptService deptService;


    //@RequestMapping(value = "/depts",method = RequestMethod.GET)
    /*根据更新时间的倒序获取部门数据*/
    @GetMapping
    public Result list(){
        //System.out.println("查询全部的部门数据");
        List<Dept> deptList = deptService.findAll();
        log.info("查询全部的部门数据：{}",deptList);
        return Result.success(deptList);
    }
    /**
     * 根据id删除部门 - delete http://localhost:8080/depts?id=1
     */
    @DeleteMapping
    public Result delete(Integer id){
        log.info("根据id删除部门，id = {}" , id);
        deptService.deleteById(id);
        return Result.success();
    }

    @PostMapping
    public Result add(@RequestBody Dept dept){
        log.info("新增部门：{}",dept);
        deptService.add(dept);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id){
        log.info("根据id查询部门信息，id = {}", id);
        Dept dept = deptService.getById(id);
        return Result.success(dept);
    }

    @PutMapping
    public Result update(@RequestBody Dept dept){
        log.info("更新部门信息：{}",dept);
        deptService.update(dept);
        return Result.success();
    }


}
