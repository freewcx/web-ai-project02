package com.itheima.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
/*封装登录结果信息*/
public class LoginInfo {
    private Integer id;
    private String userName;
    private String name;
    private String token;
}
