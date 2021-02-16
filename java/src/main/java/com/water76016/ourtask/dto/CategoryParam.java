package com.water76016.ourtask.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: our-task
 * @description: 针对分类控制器传参所创建的传输对象
 * @author: water76016
 * @create: 2020-09-24 16:45
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryParam {
    @ApiModelProperty("分类id")
    Integer id;

    @ApiModelProperty("分类名称")
    String name;

    @ApiModelProperty("所属清单总数")
    Integer count;
}
