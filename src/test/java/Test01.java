import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.ArrayList;

public class Test01 {
    public static String type = "-1", topic = "-1", inputPath = "-1", outputFile = "-1";
    public static String language = "-1", abNum = "-1", method = "-1", stopwordPath = "-1";
    public static String stemmerOrNot = "1", ReMethod = "1", RePara = "0.7", beta = "0.1"/*, alpha = "0.85", eps = "0.00001"*/;
    public static String linkThresh = "0.1", AlphaC = "0.1", LambdaC = "0.8", op = "2", AlphaS = "0.5", LambdaS = "-1";
    public static String[] arg = new String[13];

    public static void main(String[] args) {
        // {"summarization":"", "article": ""}
        String TWS_path = "/home/xuyiqiang/app/nlpcc2017textsummarization/train_with_summ.txt";
        String OutData_path = "/home/xuyiqiang/app/nlpcc2017textsummarization/";
        ArrayList<String> train_with_summ = Load_train_with_summ(TWS_path,50000);
        String summs = "";
//        int index = 0;
        for (String line : train_with_summ) {
            JSONObject JO = JSON.parseObject(line);
            String summarization = JO.getString("summarization");
            String s = summarization.replaceAll("\n", "");
            summs += s.trim();
            summs += "\n";
//            String article = JO.getString("article");
//            String file_path = OutData_path + index + ".txt";
//            OutputData(file_path, article);
//            index++;
        }
        OutputData(OutData_path + "true_summs.txt", summs);

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

    public static ArrayList<String> Load_train_with_summ(String TWS_path,int N) {
        ArrayList<String> train_with_summ = new ArrayList<>(N);
        BufferedReader reader = null;
        File fileIn = new File(TWS_path);
        try {
            FileInputStream in = new FileInputStream(fileIn);
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                train_with_summ.add(line);
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
        return train_with_summ;
    }
}
