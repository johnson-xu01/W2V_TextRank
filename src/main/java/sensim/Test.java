package sensim;

import sensim.domain.WordEntry;

import java.util.Set;

public class Test {

    public static void main(String[] args) throws Exception {
        SimilarityHandler word2vec = SimilarityHandler.getInstance();
        word2vec.connect("/home/xuyiqiang/app/word2vec model/wiki_zh_word2vec.model");//加载word2vec模型
        String s1 = "你在干什么";
        String s2 = "干哈呢";
        float simi = word2vec.calsenSimilarity(s1, s2);

        System.out.println(simi);
        Set<WordEntry> words = word2vec.getSimilarWords("北京", 10);
        for (WordEntry we : words) {
            System.out.println(we);
        }

    }
}