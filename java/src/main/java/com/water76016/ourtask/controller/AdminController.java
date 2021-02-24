package com.water76016.ourtask.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.water76016.ourtask.common.RestResult;
import com.water76016.ourtask.common.ResultCode;
import com.water76016.ourtask.entity.User;
import com.water76016.ourtask.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @program: our-task
 * @description: 管理员控制器类
 * @author: water76016
 * @create: 2020-09-24 16:45
 **/
@Api(value = "管理员控制器", tags = {"管理员操作"})
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UserService userService;

    @ApiOperation("管理员冻结/解冻一个用户")
    @GetMapping("/freezeUser/{id}/{status}")
    public RestResult freezeUserById(@PathVariable @ApiParam("用户id") Integer id,
                                     @PathVariable @ApiParam("用户id") Integer status){
        if (id == null){
            return RestResult.errorParams("用户id为空");
        }
        if (status != 0 || status != 1){
            return RestResult.errorParams("用户状态只能为0或者1");
        }
        User user = userService.getById(id);
        user.setStatus(status);
        boolean flag = userService.updateById(user);
        return flag ? RestResult.success() : RestResult.error();
    }

    @ApiOperation("查询所有用户信息")
    @GetMapping("listAllUser")
    public RestResult listAllUser(){
        List<User> userList = userService.list();
        return userList != null ? RestResult.success() : RestResult.error();
    }

    @ApiOperation("查询所有冻结/未冻结的用户信息")
    @GetMapping("listUser/{status}")
    public RestResult listUserByStatus(@PathVariable @ApiParam("传入用户状态") Integer status){
        if (status == null){
            return RestResult.errorParams("传入用户状态为空");
        }
        if (status != 0 && status != 1){
            return RestResult.errorParams("传入的用户状态只能为0或1");
        }
        Map<String, Object> map = new HashMap<>(5);
        map.put("status", status);
        List<User> userList = userService.listByMap(map);
        return userList != null ? RestResult.success() : RestResult.error();
    }
}
