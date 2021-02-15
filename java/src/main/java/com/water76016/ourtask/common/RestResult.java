package com.water76016.ourtask.common;

import java.util.HashMap;

/**
 * @program: our-task
 * @description: 操作消费提醒
 * @author: water76016
 * @create: 2020-09-24 16:45
 **/
public class RestResult extends HashMap<String, Object>
{
    private static final long serialVersionUID = 1L;

    /** 状态码 */
    public static final String CODE_TAG = "code";

    /** 返回内容 */
    public static final String MSG_TAG = "msg";

    /** 数据对象 */
    public static final String DATA_TAG = "data";

    /**
     * 初始化一个新创建的 RestResult 对象，使其表示一个空消息。
     */
    public RestResult()
    {
    }

    /**
     * 初始化一个新创建的 RestResult 对象
     *
     * @param code 状态码
     * @param msg 返回内容
     */
    public RestResult(int code, String msg)
    {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    /**
     * 初始化一个新创建的 RestResult 对象
     *
     * @param code 状态码
     * @param msg 返回内容
     * @param data 数据对象
     */
    public RestResult(int code, String msg, Object data)
    {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (data != null)
        {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static RestResult success()
    {
        return RestResult.success("操作成功");
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static RestResult success(Object data)
    {
        return RestResult.success("操作成功", data);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static RestResult success(String msg)
    {
        return RestResult.success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static RestResult success(String msg, Object data)
    {
        return new RestResult(ResultCode.SUCCESS.getCode(), msg, data);
    }

    /**
     * 返回错误消息
     *
     * @return
     */
    public static RestResult error()
    {
        return RestResult.error("操作出错");
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static RestResult error(String msg)
    {
        return RestResult.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static RestResult error(String msg, Object data)
    {
        return new RestResult(ResultCode.ERROR.getCode(), msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg 返回内容
     * @return 警告消息
     */
    public static RestResult error(int code, String msg)
    {
        return new RestResult(code, msg, null);
    }

    /**
     * 请求参数错误
     * @param msg 错误消息
     * @return 警告消息
     */
    public static RestResult errorParams(String msg){
        return new RestResult(ResultCode.BAD_REQUEST.getCode(), msg, null);
    }

    /**
     *
     * @return 成功消息
     */
    public static RestResult noContentSuccess(){
        return new RestResult(ResultCode.NO_CONTENT.getCode(), "操作已经执行成功，但是没有返回数据", null);
    }

    public static RestResult notFindError(String message){
        return new RestResult(ResultCode.NOT_FOUND.getCode(), message, null);
    }


}
