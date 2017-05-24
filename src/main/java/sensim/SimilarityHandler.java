package sensim;


import sensim.domain.WordEntry;
import sensim.utils.Tuple;
import sensim.utils.WordSegmentation;

import java.io.IOException;
import java.util.*;

public class SimilarityHandler {

    private Word2VEC vec = null;
    private boolean loadModelTF = false; //是否已经加载模型标志


    private static final SimilarityHandler single = new SimilarityHandler();

    public static SimilarityHandler getInstance() {

        return single;

    }

    private SimilarityHandler() {
    }

    public void connect() {
        try {
            connect("/wiki_chinese_word2vec(Google).model");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载Google版Word2Vec模型(C语言训练)
     *
     * @param modelPath:模型文件路径
     * @throws IOException
     */
    public void connect(String modelPath) {
        try {
            vec = new Word2VEC();
            vec.loadGoogleModel(modelPath);
            loadModelTF = true;
            System.out.println("SenSimilarity connect ............");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得词向量
     *
     * @param word
     * @return
     */
    public float[] getWordVector(String word) {
        if (loadModelTF == false) {
            return null;
        }
        return vec.getWordVector(word);
    }

    /**
     * 计算向量内积
     *
     * @param vec1
     * @param vec2
     * @return
     */
    private float calDist(float[] vec1, float[] vec2) {
        float dist = 0;
        for (int i = 0; i < vec1.length; i++) {
            dist += vec1[i] * vec2[i];
        }
        return dist;
    }

    /**
     * 计算词相似度
     *
     * @param word1
     * @param word2
     * @return
     */
    public float wordSimilarity(String word1, String word2) {
        if (loadModelTF == false) {
            return -1;
        }
        float[] word1Vec = getWordVector(word1);
        float[] word2Vec = getWordVector(word2);
        if (word1Vec == null || word2Vec == null) {
            return -1;
        }
        return calDist(word1Vec, word2Vec);
    }

    /**
     * 获取相似词语
     *
     * @param word
     * @param maxReturnNum
     * @return
     */
    public Set<WordEntry> getSimilarWords(String word, int maxReturnNum) {
        if (loadModelTF == false)
            return null;
        float[] center = getWordVector(word);
        if (center == null) {
            return Collections.emptySet();
        }
        int resultSize = vec.getWords() < maxReturnNum ? vec.getWords() : maxReturnNum;
        TreeSet<WordEntry> result = new TreeSet<WordEntry>();
        double min = Double.MIN_VALUE;
        for (Map.Entry<String, float[]> entry : vec.getWordMap().entrySet()) {
            float[] vector = entry.getValue();
            float dist = calDist(center, vector);
            if (result.size() <= resultSize) {
                result.add(new WordEntry(entry.getKey(), dist));
                min = result.last().score;
            } else {
                if (dist > min) {
                    result.add(new WordEntry(entry.getKey(), dist));
                    result.pollLast();
                    min = result.last().score;
                }
            }
        }
        result.pollFirst();
        return result;
    }

    /**
     * 计算词语与词语列表中所有词语的最大相似度
     * (最小返回0)
     *
     * @param centerWord 词语
     * @param wordList   词语列表
     * @return
     */
    private float calMaxSimilarity(String centerWord, List<String> wordList) {
        float max = 0; //最小返回0
        if (wordList.contains(centerWord)) {
            return 1;
        } else {
            for (String word : wordList) {
                float temp = wordSimilarity(centerWord, word);
                if (temp > max) {
                    max = temp;
                }
            }
        }
        return max;
    }

    /**
     * 计算句子相似度
     * 所有词语权值设为1
     *
     * @param sentence1Words 句子1词语列表
     * @param sentence2Words 句子2词语列表
     * @return 两个句子的相似度
     */
    public float sentenceSimilarity(List<String> sentence1Words, List<String> sentence2Words) {
        if (loadModelTF == false) {
            return -1;
        }
        if (sentence1Words.isEmpty() || sentence2Words.isEmpty()) {
            return -1;
        }
        float[] vector1 = new float[sentence1Words.size()];
        float[] vector2 = new float[sentence2Words.size()];
        for (int i = 0; i < vector1.length; i++) {
            vector1[i] = calMaxSimilarity(sentence1Words.get(i), sentence2Words);
        }
        for (int i = 0; i < vector2.length; i++) {
            vector2[i] = calMaxSimilarity(sentence2Words.get(i), sentence1Words);
        }
        float sum1 = 0;
        for (int i = 0; i < vector1.length; i++) {
            sum1 += vector1[i];
        }
        float sum2 = 0;
        for (int i = 0; i < vector2.length; i++) {
            sum2 += vector2[i];
        }
        return (sum1 + sum2) / (sentence1Words.size() + sentence2Words.size());
    }

    /**
     * 计算句子相似度(带权值)
     * 每一个词语都有一个对应的权值
     *
     * @param sentence1Words 句子1词语列表
     * @param sentence2Words 句子2词语列表
     * @param weightVector1  句子1权值向量
     * @param weightVector2  句子2权值向量
     * @return 两个句子的相似度
     * @throws Exception 词语列表和权值向量长度不同
     */
    public float sentenceSimilarity(List<String> sentence1Words, List<String> sentence2Words, float[] weightVector1, float[] weightVector2) throws Exception {
        if (loadModelTF == false) {
            return -1;
        }
        if (sentence1Words.isEmpty() || sentence2Words.isEmpty()) {
            return -1;
        }
        if (sentence1Words.size() != weightVector1.length || sentence2Words.size() != weightVector2.length) {
            throw new Exception("length of word list and weight vector is different");
        }
        float[] vector1 = new float[sentence1Words.size()];
        float[] vector2 = new float[sentence2Words.size()];
        for (int i = 0; i < vector1.length; i++) {
            vector1[i] = calMaxSimilarity(sentence1Words.get(i), sentence2Words);
        }
        for (int i = 0; i < vector2.length; i++) {
            vector2[i] = calMaxSimilarity(sentence2Words.get(i), sentence1Words);
        }
        float sum1 = 0;
        for (int i = 0; i < vector1.length; i++) {
            sum1 += vector1[i] * weightVector1[i];
        }
        float sum2 = 0;
        for (int i = 0; i < vector2.length; i++) {
            sum2 += vector2[i] * weightVector2[i];
        }
        float divide1 = 0;
        for (int i = 0; i < weightVector1.length; i++) {
            divide1 += weightVector1[i];
        }
        float divide2 = 0;
        for (int j = 0; j < weightVector2.length; j++) {
            divide2 += weightVector2[j];
        }
        return (sum1 + sum2) / (divide1 + divide2);
    }

    /**
     * @param s1
     * @param s2
     * @return
     */
    public float calsenSimilarity(String s1, String s2) {
        try {
            //分词，获取词语列表
            Tuple<List<String>, List<String>> wordsPOS1 = WordSegmentation.getWordsPOS(s1);
            Tuple<List<String>, List<String>> wordsPOS2 = WordSegmentation.getWordsPOS(s2);
            //句子相似度(名词、动词权值设为1，其他设为0.8)
            float[] weights1 = WordSegmentation.getPOSWeightArray(wordsPOS1.second);
            float[] weights2 = WordSegmentation.getPOSWeightArray(wordsPOS2.second);
            return sentenceSimilarity(wordsPOS1.first, wordsPOS2.first, weights1, weights2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }
}
