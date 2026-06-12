package com.itheima.mapper;

import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmpMapper {

/*    *//*查询总记录数*//*
    @Select("select count(*) from emp e left join dept d on e.dept_id = d.id")
    public Long count();

    *//*查询结果列表*//*
    @Select("select e.*,d.name from emp e left join dept d on e.dept_id = d.id " +
            "order by e.update_time desc limit #{start},#{pageSize}")
    public List<Emp> list(Integer start,Integer pageSize);*/

/*@Select("select e.*,d.name deptName from emp e left join dept d on e.dept_id = d.id " +
        "order by update_time desc")
    List<Emp> list();*/

    /**
     * 根据查询条件查询员工
     */
    List<Emp> list(EmpQueryParam empQueryParam);

}
