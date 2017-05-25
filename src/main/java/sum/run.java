package sum;

import sensim.SimilarityHandler;

import java.io.File;

public class run {
    public static String type = "1", inputPath = "-1", outputFile = "-1";
    public static String language = "1", abNum = "-1", method = "-1", stopwordPath = "n";
    public static String stemmerOrNot = "1", ReMethod = "1", RePara = "0.7", beta = "0.1"/*, alpha = "0.85", eps = "0.00001"*/;
    public static String W2VPath = "-1";
    public static String[] arg = new String[10];

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < args.length; ) {
            if (args[i].compareTo("-m") == 0) {
                method = args[++i];
                ++i;
            } else if (args[i].compareTo("-n") == 0) {
                abNum = args[++i];
                ++i;
            } else if (args[i].compareTo("-input") == 0) {
                inputPath = args[++i];
                ++i;
            } else if (args[i].compareTo("-output") == 0) {
                outputFile = args[++i];
                ++i;
            } else if (args[i].compareTo("-w2v") == 0) {
                W2VPath = args[++i];
                ++i;
            } else {
                System.out.println("Invalid parameter!");
                return;
            }
        }
        /**
         * 判断参数有效性
         * */
        if (method.equals("-1")) {
            System.out.println("Please choose the method you want to use.");
            return;
        } else if (abNum.equals("-1")) {
            System.out.println("Please input expected number of words in summary.");
            return;
        } else if (inputPath.equals("-1")) {
            System.out.println("Please input the path of input.");
            return;
        } else if (outputFile.equals("-1")) {
            System.out.println("Please input the path of output file.");
            return;
        } else if (W2VPath.equals("-1")) {
            System.out.println("Please input the path of Word2Vec model.");
            return;
        } else {
            if (!method.equals("textrank") && !method.equals("w2vtextrank")) {
                System.out.println("Specify method: \"textrank\" or \"w2vtextrank\", not \"" + method + "\".");
                return;
            }
            if (!isIntegerNumber(abNum) || abNum.contains("-")) {
                System.out.println("The expected number of words in summary should be an integer.");
                return;
            }
            File input = new File(inputPath);
            if (!input.exists()) {
                System.out.println("The path of input is not correct.");
                return;
            }
            File output = new File(outputFile);
            File outputDir = new File(output.getParent());
            if (!outputDir.exists()) {
                System.out.println("The directory of output file does not exist.");
                return;
            }
            if (method.equals("w2vtextrank")) {
                File word2vec = new File(W2VPath);
                if (!word2vec.exists()) {
                    System.out.println("The directory of Word2Vec model file does not exist.");
                    return;
                }
            }
        }
        arg[0] = inputPath;//inputPath
        arg[1] = outputFile;//outputFile
        arg[2] = language;//language
        arg[3] = type;//type: single-document summarization
        arg[4] = abNum;//abNum: the number of words in the final summary.
        arg[5] = stemmerOrNot;
        arg[6] = stopwordPath;//stopwordPath
        arg[7] = ReMethod;
        arg[8] = RePara;
        arg[9] = beta;

        switch (method) {
            case "textrank": {
                TextRank_V2 textrank = new TextRank_V2();
                textrank.Summarize(arg);
                break;
            }
            case "w2vtextrank": {
                SimilarityHandler word2vec = SimilarityHandler.getInstance();
                word2vec.connect(W2VPath);//加载word2vec模型
                W2V_TextRank w2V_textRank = new W2V_TextRank();
                w2V_textRank.Summarize(arg);
                break;
            }
            default:
                System.out.println("No this method!");
                break;
        }
    }

    public static boolean isIntegerNumber(String number) {
        number = number.trim();
        String intNumRegex = "\\-{0,1}\\d+";
        if (number.matches(intNumRegex))
            return true;
        else
            return false;
    }

    public static boolean isFloatPointNumber(String number) {
        number = number.trim();
        String pointPrefix = "(\\-|\\+){0,1}\\d*\\.\\d+";
        String pointSuffix = "(\\-|\\+){0,1}\\d+\\.";
        if (number.matches(pointPrefix) || number.matches(pointSuffix))
            return true;
        else
            return false;
    }

}
