package com.water76016.ourtask.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.water76016.ourtask.common.RestResult;
import com.water76016.ourtask.dto.CategoryParam;
import com.water76016.ourtask.entity.Category;
import com.water76016.ourtask.entity.Task;
import com.water76016.ourtask.service.CategoryService;
import com.water76016.ourtask.service.RedisService;
import com.water76016.ourtask.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author github:water76016
 * @since 2020-07-21
 */
@Api(value = "分类控制", tags = {"分类操作"})
@RestController
@RequestMapping("/category")
public class CategoryController {
    static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    TaskService taskService;
    @Autowired
    CategoryService categoryService;

    @ApiOperation("添加一个新的分类")
    @PostMapping("add")
    public RestResult add(@RequestBody @ApiParam("新增分类对象") Category category){
        if (category.getUserId() == null){
            return RestResult.errorParams("新增分类对象：传入用户id不能为空");
        }
        if (StrUtil.isEmpty(category.getName())){
            return RestResult.errorParams("新增分类对象：传入分类名不能为空");
        }
        boolean flag = categoryService.save(category);
        if (flag){
            return RestResult.success(category);
        }
        return RestResult.error();
    }

    @ApiOperation("逻辑删除一个分类")
    @GetMapping("delete/{id}/{userId}")
    public RestResult delete(@PathVariable("id") @ApiParam("分类id") Integer id,
                             @PathVariable("userId") @ApiParam("用户id") Integer userId){
        if (id == null){
            return RestResult.errorParams("分类id不能为空");
        }

        boolean flag = categoryService.removeById(id, userId);
        return flag ? RestResult.success() : RestResult.error("该分类下还有未完成的清单，不能删除");
    }

    @ApiOperation("修改分类的名称")
    @PostMapping("update")
    public RestResult update(@RequestBody @ApiParam("分类对象") Category category){
        if (category.getId() == null){
            return RestResult.errorParams("分类id不能为空");
        }
        if (StrUtil.isEmpty(category.getName())){
            return RestResult.errorParams("分类名不能为空");
        }
        boolean flag = categoryService.updateById(category);
        return flag ? RestResult.success() : RestResult.error();
    }

    @ApiOperation("查询当前用户的所有分类")
    @GetMapping("listAll/{userId}")
    public RestResult listAll(@PathVariable("userId") @ApiParam("用户id") Integer userId){
        if (userId == null){
            return RestResult.errorParams("用户id不能为空");
        }
        List<Map<String, Object>> categoryList = categoryService.list(userId);
        if (categoryList == null){
            return RestResult.noContentSuccess();
        }
        //根据分类列表,设置分类传输对象列表
        List<CategoryParam> categoryParamList = taskService.getCategoryParamList(categoryList);
        return RestResult.success(categoryParamList);
    }

    @ApiOperation("根据分类id查询分类对象")
    @GetMapping("get/{categoryId}")
    public RestResult getCategoryById(@PathVariable("categoryId") @ApiParam("分类id") String categoryId){
        if (StrUtil.isEmpty(categoryId)){
            return RestResult.errorParams("分类id不能为空");
        }
        Category result = categoryService.getById(Integer.valueOf(categoryId));
        return result != null ? RestResult.success(result) : RestResult.error();
    }

    @ApiOperation("查询当前用户，当前页的分类列表")
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
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("run", 1);
        Page<Category> page = new Page<>();
        page.setCurrent(pageCurrent);
        page.setSize(pageSize);
        IPage<Category> categoryPage = categoryService.page(page, queryWrapper);
        for (Category category : categoryPage.getRecords()){
            Integer categoryId = category.getId();
            Integer countTask = taskService.countTask(categoryId);
            category.setTaskCount(countTask);
        }
        return RestResult.success(categoryPage);
    }
}
