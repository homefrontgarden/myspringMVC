package com.jsalpha.utils.servlet;

import java.lang.reflect.Method;
import java.util.List;

public class MethodInfo {
    private Object leader;
    private List<Object> params;
    private Method method;
    private String requestType;
    public MethodInfo() {
    }

    public MethodInfo(Object leader, List<Object> params, Method method, String requestType) {
        this.leader = leader;
        this.params = params;
        this.method = method;
        this.requestType = requestType;
    }

    public Object getLeader() {
        return leader;
    }

    public void setLeader(Object leader) {
        this.leader = leader;
    }

    public List<Object> getParams() {
        return params;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
}
