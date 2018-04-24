package api;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.binjoo.base.utils.OkHttpManager;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import okhttp3.OkHttpClient;

public class Test {
    public static void main(String[] args) {
        // Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);
        String url = "http://www.imxingzhe.com/api/v4/insure/?order_id=%s";
        String p_path = "E:\\imxingzhe\\";
        File xlsFile = new File(p_path + "xingzhe.xls");
        if (!xlsFile.getParentFile().exists()) {
            xlsFile.getParentFile().mkdirs();
        }
        int start = 30, end = 2500;
        int succeed = 0, fail = 0;
        System.out.println(xlsFile.getPath());
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(xlsFile);
            WritableSheet sheet = workbook.createSheet("sheet1", 0);
            Label labelA = new Label(0, 0, "编号"); // id
            Label labelB = new Label(1, 0, "用户"); // user
            Label labelC = new Label(2, 0, "姓名"); // name
            Label labelD = new Label(3, 0, "身份证"); // certificate_no
            Label labelE = new Label(4, 0, "国家代码"); // phone_prefix
            Label labelF = new Label(5, 0, "手机"); // phone
            Label labelG = new Label(6, 0, "邮箱"); // email
            Label labelH = new Label(7, 0, "保单号"); // policy_no
            Label labelI = new Label(8, 0, "保单费用"); // money_actual
            Label labelJ = new Label(9, 0, "保单开始时间"); // date_begin
            Label labelK = new Label(10, 0, "保单结束时间"); // date_end
            sheet.addCell(labelA);
            sheet.addCell(labelB);
            sheet.addCell(labelC);
            sheet.addCell(labelD);
            sheet.addCell(labelE);
            sheet.addCell(labelF);
            sheet.addCell(labelG);
            sheet.addCell(labelH);
            sheet.addCell(labelI);
            sheet.addCell(labelJ);
            sheet.addCell(labelK);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            int hang = 1;
            for (int i = start; i < end; i++) {
                File json = new File(p_path + "json\\" + i + ".json");
                if (json.exists()) {
                    continue;
                }
                json.getParentFile().mkdirs();
                System.out.print(String.format("正在进行第%s个采集...", i));
                OkHttpManager manage = new OkHttpManager();
                manage.get(String.format(url, i));
                manage.addHeader("Cookie", "sessionid=e83n01jbpekdf5qn8dg7tic4rs9myo2t");
                String result = manage.execute().body().string();
                if (manage.execute().code() == 200) {
                    JSONObject obj = JSON.parseObject(result);
                    JSONObject data = obj.getJSONObject("data");
                    JSONObject insurance_data = data.getJSONObject("insurance_data");
                    String id = insurance_data.getString("id");
                    String user = insurance_data.getString("user");
                    String name = insurance_data.getString("name");
                    String certificate_no = insurance_data.getString("certificate_no");
                    String phone_prefix = insurance_data.getString("phone_prefix");
                    String phone = insurance_data.getString("phone");
                    String email = insurance_data.getString("email");
                    String policy_no = insurance_data.getString("policy_no");
                    String money_actual = insurance_data.getString("money_actual");
                    String date_begin = insurance_data.getString("date_begin");
                    String date_end = insurance_data.getString("date_end");

                    sheet.addCell(new Label(0, hang, id));
                    sheet.addCell(new Label(1, hang, user));
                    sheet.addCell(new Label(2, hang, name));
                    sheet.addCell(new Label(3, hang, certificate_no));
                    sheet.addCell(new Label(4, hang, phone_prefix));
                    sheet.addCell(new Label(5, hang, phone));
                    sheet.addCell(new Label(6, hang, email));
                    sheet.addCell(new Label(7, hang, policy_no));
                    sheet.addCell(new Label(8, hang, money_actual));
                    sheet.addCell(new Label(9, hang, sdf.format(new Date(Long.valueOf(date_begin)))));
                    sheet.addCell(new Label(10, hang, sdf.format(new Date(Long.valueOf(date_end)))));

                    hang++;

//                    File json = new File(p_path + "json\\" + id + ".json");
//                    if (!json.getParentFile().exists()) {
//                        json.getParentFile().mkdirs();
//                    }
                    json.createNewFile();
                    FileWriter fw = new FileWriter(json, false);
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(result);
                    bw.close();
                    fw.close();
                    System.out.println("数据采集成功...");
                    succeed++;
                } else {
                    System.out.println("数据不存在，采集失败...");
                    fail++;
                }
                manage.execute().close();
                manage.dispatcher();
            }
            try {
                workbook.write();
            } catch (Exception e) {
                e.printStackTrace();
            }
            workbook.close();
            System.err.println("一共采集 " + (end - start) + " 条，成功 " + succeed + " 条，失败 " + fail + " 条。");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
}
