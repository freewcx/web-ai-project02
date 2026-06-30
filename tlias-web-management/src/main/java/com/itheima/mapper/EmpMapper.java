package com.itheima.mapper;

import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

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

    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into emp(username, name, gender, phone, job, salary, image, entry_date, dept_id, create_time, update_time)" +
            "values(#{username},#{name},#{gender},#{phone},#{job},#{salary},#{image},#{entryDate},#{deptId},#{createTime},#{updateTime})")
    void insert(Emp emp);





    /*根据ID批量删除员工的基本信息*/
    void deleteByIds(List<Integer> ids);


    //根据id查询员工信息及员工经历信息
    Emp getById(Integer id);

    void updateById(Emp emp);

    /*统计员工职位人数*/
    @MapKey("pos")
    List<Map<String,Object>> countEmpJobData();

    /*统计员工性别人数*/
    @MapKey("name")
    List<Map<String, Object>> countEmpGdenderData();

    @Select("select id,username,name from emp where username = #{username} and password = #{password}")
    Emp selectByUsernameAndPassword(Emp emp);
}
