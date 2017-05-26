import java.util.regex.Pattern;

public class Test04 {
    public static void main(String[] args) {
        String buffer = "2015-06-0?6<Paragraph>14:57<Paragraph>荆楚。网<Paragraph>显示图片东方之星被整体吊出水面<Paragraph>【最新消息】广州军区参谋长刘小午称，“东方之星”乘客三岁小女孩确认遇难，搜救官兵在船舱4楼前舱的一等舱内发现其遗体!hhh!ddd";
        String delimiters = "。|？|！|!|\\?|；|;";
        Pattern pattern = Pattern.compile(delimiters);
        String[] splits = pattern.split(buffer);

        for (String s : splits) {
            System.out.println(s);
        }
//
//        Matcher matcher = pattern.matcher(buffer);
//        while (matcher.find()) {//有问题!!!
//            System.out.println(matcher.group());
//        }
    }
}
