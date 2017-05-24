package sensim.utils;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

import java.util.ArrayList;
import java.util.List;

public class WordSegmentation {

    /**
     * 分词 NlpAnalysis
     *
     * @param sentence 待分词的句子
     * @return 分词结果
     */
    public static List<Term> Seg(String sentence) {
        return NlpAnalysis.parse(sentence).getTerms();
    }

    /**
     * 获取词语列表 和 词性列表
     *
     * @param sentence 待分词的句子
     * @return 分词后的词语列表
     */
    public static Tuple<List<String>, List<String>> getWordsPOS(String sentence) {
        List<Term> termList = Seg(sentence);
        List<String> wordList = new ArrayList<>();
        List<String> natureList = new ArrayList<>();
        for (Term wordTerm : termList) {
            wordList.add(wordTerm.getName());
            natureList.add(wordTerm.getNatureStr());
        }
        return new Tuple<>(wordList, natureList);
    }

    /**
     * 获取词性权值数组
     *
     * @param posList 词性列表
     * @return 词性列表对应的权值数组
     */
    public static float[] getPOSWeightArray(List<String> posList) {
        float[] weightVector = new float[posList.size()];
        for (int i = 0; i < weightVector.length; i++) {
            String POS = posList.get(i);
            switch (POS.charAt(0)) {
                case 'n':
                case 'v':
                    weightVector[i] = 1;
                    break;
                default:
                    weightVector[i] = (float) 0.8;
                    break;
            }
        }
        return weightVector;
    }
}
