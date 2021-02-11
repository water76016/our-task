package com.water76016.ourtask.controller;

import com.water76016.ourtask.common.RestResult;
import com.water76016.ourtask.entity.User;
import com.water76016.ourtask.service.CategoryService;
import com.water76016.ourtask.service.TaskService;
import com.water76016.ourtask.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author github:water76016
 * @since 2020-07-21
 */
@Api(value = "用户控制", tags = {"用户操作"})
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    TaskService taskService;

    @ApiOperation("修改用户基本信息")
    @PostMapping("update/{id}")
    public RestResult update(@PathVariable("id") @ApiParam("用户id") Integer id,
                             @RequestBody @ApiParam("用户对象") User user){
        boolean flag = userService.updateById(user);
        return flag ? RestResult.success() : RestResult.error();
    }

    @ApiOperation("根据id返回一个用户信息")
    @GetMapping("/get/{id}")
    public RestResult get(@PathVariable @ApiParam("用户id") Integer id){
        User user = userService.getById(id);
        return RestResult.success(user);
    }

    @ApiOperation("修改用户密码")
    @PostMapping("updatePassword/{id}")
    public RestResult updatePassword(@PathVariable("id") @ApiParam("用户id") Integer id,
                                     @ApiParam("旧密码") String oldPassword,
                                     @ApiParam("新密码") String newPassword){
        //先判断用户名和密码是否匹配
        BCryptPasswordEncoder bcp = new BCryptPasswordEncoder();
        User user = userService.getById(id);
        if (bcp.matches(oldPassword, user.getPassword()) == false){
            return RestResult.error("用户名和密码不匹配");
        }
        String password = bcp.encode(newPassword);
        user.setPassword(password);
        boolean flag = userService.updateById(user);
        return flag ? RestResult.success() : RestResult.error();
    }
}
