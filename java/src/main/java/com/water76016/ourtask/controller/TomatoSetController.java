package com.water76016.ourtask.controller;


import com.water76016.ourtask.common.RestResult;
import com.water76016.ourtask.entity.TomatoSet;
import com.water76016.ourtask.service.TomatoSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author github:water76016
 * @since 2021-03-02
 */
@RestController
@RequestMapping("/tomato-set")
public class TomatoSetController {
    @Autowired
    TomatoSetService tomatoSetService;

    /**
     * 添加一个番茄设置
     * @param tomatoSet
     * @return
     */
    @PostMapping("/save")
    public RestResult save(TomatoSet tomatoSet){
        if (tomatoSet.getUserId() == null){
            return RestResult.errorParams("用户id不能为空");
        }
        boolean flag = tomatoSetService.save(tomatoSet);
        if (flag == false){
            return RestResult.error();
        }
        return RestResult.success();
    }

    /**
     * 更新一个番茄设置
     * @param tomatoSet
     * @return
     */
    @PostMapping("/update")
    public RestResult update(TomatoSet tomatoSet){
        if (tomatoSet.getId() == null){
            return RestResult.errorParams("番茄设置id不能为空");
        }
        if (tomatoSet.getUserId() == null){
            return RestResult.errorParams("用户id不能为空");
        }
        boolean flag = tomatoSetService.updateById(tomatoSet);
        if (flag == false){
            return RestResult.error();
        }
        return RestResult.success();
    }
}
