package com.water76016.ourtask.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.water76016.ourtask.common.RestResult;
import com.water76016.ourtask.dto.LabelParam;
import com.water76016.ourtask.entity.Category;
import com.water76016.ourtask.entity.Label;
import com.water76016.ourtask.entity.TaskLabel;
import com.water76016.ourtask.service.LabelService;
import com.water76016.ourtask.service.TaskLabelService;
import com.water76016.ourtask.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author github:water76016
 * @since 2020-08-17
 */
@Api(value = "标签控制", tags = {"标签操作"})
@RestController
@RequestMapping("/label")
public class LabelController {
    @Autowired
    LabelService labelService;

    @Autowired
    TaskLabelService taskLabelService;

    @Autowired
    TaskService taskService;

    @ApiOperation("查询当前用户的所有标签")
    @GetMapping("listAll/{userId}")
    public RestResult getAllList(@PathVariable("userId") @ApiParam("用户id") Integer userId){
        if (userId == null){
            return RestResult.errorParams("用户id不能为空");
        }
        QueryWrapper<Label> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("run", 1);
        List<Label> labelList = labelService.list(queryWrapper);
        if (labelList == null || labelList.size() == 0){
            return RestResult.noContentSuccess();
        }
        List<LabelParam> labelParamList = new ArrayList<>();
        for (Label label : labelList){
            int labelId = label.getId();
            QueryWrapper<TaskLabel> taskLabelQueryWrapper = new QueryWrapper<>();
            taskLabelQueryWrapper.eq("label_id", labelId);
            Integer count = taskLabelService.count(taskLabelQueryWrapper);
            LabelParam labelParam = new LabelParam(labelId, label.getName(), count);
            labelParamList.add(labelParam);
        }
        return RestResult.success(labelParamList);
    }

    @ApiOperation("逻辑删除/完成一个标签")
    @GetMapping("/delete/{id}")
    public RestResult deleteLabelById(@PathVariable("id") @ApiParam("标签id") Integer id){
        if (id == null){
            return RestResult.errorParams("标签id不能为空");
        }
        boolean flag = labelService.removeById(id);
        return flag ? RestResult.success() : RestResult.error();
    }

    @ApiOperation("修改标签的名称")
    @PostMapping("update")
    public RestResult update(@RequestBody @ApiParam("标签对象") Label label){
        if (label.getUserId() == null){
            return RestResult.errorParams("用户id不能为空");
        }
        if (StrUtil.isEmpty(label.getName())){
            return RestResult.errorParams("标签名不能为空");
        }
        boolean flag = labelService.updateById(label);
        return flag ? RestResult.success() : RestResult.error();
    }

    @ApiOperation("添加一个新的标签")
    @PostMapping("add")
    public RestResult add(@RequestBody @ApiParam("标签对象") Label label){
        if (label.getUserId() == null){
            return RestResult.errorParams("用户id不能为空");
        }
        if (StrUtil.isEmpty(label.getName())){
            return RestResult.errorParams("标签名不能为空");
        }
        boolean flag = labelService.save(label);
        return flag ? RestResult.success(label) : RestResult.error();
    }

    @ApiOperation("查询当前用户，当前页的标签列表")
    @GetMapping("/getPageList/{userId}/{pageCurrent}/{pageSize}")
    public RestResult getPageList(@PathVariable("userId") @ApiParam("用户id") Integer userId,
                                  @PathVariable("pageCurrent") @ApiParam("当前页") Integer pageCurrent,
                                  @PathVariable("pageSize") @ApiParam("每页大小") Integer pageSize){
        if (userId == null){
            return RestResult.errorParams("用户id不能为空");
        }
        if (pageCurrent == null || pageCurrent == 0){
            return RestResult.errorParams("当前页不能为空或当前页不能为0");
        }
        if (pageSize == null || pageSize == 0){
            return RestResult.errorParams("每页大小不能为空或每页大小不能为0");
        }
        QueryWrapper<Label> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("run", 1);
        Page<Label> page = new Page<>();
        page.setCurrent(pageCurrent);
        page.setSize(pageSize);
        IPage<Label> labelPage = labelService.page(page, queryWrapper);
        for (Label label : labelPage.getRecords()){
            Integer labelId = label.getId();
            Integer countTask = taskLabelService.countTaskByLabelId(labelId);
            label.setTaskCount(countTask);
        }
        return RestResult.success(labelPage);
    }



}
