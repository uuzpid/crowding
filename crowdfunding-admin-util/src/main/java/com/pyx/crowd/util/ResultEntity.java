package com.pyx.crowd.util;

public class ResultEntity<T> {

    public static final String SUCCESS="SUCCESS";
    public static final String FAILED="FAILED";
    private String result;//封装当前请求处理结果是成功还是失败
    private String message;//封装错误信息
    private T data;//要返回的数据

    //请求处理成功且不需要返回数据
    public static <E> ResultEntity<E> successWithoutData(){
        return new ResultEntity<E>(SUCCESS,null,null);
    }

    //请求处理成功且需要返回数据
    public static <E> ResultEntity<E> successWithData(E data){
        return new ResultEntity<E>(SUCCESS,null,data);
    }

    //请求处理失败
    public static <E> ResultEntity<E> failed(String message){
        return new ResultEntity<E>(FAILED,message,null);
    }

    @Override
    public String toString() {
        return "ResultEntity{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResultEntity() {
    }

    public ResultEntity(String result, String message, T data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }
}
