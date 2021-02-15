package com.water76016.ourtask.controller;

import cn.hutool.core.util.StrUtil;
import com.water76016.ourtask.common.RestResult;
import com.water76016.ourtask.config.security.jwt.JwtAuthService;
import com.water76016.ourtask.entity.Tourist;
import com.water76016.ourtask.entity.User;
import com.water76016.ourtask.service.TouristService;
import com.water76016.ourtask.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


/**
 * @program: our-task
 * @description: 针对游客对象能够调用接口的控制器
 * @author: water76016
 * @create: 2020-09-24 16:45
 **/
@Api(value = "游客控制", tags = {"游客操作"})
@RestController
public class TouristController {
    @Autowired
    TouristService touristService;

    @ApiOperation("游客进行注册操作")
    @PostMapping("/register")
    public RestResult register(@RequestBody @ApiParam("游客对象") Tourist tourist){
        if (StrUtil.isEmpty(tourist.getUsername())){
            RestResult.errorParams("注册用户：用户名不能为空");
        }
        if (StrUtil.isEmpty(tourist.getPassword())){
            RestResult.errorParams("注册用户：用户密码不能为空");
        }
        return touristService.register(tourist);
    }

    @ApiOperation("游客进行登录操作")
    @PostMapping({"/login"})
    public RestResult login(@RequestBody @ApiParam("游客对象") Tourist tourist, HttpServletResponse response) {
        if (StrUtil.isEmpty(tourist.getUsername())){
            RestResult.errorParams("用户登录：用户名不能为空");
        }
        if (StrUtil.isEmpty(tourist.getPassword())){
            RestResult.errorParams("用户登录：用户密码不能为空");
        }
        return touristService.login(tourist, response);
    }

}
