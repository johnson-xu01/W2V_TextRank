public class Test02 {
    public static void main(String[] args) {
        String s = "   what are you doing \t\r ";
        System.out.println(s);
        String s1 = s.replaceAll("\n", "");
        String trim = s.trim();
        System.out.println(s.trim());
    }
}
