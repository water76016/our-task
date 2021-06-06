package com.water76016.ourtask.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.water76016.ourtask.common.RestResult;
import com.water76016.ourtask.common.Utils;
import com.water76016.ourtask.config.security.jwt.JwtAuthService;
import com.water76016.ourtask.config.security.jwt.JwtTokenUtil;
import com.water76016.ourtask.entity.Tourist;
import com.water76016.ourtask.entity.User;
import com.water76016.ourtask.service.TouristService;
import com.water76016.ourtask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.rmi.CORBA.Util;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: our-task
 * @description: 游客操作服务接口实现类
 * @author: water76016
 * @create: 2020-09-24 16:45
 **/
@Service
public class TouristServiceImpl implements TouristService {
    @Autowired
    UserService userService;

    @Autowired
    JwtAuthService jwtAuthService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    /**
     * 游客注册服务
     * */
    @Override
    public RestResult register(Tourist tourist){
        String username = tourist.getUsername();
        String password = tourist.getPassword();
        User user = new User();
        user.setUsername(username);
        //加密需要把用户密码进行加密存储
        BCryptPasswordEncoder bcp = new BCryptPasswordEncoder();
        String encodePassword = bcp.encode(password);
        user.setPassword(encodePassword);
        user.setRoles("ROLE_USER");
        boolean flag = userService.save(user);
        return flag ? RestResult.success() : RestResult.error();
    }

    /**
     *游客登录服务
     */
    @Override
    public RestResult login(Tourist tourist, HttpServletResponse response) {
        User loginUser = jwtAuthService.login(tourist.getUsername(), tourist.getPassword());
        if (loginUser == null){
            return RestResult.error("用户名或密码错误");
        }
        //生成用户token
        String token = jwtTokenUtil.generateToken(loginUser);
        RestResult result = RestResult.success();
        //这里就已经登录成功了
        response.setHeader("Authorization", token);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        result.put("user", loginUser);
        return result;
    }
}
