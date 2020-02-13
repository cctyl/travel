package cn.itcast.travel.domain;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * 通用的返回类
 */
public class Msg {

    //状态码 100失败    200成功
    private  Integer code;

    //提示消息
    private String msg;

    //封装的数据，我也不知道为什么放在Map中，但据说能保持统一性
    private Map<String,Object>  extend = new HashMap<>();

    public Map<String, Object> getExtend() {
        return extend;
    }

    public void setExtend(Map<String, Object> extend) {
        this.extend = extend;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }



    /**
     * 通用的返回成功信息的方法
     *
     */
    public static  Msg success(){
        Msg result = new Msg();
        result.setCode(200);
        result.setMsg("请求成功");
      /*  result.getExtend().put(key, value);*/
        return result;
    }

    /**
     * 通用的返回失败信息的方法
     *
     */
    public static  Msg fail(){
        Msg result = new Msg();
        result.setCode(100);
        result.setMsg("请求失败");
        return result;
    }

    /**
     * 封装数据的方法
     */
    public Msg add(String key,Object value){

        this.getExtend().put(key,value);
        return this;

    }

    /**
     * 用于替换原有的提示信息
     *
     * @return 返回自己
     */
    public  Msg  addMsg(String  text){
        this.setMsg(text);
        return this;
    }

    /**
     * 什么也不设置，返回一个空白的对象，自行添加数据
     * @return
     */
    public static  Msg object(){
        Msg result = new Msg();
        return result;
    }

    /**
     * 给消息设置code，和object方法共同使用
     * @param code
     * @return
     */
    public Msg addCode(int code){
        this.setCode(code);
        return this;
    }
}
