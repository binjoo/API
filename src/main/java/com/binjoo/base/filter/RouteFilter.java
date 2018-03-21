package com.binjoo.base.filter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.bson.Document;

import com.alibaba.fastjson.JSON;
import com.binjoo.action.BaseAction;
import com.binjoo.base.db.MongoDBUtils;
import com.binjoo.base.exception.RouteException;
import com.binjoo.base.utils.CharsetUtils;
import com.binjoo.base.utils.ParaMap;
import com.binjoo.base.utils.RequestUtils;
import com.binjoo.base.utils.RouteUtils;

public class RouteFilter implements Filter {
    private String contextPath = null;
    
    private final static HashMap<String, Object> actions = new HashMap<String, Object>();

    public void init(FilterConfig cfg) throws ServletException {
        this.contextPath = cfg.getServletContext().getContextPath();
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chi)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        request.setCharacterEncoding(CharsetUtils.UTF_8);
        response.setCharacterEncoding(CharsetUtils.UTF_8);
        ParaMap out = new ParaMap();
        out.put("errcode", 1);

        try {
            ParaMap params = RequestUtils.getParameters(request);
            Document user = this.validate(params.getString("appkey", ""));

            if (user == null) {
                throw new Exception("appkey错误或者无效。");
            }
            
            String uri = request.getRequestURI();
            String[] routes = null;
            if (uri.startsWith(this.contextPath)) {
                routes = StringUtils.split(uri.substring(this.contextPath.length()), "/");
            } else {
                routes = StringUtils.split(uri, "/");
            }

            if (routes.length < 1 || StringUtils.isBlank(routes[0])) {
                throw new RouteException(uri + " not found");
            }
            BaseAction action = (BaseAction) this.loadAction(routes[0], uri);
            action.setRequest(request);
            action.setResponse(response);
            action.setSession(request.getSession(true));
            action.setUser(user);

            Method method = null;

            if (routes.length >= 2 && StringUtils.isNotBlank(routes[1])) {
                method = action.getClass().getMethod(routes[1], ParaMap.class);
            } else {
                method = action.getClass().getMethod("index", ParaMap.class);
            }

            try {
                method.invoke(action, params);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                throw new Exception(e.getTargetException().getMessage());
            }
            out.put("errcode", 0);
        } catch (RouteException e) {
            e.printStackTrace();
            response.setStatus(404);
            out.put("errmsg", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            out.put("errmsg", e.getMessage());
        }
    }

    private Object loadAction(String actionName, String uri) throws Exception {
        Object action = actions.get(actionName);
        if (action == null) {
            String cls = "com.binjoo.api." + RouteUtils.replaceUnderlineAndFirstLetterToUpper(actionName) + "Action";
            try {
                action = Class.forName(cls).newInstance();
            } catch (ClassNotFoundException excp) {
                throw new RouteException(uri + " not found");
            }
            if (action != null && !actions.containsKey(actionName)) {
                synchronized (actions) {
                    actions.put(actionName, action);
                }
            }
        }
        return action;
    }

    private Document validate(String appkey) throws Exception {
        Document doc = new Document();
        doc.append("appkey", appkey);
        return MongoDBUtils.getItemOne("users", doc);
    }
}
