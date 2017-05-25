import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.ArrayList;

/**
 * 后处理
 */
public class Test05 {

    public static void main(String[] args) {
        String TWS_path = "/home/xuyiqiang/app/nlpcc2017textsummarization/textrank_V2_summs5000.txt";
        String OutData_path = "/home/xuyiqiang/app/nlpcc2017textsummarization/";
        BufferedReader reader = null;
        File fileIn = new File(TWS_path);
        String summs = "";
        try {
            FileInputStream in = new FileInputStream(fileIn);
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                String s = line.replaceAll("<Paragraph>", " ");
                summs += s.trim();
                summs += "\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        OutputData(OutData_path + "textrank_V2_summs5000_handled.txt", summs);

    }

    public static void OutputData(String outData_path, String article) {
        try {
            File outfile = new File(outData_path);
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(outfile), "utf-8");
            BufferedWriter writer = new BufferedWriter(write);
            writer.write(article);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
