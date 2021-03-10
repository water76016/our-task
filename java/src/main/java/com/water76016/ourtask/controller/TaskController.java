package com.water76016.ourtask.controller;


import ch.qos.logback.core.pattern.util.RestrictedEscapeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.water76016.ourtask.common.RestResult;
import com.water76016.ourtask.common.ResultCode;
import com.water76016.ourtask.dto.SelectCondition;
import com.water76016.ourtask.dto.Statistics;
import com.water76016.ourtask.dto.TaskParam;
import com.water76016.ourtask.entity.Task;
import com.water76016.ourtask.entity.TaskLabel;
import com.water76016.ourtask.service.TaskLabelService;
import com.water76016.ourtask.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author github:water76016
 * @since 2020-07-21
 */
@Api(value = "清单控制", tags = {"清单操作"})
@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    TaskService taskService;
    @Autowired
    TaskLabelService taskLabelService;

    @ApiOperation("添加/更新一个清单")
    @PostMapping("/save")
    public RestResult saveOrUpdate(@RequestBody @ApiParam("清单controller传输对象") TaskParam taskParam){
        if (taskParam.getUserId() == null){
            return RestResult.errorParams("用户id不能为空");
        }
        if (taskParam.getCategoryId() == null){
            return RestResult.errorParams("分类id不能为空");
        }
        if (StrUtil.isEmpty(taskParam.getName())){
            return RestResult.errorParams("清单名不能为空");
        }
        if (StrUtil.isEmpty(taskParam.getDescription())){
            return RestResult.errorParams("清单描述不能为空");
        }
        Task task = Task.builder().id(taskParam.getId()).userId(taskParam.getUserId())
                .categoryId(taskParam.getCategoryId()).name(taskParam.getName())
                .description(taskParam.getDescription()).build();
        boolean flag = taskService.saveOrUpdate(task);
        if (flag == false){
            return RestResult.error();
        }
        Integer taskId = task.getId();
        //先删除之前的task和label对应关系
        QueryWrapper<TaskLabel> taskLabelQueryWrapper = new QueryWrapper<>();
        taskLabelQueryWrapper.eq("task_id", taskId);
        taskLabelService.remove(taskLabelQueryWrapper);
        //准备往task_label表里面插入新的task和label对应关系
        List<Integer> labelList = taskParam.getLabelList();
        for (Integer labelId : labelList){
            TaskLabel taskLabel = TaskLabel.builder().taskId(taskId).labelId(labelId).build();
            taskLabelService.save(taskLabel);
        }
        return RestResult.success();
    }

    @ApiOperation("逻辑删除/完成一个清单")
    @GetMapping("/delete/{id}")
    public RestResult deleteTaskById(@PathVariable("id") @ApiParam("清单id") Integer id){
        if (id == null){
            return RestResult.errorParams("用户id输入为空");
        }
        Task task = Task.builder().id(id).run(0).build();
        taskService.updateById(task);
        return RestResult.success();
    }

    @ApiOperation("查询当前用户的所有清单")
    @GetMapping("getAllList/{userId}")
    public RestResult getAllList(@PathVariable("userId") @ApiParam("用户id") Integer userId){
        if (userId == null){
            return RestResult.errorParams("用户id不能为空");
        }
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("run", 1);
        List<Task> taskList = taskService.list(queryWrapper);
        if (taskList == null || taskList.size() == 0){
            return RestResult.noContentSuccess();
        }
        List<TaskParam> taskParamList = new ArrayList<>();
        for (Task task : taskList){
            Integer taskId = task.getId();
            List<Integer> integerList = taskLabelService.getLableListByTaskId(taskId);
            TaskParam taskParam = new TaskParam();
            taskParam.setId(taskId);
            taskParam.setUserId(userId);
            taskParam.setName(task.getName());
            taskParam.setCategoryId(task.getCategoryId());
            taskParam.setLabelList(integerList);
            taskParam.setDescription(task.getDescription());
            taskParamList.add(taskParam);
        }
        return RestResult.success(taskParamList);
    }

    @ApiOperation("查询当前用户当前分类的所有未完成清单")
    @GetMapping("getAllList/{userId}/{categoryId}")
    public RestResult getTaskList(@PathVariable("userId") @ApiParam("用户id") Integer userId,
                                  @PathVariable("categoryId") @ApiParam("分类id") Integer categoryId){
        if (userId == null){
            return RestResult.errorParams("用户id不能为空");
        }
        if (categoryId == null){
            return RestResult.errorParams("分类id不能为空");
        }
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("category_id", categoryId);
        queryWrapper.eq("run", 1);
        List<Task> taskList = taskService.list(queryWrapper);
        if (taskList == null || taskList.size() == 0){
            return RestResult.noContentSuccess();
        }
        return RestResult.success(taskList);
    }

    @ApiOperation("查询当前用户，当前页的所有未完成的清单，也可根据条件进行查询")
    @PostMapping("/getPageList/{userId}/{pageCurrent}/{pageSize}")
    public RestResult getPageList(@PathVariable("userId") @ApiParam("用户id") Integer userId,
                                  @PathVariable("pageCurrent") @ApiParam("当前页") Integer pageCurrent,
                                  @PathVariable("pageSize") @ApiParam("每页大小") Integer pageSize,
                                  @RequestBody SelectCondition selectCondition){
        if (userId == null){
            return RestResult.errorParams("用户id不能为空");
        }
        if (pageCurrent == null || pageCurrent == 0){
            return RestResult.errorParams("当前页不能为空或当前页不能为0");
        }
        if (pageSize == null || pageSize == 0){
            return RestResult.errorParams("每页大小不能为空或每页大小不能为0");
        }
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("run", 1);
        String name = selectCondition.getName();
        Integer categoryId = selectCondition.getCategoryId();
        List<Integer> selectLabelIdList = selectCondition.getLabelIdList();
        if (StrUtil.hasEmpty(name) == false){
            queryWrapper.and(wrapper -> wrapper.like("name", name).or().like("description", name));
        }
        if (categoryId != null){
            queryWrapper.eq("category_id", categoryId);
        }
        if (selectLabelIdList.size() > 0){
            List<Integer> idList = taskLabelService.getTaskIdListByLabelIdList(selectLabelIdList);
            if (idList != null && idList.size() > 0){
                queryWrapper.in("id", idList);
            }
        }
        Page<Task> page = new Page<>();
        page.setCurrent(pageCurrent);
        page.setSize(pageSize);
        IPage<Task> taskPage = taskService.page(page, queryWrapper);
        for (Task task : taskPage.getRecords()){
            Integer taskId = task.getId();
            List<Integer> labelIdList = taskLabelService.getLableListByTaskId(taskId);
            task.setLabelList(labelIdList);
        }
        return RestResult.success(taskPage);
    }

    @ApiOperation("获取最近一周的清单完成情况")
    @GetMapping("/countTaskForDay/{userId}")
    public RestResult countTaskForDay(@PathVariable("userId") @ApiParam("用户id") Integer userId){
        if (userId == null){
            return RestResult.errorParams("用户id不能为空");
        }
        List<TreeMap<String, String>> result = taskService.countTaskForDay(userId);
        return RestResult.success(result);
    }

    @ApiOperation("获取最近七周的清单完成情况")
    @GetMapping("/countTaskForWeek/{userId}")
    public RestResult countTaskForWeek(@PathVariable("userId") @ApiParam("用户id") Integer userId){
        if (userId == null){
            return RestResult.errorParams("用户id不能为空");
        }
        List<TreeMap<String, String>> result = taskService.countTaskForWeek(userId);
        return RestResult.success(result);
    }

    @ApiOperation("获取用户清单的使用情况")
    @GetMapping("/getStatistics/{userId}")
    public RestResult getStatistics(@PathVariable("userId") @ApiParam("用户id") Integer userId){
        if (userId == null){
            return RestResult.errorParams("用户id不能为空");
        }
        Statistics statistics = taskService.getStatistics(userId);
        return RestResult.success(statistics);
    }

    @ApiOperation("查询用户在当天的每个分类清单的完成情况")
    @GetMapping("/countTodayForCategory/{userId}")
    public RestResult countTodayForCategory(@PathVariable("userId") @ApiParam("用户id") Integer userId){
        if (userId == null){
            return RestResult.errorParams("用户id不能为空");
        }
        List<Map<String, String>> result = taskService.countTodayForCategory(userId);
        return RestResult.success(result);
    }
}
