package com.binjoo.action;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bson.Document;

import com.alibaba.fastjson.JSON;
import com.binjoo.base.utils.CharsetUtils;
import com.binjoo.base.utils.ParaMap;

@SuppressWarnings("unused")
public abstract class BaseAction {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private Map<String, Cookie> cookies;
    private Document user;

    public Document getUser() {
        return user;
    }

    public void setUser(Document user) {
        this.user = user;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public Map<String, Cookie> getCookies() {
        if (this.cookies == null) {
            Cookie[] cookies = this.request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    this.cookies.put(cookie.getName(), cookie);
                }
            }
        }
        return cookies;
    }

    public void setCookies(Map<String, Cookie> cookies) {
        this.cookies = cookies;
    }

    /**
     * 默认执行方法
     * 
     * @param inMap
     * @return
     * @throws Exception
     */
    public abstract ParaMap index(ParaMap inMap) throws Exception;

    // private void renderCode(int statusCode, String page) throws Exception {
    // response.setContentType("text/json; charset=" + CharsetUtils.UTF_8);
    // }

    // private void renderText() throws Exception {
    // }
    //
    // private void renderFile() throws Exception {
    // }

    protected void renderJSON(Object obj) throws Exception {
        this.response.setContentType("text/json; charset=" + CharsetUtils.UTF_8);
        response.getWriter().print(JSON.toJSONString(obj));
    }

    protected void renderPage() throws Exception {
        this.response.setContentType("text/json; charset=" + CharsetUtils.UTF_8);
    }
}
