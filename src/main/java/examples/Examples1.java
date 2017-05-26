package examples;

import sum.W2V_TextRank;
import sensim.SimilarityHandler;

public class Examples1 {
    public static String type = "-1", topic = "-1", inputPath = "-1", outputFile = "-1";
    public static String language = "-1", abNum = "-1", method = "-1", stopwordPath = "-1";
    public static String stemmerOrNot = "1", ReMethod = "1", RePara = "0.7", beta = "0.1"/*, alpha = "0.85", eps = "0.00001"*/;
    public static String linkThresh = "0.1", AlphaC = "0.1", LambdaC = "0.8", op = "2", AlphaS = "0.5", LambdaS = "-1";
    public static String[] arg = new String[13];

    public static void main(String[] args) throws Exception {
        SimilarityHandler word2vec = SimilarityHandler.getInstance();
        word2vec.connect("/home/xuyiqiang/app/word2vec_model/wiki_zh_word2vec.model");//加载word2vec模型

        arg[0] = "/home/xuyiqiang/app/idea-IU-163.11103.6/workspace/W2V_TextRank/src/main/java/data/doc001.txt";//inputPath
        arg[1] = "/home/xuyiqiang/app/idea-IU-163.11103.6/workspace/W2V_TextRank/src/main/java/data/doc001_w2v.txt";//outputFile
        arg[2] = "1";//language
        arg[3] = "1";//type: single-document summarization
        arg[4] = "60";//abNum: the number of words in the final summary.
        arg[5] = stemmerOrNot;
        arg[6] = "n";//stopwordPath
        arg[7] = ReMethod;
        arg[8] = RePara;
        arg[9] = beta;
        W2V_TextRank w2V_textRank = new W2V_TextRank();
        w2V_textRank.Summarize(arg);
//        TextRank_V2 textrank = new TextRank_V2();
//        textrank.Summarize(arg);

    }
}
