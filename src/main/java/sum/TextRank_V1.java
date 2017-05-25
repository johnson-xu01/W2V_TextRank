package sum;

import java.io.*;
import java.util.Iterator;

public class TextRank_V1 {
    public Doc myDoc = new Doc();
    public double[][] similarity;

    public void Summarize(String args[]) throws IOException {
        /* Read files */
        String[] singleFile = new String[1];
        singleFile[0] = args[0];
        myDoc.maxlen = Integer.parseInt(args[4]);
        myDoc.readfile(singleFile, " ", args[2], args[6]);
        /* Calculate similarity matrix of sentences */
        myDoc.calcTfidf(Integer.parseInt(args[3]), Integer.parseInt(args[5]));
        myDoc.calcSim();
        similarity = new double[myDoc.snum][myDoc.snum];
        for (int i = 0; i < myDoc.snum; ++i) {
            double sumISim = 0.0;
            for (int j = 0; j < myDoc.snum; ++j) {
                if (i == j) similarity[i][j] = 0.0;
                else {
                    int tmpNum = 0;
                    for (Iterator<Integer> iter = myDoc.sVector.get(i).iterator(); iter.hasNext(); ) {
                        int now = iter.next();
                        if (myDoc.sVector.get(j).contains(now)) {
                            tmpNum++;
                        }
                    }
                    similarity[i][j] = tmpNum / (Math.log(1.0 * myDoc.senLen.get(i)) + Math.log(1.0 * myDoc.senLen.get(j)));
                }
                sumISim += similarity[i][j];
            }

    		/* Normalization the similarity matrix by row */
            for (int j = 0; j < myDoc.snum; ++j) {
                if (sumISim == 0.0) {
                    similarity[i][j] = 0.0;
                } else {
                    similarity[i][j] = similarity[i][j] / sumISim;
                }
            }
        }

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
        /* Output the abstract */
        try {
            File outfile = new File(args[1]);
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(outfile), "utf-8");
            BufferedWriter writer = new BufferedWriter(write);
            String res = "";
            if (myDoc.summaryId.get(0) < 0) {
                res = myDoc.article.trim();
            } else {
                for (int i : myDoc.summaryId) {
                    res += myDoc.originalSen.get(i).trim();
                }
            }
            writer.write(res);
            writer.close();
        } catch (Exception e) {
            System.out.println("There are errors in the output.");
            e.printStackTrace();
        }
    }
}