package com.soob.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 后台返回结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult {
    private int status;
    private String msg;
    private Object data;

    public CommonResult(int status,String msg){
        this.status=status;
        this.msg=msg;
    }
}
