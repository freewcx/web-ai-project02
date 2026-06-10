package com.itheima.service.impl;

public class EmpServiceimpl {

    @Autowired
    EmpMapper empMapper;

    @Override
    public PageResult<Emp> page(Integer page, Integer pageSize){
        Long total = empMapper.count();

        Integer start = (page - 1) * pageSize;
        List<Emp> rows = empMapper.page(start, pageSize);

        return new PageResult<Emp>(total,  rows);
    }

}

