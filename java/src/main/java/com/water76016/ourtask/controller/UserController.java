package com.water76016.ourtask.controller;

import cn.hutool.core.util.StrUtil;
import com.water76016.ourtask.common.RestResult;
import com.water76016.ourtask.common.Utils;
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
    public RestResult update(@RequestBody @ApiParam("用户对象") User user){
        if (StrUtil.isEmpty(user.getUsername())){
            return RestResult.errorParams("用户名不能为空");
        }
        if (StrUtil.isEmpty(user.getPassword())){
            return RestResult.errorParams("用户密码不能为空");
        }
        boolean flag = userService.updateById(user);
        return flag ? RestResult.success() : RestResult.error();
    }

    @ApiOperation("根据id返回一个用户信息")
    @GetMapping("/get/{id}")
    public RestResult get(@PathVariable @ApiParam("用户id") Integer id){
        User user = userService.getById(id);
        if (user == null){
            return RestResult.notFindError("该用户不存在");
        }
        return RestResult.success(user);
    }

    @ApiOperation("修改用户密码")
    @PostMapping("updatePassword/{id}")
    public RestResult updatePassword(@PathVariable("id") @ApiParam("用户id") Integer id,
                                     @ApiParam("旧密码") String oldPassword,
                                     @ApiParam("新密码") String newPassword){
        if (id == null){
            return RestResult.errorParams("用户id为空");
        }
        if (StrUtil.isEmpty(oldPassword)){
            return RestResult.errorParams("旧密码不能为空");
        }
        if (StrUtil.isEmpty(newPassword)){
            return RestResult.errorParams("新密码不能为空");
        }
        //先判断用户名和密码是否匹配
        User user = userService.getById(id);
        if (user == null){
            return RestResult.notFindError("该用户不存在");
        }
        if (Utils.matches(oldPassword, user.getPassword()) == false){
            return RestResult.error("用户名和密码不匹配");
        }
        String password = Utils.encode(newPassword);
        user.setPassword(password);
        boolean flag = userService.updateById(user);
        return flag ? RestResult.success() : RestResult.error();
    }
}
