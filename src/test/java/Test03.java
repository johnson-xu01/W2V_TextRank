import sum.TextRank_V1_test;
import sum.TextRank_V2_test;

public class Test03 {
    public static String type = "-1", topic = "-1", inputPath = "-1", outputFile = "-1";
    public static String language = "-1", abNum = "-1", method = "-1", stopwordPath = "-1";
    public static String stemmerOrNot = "1", ReMethod = "1", RePara = "0.7", beta = "0.1"/*, alpha = "0.85", eps = "0.00001"*/;
    public static String linkThresh = "0.1", AlphaC = "0.1", LambdaC = "0.8", op = "2", AlphaS = "0.5", LambdaS = "-1";
    public static String[] arg = new String[10];

    public static void main(String[] args) {
//        SimilarityHandler word2vec = SimilarityHandler.getInstance();
//        word2vec.connect("/home/xuyiqiang/app/word2vec_model/wiki_zh_word2vec.model");//加载word2vec模型
        arg[1] = "-1";//outputFile: 未使用
        arg[2] = "1";//language
        arg[3] = "1";//type: single-document summarization
        arg[4] = "60";//abNum: the number of words in the final summary.
        arg[5] = stemmerOrNot;
        arg[6] = "n";//stopwordPath
        arg[7] = ReMethod;
        arg[8] = RePara;
        arg[9] = beta;
        String w2v_summs = "";
        int N = 5000;
        for (int i = 0; i < N; i++) {
            try {
                arg[0] = "/home/xuyiqiang/app/nlpcc2017textsummarization/train_datas/" + i + ".txt";//inputPath
                // W2V_TextRank_test w2V_textRank = new W2V_TextRank_test();
                // String summarize = w2V_textRank.Summarize(arg);
                TextRank_V2_test textrank = new TextRank_V2_test();
                String summarize = textrank.Summarize(arg);
                String s = summarize.replaceAll("\n", "");
                System.out.println("#####当前进度#####: " + i);
                String trim = s.trim();
                System.out.println(trim);
                w2v_summs += trim + "\n";
            } catch (Exception e) {
                System.out.println(i);
                e.printStackTrace();
            }
        }

        //Examples1.OutputData("/home/xuyiqiang/app/nlpcc2017textsummarization/w2v_summs5000.txt", w2v_summs);
        Test01.OutputData("/home/xuyiqiang/app/nlpcc2017textsummarization/textrank_V2_summs5000.txt", w2v_summs);
        System.out.println("########end#######");

    }
}
