package com.binjoo.api;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.binjoo.action.BaseAction;
import com.binjoo.base.utils.OkHttpManager;
import com.binjoo.base.utils.ParaMap;

public class XingzheAction extends BaseAction {
    private final static String XINGZHE_API_URL = "http://www.imxingzhe.com/api/v4/";
    private final static String XINGZHE_API_SESSION_ID = "sessionid=rc4l58z0t37qtaos18cy20711n6c9zgn";

    public JSONObject httpGet(String url) throws Exception {
        OkHttpManager manage = new OkHttpManager();
        manage.get(url);
        manage.addHeader("Cookie", XINGZHE_API_SESSION_ID);
        String result = manage.execute().body().string();
        return JSON.parseObject(result);
    }

    public void info(ParaMap inMap) throws Exception {
        String url = XINGZHE_API_URL + "account/get_user_info/";

        JSONObject resObj = this.httpGet(url);
        ParaMap result = new ParaMap();
        result.put("level", resObj.getIntValue("ulevel"));  //行者等级
        result.put("credits", resObj.getIntValue("credits"));   //行者积分
        result.put("main_team", resObj.getString("main_team")); //所属主俱乐部
        result.put("city", resObj.getString("city")); //所在城市
        result.put("total_distance", resObj.getIntValue("total_distance")); //总里程数（米）
        
        renderJSON(result);
    }

    public static void main(String[] args) {
        XingzheAction xingZheAction = new XingzheAction();
        try {
            xingZheAction.info(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public ParaMap index(ParaMap inMap) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

}
