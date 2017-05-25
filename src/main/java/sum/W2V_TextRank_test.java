package sum;

import java.io.*;

public class W2V_TextRank_test {
    public Doc myDoc = new Doc();
    public double[][] similarity;

    public String Summarize(String args[]) throws IOException {

    	/* Read files */
        String[] singleFile = new String[1];
        singleFile[0] = args[0];
        myDoc.maxlen = Integer.parseInt(args[4]);
        myDoc.readfile(singleFile, " ", args[2], args[6]);
        /* Calculate similarity matrix of sentences */
        /* use sensim based on word2vec improve similarity matrix of sentences	 */
        myDoc.calcTfidf(Integer.parseInt(args[3]), Integer.parseInt(args[5]));
        myDoc.calcSim_W2V();
        similarity = myDoc.normalSim;
        //Calculate the TextRank_V2 score of sentences
        double[] uOld = new double[myDoc.snum];
        double[] u = new double[myDoc.snum];
        for (int i = 0; i < myDoc.snum; ++i) {
            uOld[i] = 1.0;
            u[i] = 1.0;
        }

        double eps = 0.00001, alpha = 0.85, minus = 1.0;
        while (minus > eps) {
            uOld = u;
            for (int i = 0; i < myDoc.snum; i++) {
                double sumSim = 0.0;
                for (int j = 0; j < myDoc.snum; j++) {
                    if (j == i) continue;
                    else {
                        sumSim = sumSim + similarity[j][i] * uOld[j];
                    }

                }
                u[i] = alpha * sumSim + (1 - alpha);
            }
            minus = 0.0;
            for (int j = 0; j < myDoc.snum; j++) {
                double add = Math.abs(u[j] - uOld[j]);
                minus += add;
            }
        }
        myDoc.pickSentence(u);
        String res = "";
        if (myDoc.summaryId.get(0) < 0) {
            res = myDoc.article.trim();
        } else {
            for (int i : myDoc.summaryId) {
                res += myDoc.originalSen.get(i);
            }
        }
        return res;

    }
}