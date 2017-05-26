# W2V_TextRank

## 简介
文本自动摘要算法：用Word2Vec改进的TextRank算法

## 结果对比：在5000个中文文本样本上的结果
![](/jpg/result.jpg)

## 评价指标
ROUGE1&2 ROUGE SU4 R&F[参考论文](http://www.aclweb.org/anthology/W04-1013)

## 使用说明
可以直接在命令行中运行编译好的jar包，jar包及训练好的Word2Vec模型可以在[这里下载](http://pan.baidu.com/s/1geJO8aJ) ,密码为：3cau
> java -jar W2V_TextRank.jar <参数>

## 参数说明
| 参数 | 说明 | 
|:---------------------|:--------|
| -m | 指定摘要算法：textrank / w2v_textrank,目前支持这两种方法。| 
| -n | 指定生成的摘要的最大字数| 
| -input | 指定输入文件路径（待摘要文本文件路径）|
| -output| 指定输出文件路径（生成的摘要文本文件路径） | 
| -w2v | 指定训练好的Word2Vec模型路径，可以在上面资源下载地址中下载已在中文维基百科上训练好Word2Vec模型文件，使用者也可以使用自己训练好的Word2Vec模型| 

## 示例
```
java -jar W2V_TextRank.jar –m w2v_textrank -n 60 –input ./article.txt –output ./summay.txt -w2v ./wiki_zh_word2vec.model
```
