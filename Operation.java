import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class Operation {

    public static void main(String[] args) {
//        第一步：先将 6+4*2+8/4 转成一个集合 ["6","+","4","*","2","+","8","/","4"];
//        List list = Character("6+4.5*2+8/4");
//        list.stream().forEach(System.out::println);
//        第二步：把集合中的元素进行数学运算，运算逻辑可查看calculate方法注释
//        String result = calculate(list);
//        System.out.println(result);
        // 有加减乘除和括号嵌套的表达式计算方法
        String result = arithmetic("1+1*((-2)*(-2))");
        System.out.println(result);
    }

    public static String arithmetic(String str) {
        // 检查表达式是否正确
        checkParam(str);
        // 表达式中负数转换
        str = negative(str);
        // 判断字符串表达式是否包含有括号
        if (isBracket(str)) {
            int startFalg = 0;
            int endFalg = 0;
            for (int i = 0; i < str.length(); i++) {
                if (!isBracket(str)) {
                    break;
                }
                if (str.charAt(i) == '(') {
                    startFalg = i;
                }
                // 获取最里面一层括号内的表达式
                if (str.charAt(i) == ')') {
                    endFalg = i;
                    String subStr = str.substring(startFalg + 1, endFalg);
                    String result = calculate(Character(subStr));
                    // 如果最里面一层括号内的计算结果返回值为负数，则需要判断括号前面的符号：例如：6+(4-2*3),其中4-2*3=-2，最终表达式拼接为 6-2
                    if (result.charAt(0) == '-') {
                        if (startFalg == 0) {
                            str = "1-1" + result + str.substring(endFalg + 1);
                            break;
                        }
                        char mark = str.charAt(startFalg - 1);
                        if (mark == '-') {
                            str = str.substring(0, startFalg - 1) + "+" + result.substring(1) + str.substring(endFalg + 1);
                        } else {
                            str = str.substring(0, startFalg - 1) + result + str.substring(endFalg + 1);
                        }
                    }else {
                        str = str.substring(0, startFalg) + result + str.substring(endFalg + 1);
                    }
                    // 计算完最里面一层括号后，将for循环和括号的索引重新置为 0 ，重新循环判断字符中是否还有括号表达式
                    i = 0;
                    startFalg = 0;
                    endFalg = 0;
                }
            }
        }
        return calculate(Character(str));
    }

    /**
     * 判断表达式是否有括号
     * @param str
     * @return
     */
    public static boolean isBracket(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '(') {
                return true;
            }
        }
        return false;
    }

    /**
     * 表达式格式检查
     * @param str
     * @return
     */
    public static String checkParam(String str) {
        int a = 0;
        int b = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!(c >= 40 && c < 57)) {
                throw new RuntimeException("运算表达式有误！该字符不是数字或符号：" + String.valueOf(c));
            }
            if (c == 40) {
                a++;
            }else if (c == 41) {
                b++;
            }
        }
        if (a != b) {
            throw new RuntimeException("运算表达式缺失括号！");
        }
        return str;
    }

    /**
     * 表达式中负数字符串转化
     * @param str
     * @return
     */
    public static String negative(String str) {
        int a = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == 40) {
                for (int j = i; j < str.length(); j++) {
                    char c1 = str.charAt(j);
                    if (c1 == 41) {
                        a = j;
                        break;
                    }
                }
                String subStr = str.substring(i, a);
                boolean flag = true;
                for (int b = 1; b < subStr.length(); b++) {
                    char c2 = subStr.charAt(b);
                    if (!(c2 >= 48 && c2 < 57 || c2 == 45) || c2 == 46) {
                        flag = false;
                    }
                }
                if (flag) {
                    String s = subStr.substring(2);
                    str = str.substring(0,i) + "_" + s + str.substring(a+1);
                    i = 0;
                }
            }
        }
        return str;
    }

    /**
     * 将字符串表达式转成数组
     * @param str
     * @return
     */
    public static List Character(String str) {

        StringBuffer sb = new StringBuffer();
        List<String> list = new LinkedList<>();

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c >= 48 && c < 57 || c == 46 || c ==95) {
                if (c == 95) {
                    sb.append("-");
                } else {
                    sb.append(str.charAt(i));
                }
            }else {
                list.add(sb.toString());
                sb.delete(0,sb.length());
                list.add(String.valueOf(str.charAt(i)));
            }
        }
        list.add(sb.toString());
        return list;
    }

    /**
     * 数组表达式计算
     * @param list
     * @return
     */
    public static String calculate(List list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.size() == 1) {
                return (String) list.get(0);
            }
            // 先判断集合中是否有 乘法 或 除法
            if (list.get(i).equals("*") || list.get(i).equals("/")) {
                String num1 = (String)list.get(i-1);
                String mark = (String)list.get(i);
                String num2 = (String)list.get(i+1);
                String result = count(num1, num2, mark);
                list.set(i,result);
                list.remove(i-1);
                list.remove(i);
                // for循环索引重新置为0，重新遍历集合判断是否有 乘 除
                i = 0;
            }
        }
        // 上面for循环已经处理完了表达式中的 乘 除，下面计算 加 和 减
        for (int i = 0; i < list.size(); i++) {
            if (list.size() == 1) {
                return (String) list.get(0);
            }
            String num1 = (String)list.get(0);
            String mark = (String)list.get(1);
            String num2 = (String)list.get(2);
            String result = count(num1, num2, mark);
            list.set(1,result);
            list.remove(0);
            list.remove(1);
            i = 0;
        }
        // 将式子最终计算结果返回
        return (String) list.get(0);
    }

    /**
     *
     * @param num1 乘数
     * @param num2 被乘数
     * @param mark 算数符号(+ - * /)
     * @return
     */
    public static String count(String num1,String num2,String mark) {
        BigDecimal num01 = new BigDecimal(num1);
        BigDecimal num02 = new BigDecimal(num2);
        switch (mark){
            case "+":
                return num01.add(num02).toString();
            case "-":
                return num01.subtract(num02).toString();
            case "*":
                return num01.multiply(num02).toString();
            case "/":
                return num01.divide(num02,BigDecimal.ROUND_HALF_UP).toString();
        }
        return "0";
    }
}
