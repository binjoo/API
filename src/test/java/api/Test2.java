package api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class Test2 {
    public static void main(String[] args) {
        String path = "E:\\imxingzhe";
        File pathFile = new File(path + "\\json");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            WritableWorkbook workbook = Workbook.createWorkbook(new File(path + "\\imxingzhe.xls"));
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

            File[] files = pathFile.listFiles();
            int hang = 1;
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                FileInputStream fileInputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuffer sb = new StringBuffer();
                String text = null;
                while ((text = bufferedReader.readLine()) != null) {
                    sb.append(text);
                }
                String content = sb.toString();

                JSONObject obj = JSON.parseObject(content);
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
            }
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
