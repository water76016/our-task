package com.water76016.ourtask.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author github:water76016
 * @since 2021-03-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TomatoSet implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 设置id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 每日目标数
     */
    private Integer aimNumber;

    /**
     * 是否开始目标
     */
    private Integer startAim;

    /**
     * 番茄时长
     */
    private Integer duration;

    /**
     * 短休息时长
     */
    private Integer shortRestDuration;

    /**
     * 长休息时长
     */
    private Integer longRestDuration;

    /**
     * 长休息间隔数
     */
    private Integer longRestIntervalNumber;

    /**
     * 是否自动开始下一个番茄
     */
    private Integer autoStartNext;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
