package com.binjoo.base.utils;

import java.net.URLEncoder;
import java.util.HashMap;

public class URLBuilder {
    public static String httpBuildQuery(ParaMap params) throws Exception {
        StringBuffer sb = new StringBuffer();
        for (HashMap.Entry<String, Object> entry : params.entrySet()) {
            String name = String.valueOf(entry.getKey());
            String value = String.valueOf(entry.getValue());

            sb.append(URLEncoder.encode(name, "UTF-8"));
            sb.append("=");
            sb.append(URLEncoder.encode(value, "UTF-8"));
            sb.append("&");
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
