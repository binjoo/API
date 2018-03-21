package com.binjoo.api;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.binjoo.action.BaseAction;
import com.binjoo.base.utils.OkHttpManager;
import com.binjoo.base.utils.ParaMap;

public class DoubanAction extends BaseAction {
    private final static String DOUBAN_API_URL = "https://frodo.douban.com/api/v2/user/%s/interests";
    private final static String DOUBAN_API_CODE = "0dad551ec0f84ed02907ff5c42e8ec70";

    /*
     * 电影
     */
    public void movie(ParaMap inMap) throws Exception {
        this.resultJson("movie", inMap);
    }

    /*
     * 音乐
     */
    public void music(ParaMap inMap) throws Exception {
        this.resultJson("music", inMap);
    }

    /*
     * 图书
     */
    public void book(ParaMap inMap) throws Exception {
        this.resultJson("book", inMap);
    }

    /*
     * 游戏
     */
    public void game(ParaMap inMap) throws Exception {
        this.resultJson("game", inMap);
    }

    /*
     * 应用
     */
    public void app(ParaMap inMap) throws Exception {
        this.resultJson("app", inMap);
    }

    /**
     * 发起请求
     * 
     * @param uid
     *            用户豆瓣ID
     * @param type
     *            资源类型
     * @param status
     *            状态（想看、看完、在看）
     * @param count
     *            返回数量
     * @param start
     *            从哪开始
     * @return
     * @throws Exception
     */
    public JSONObject httpGet(String uid, String type, String status, String count, String start) throws Exception {
        if (StringUtils.isEmpty(status)) {
            status = "";
        }
        String url = String.format(DOUBAN_API_URL, uid);
        url = String.format("%s?apikey=%s&type=%s&status=", url, DOUBAN_API_CODE, type, status);
        OkHttpManager manage = new OkHttpManager();
        manage.get(url);
        manage.addHeader("User-Agent", "com.douban.frodo/5.20.1(126)");
        String result = manage.execute().body().string();
        return JSON.parseObject(result);
    }

    private void resultJson(String type, ParaMap inMap) throws Exception {
        boolean power = this.getUser().getBoolean("douban_module");
        String douban_id = this.getUser().getString("douban_id");

        if (!power) {
            throw new Exception("没有权限获取douban数据。");
        }

        ParaMap result = new ParaMap();
        String count = inMap.getString("count", "10");
        String start = inMap.getString("start", "0");
        String status = inMap.getString("status", "");

        JSONObject resObj = this.httpGet(douban_id, type, status, count, start);

        result.put("errcode", 0);
        result.put("count", resObj.getIntValue("count"));
        result.put("start", resObj.getIntValue("start"));
        result.put("total", resObj.getIntValue("total"));
        List<ParaMap> dataList = new ArrayList<ParaMap>();
        JSONArray array = resObj.getJSONArray("interests");
        for (int i = 0; i < array.size(); i++) {
            ParaMap item = new ParaMap();
            JSONObject row = (JSONObject) array.get(i);
            JSONObject subject = row.getJSONObject("subject");
            // 唯一ID
            item.put("id", subject.getString("id"));
            // 标题
            item.put("title", subject.getString("title"));
            JSONObject pic = subject.getJSONObject("pic");
            // 封面
            item.put("cover", pic.getString("normal"));
            // 豆瓣地址
            item.put("url", subject.getString("url"));
            // 评分
            JSONObject rating = subject.getJSONObject("rating");
            double rate = rating.getDoubleValue("value");
            item.put("rating", new BigDecimal(rate).setScale(1, RoundingMode.HALF_UP).doubleValue());
            // 星级
            item.put("star", rating.getString("star_count"));
            // 类型
            String stype = subject.getString("type");
            item.put("type", "tv".equals(stype) ? "movie" : stype);
            // 状态
            item.put("status", row.getString("status"));
            dataList.add(item);
        }
        result.put("data", dataList);

        renderJSON(result);
    }

    @Override
    public ParaMap index(ParaMap inMap) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
}
