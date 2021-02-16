package com.water76016.ourtask.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @program: our-task
 * @description: 清单controller传输对象
 * @author: water76016
 * @create: 2020-09-24 16:45
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskParam {
    @ApiModelProperty("清单id")
    Integer id;

    @ApiModelProperty("用户id")
    Integer userId;

    @ApiModelProperty("分类id")
    Integer categoryId;

    @ApiModelProperty("清单名称")
    String name;

    @ApiModelProperty("所属标签列表")
    List<Integer> labelList;

    @ApiModelProperty("清单描述")
    String description;
}
