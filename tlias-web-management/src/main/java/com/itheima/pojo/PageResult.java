package com.itheima.pojo;

/*分页结果封装类*/

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {
    private Long total;
    private List<T> row;
}
