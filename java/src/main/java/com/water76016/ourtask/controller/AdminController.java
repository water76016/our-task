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
    /**
     * @param
     * @return 返回结果消息
     */

    @ApiOperation("管理员冻结一个用户")
    @GetMapping("/freezeUser/{id}")
    public RestResult freezeUserById(@PathVariable @ApiParam("用户id") Integer id){
        User user = userService.getById(id);
        user.setStatus(0);
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
        if (status != 0 && status != 1){
            return RestResult.error("错误的传值");
        }
        Map<String, Object> map = new HashMap<>(5);
        map.put("status", status);
        List<User> userList = userService.listByMap(map);
        return userList != null ? RestResult.success() : RestResult.error();
    }
}
