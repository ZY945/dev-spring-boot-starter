package io.github.zy945.util.commonUtil;


import java.security.MessageDigest;
import java.util.*;
import java.util.random.RandomGenerator;

/**
 * 字符串工具栏
 * 功能
 * 1.匹配句子中的词
 * 2.匹配文件后缀、路径等
 *
 * @author 伍六七
 * @date 2023/5/17 15:15
 */
public class StringUtil {


    private static char[] CHARS = new char[]{
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };


    //TODO           语句
    //目前是匹配字符(文字和字母)，遇到符号就停止，
    // 例如:   城市(居住地)
    // 这种会拆分为 [城市,居住地]
    // 最终希望这种可以不拆分,添加条件跳过即可,但是会有特殊的情况,

    /**
     * 统计不同的词
     *
     * @param content 句子
     * @return LinkedHashSet 有序的不重复
     * isLetter() 方法用于判断指定字符是否为字母。
     */
    public static Set<String> getDistinctWordSet(String content, Boolean isOrder) {
        Set<String> set = null;
        StringBuilder wordBuilder = new StringBuilder();
        //保证最后一个字符可以停止
        if (!(content.charAt(content.length() - 1) == '.')) {
            content += '.';
        }
        //是否排序
        if (isOrder) {
            set = new LinkedHashSet<>();
        } else {
            set = new HashSet<>();
        }

        for (char c : content.toCharArray()) {
            if (Character.isLetter(c)) {//为字符
                wordBuilder.append(c);
            } else if (wordBuilder.length() > 0) {
                String word = wordBuilder.toString();
                set.add(word);
                wordBuilder = new StringBuilder();
            }
        }
        return set;
    }

    public static List<String> getWordList(String content) {
        List<String> list = new ArrayList<>();
        StringBuilder wordBuilder = new StringBuilder();
        //保证最后一个字符可以停止
        if (!(content.charAt(content.length() - 1) == '.')) {
            content += '.';
        }

        for (char c : content.toCharArray()) {
            if (Character.isLetter(c)) {//为字符
                wordBuilder.append(c);
            } else if (wordBuilder.length() > 0) {
                String word = wordBuilder.toString();
                list.add(word);
                wordBuilder = new StringBuilder();
            }
        }
        return list;
    }


    public static Map<String, Integer> getWordMap(String content, Boolean isOrder) {
        Map<String, Integer> map = null;
        StringBuilder wordBuilder = new StringBuilder();
        //保证最后一个字符可以停止
        if (!(content.charAt(content.length() - 1) == '.')) {
            content += '.';
        }
        if (isOrder) {
            map = new LinkedHashMap<>();
        } else {
            map = new HashMap<>();
        }
        for (char c : content.toCharArray()) {
            if (Character.isLetter(c)) {//为字符
                wordBuilder.append(c);
            } else if (wordBuilder.length() > 0) {
                String word = wordBuilder.toString();
                if (map.containsKey(word)) {
                    map.put(word, map.get(word) + 1);
                } else {
                    map.put(word, 1);
                }
                wordBuilder = new StringBuilder();
            }
        }
        return map;
    }

    public static List<String> getWord(String content) {
        //保证最后一个字符可以停止
        if (!(content.charAt(content.length() - 1) == '.')) {
            content += '.';
        }
        List<String> list = new ArrayList<>();

        String ans = "";
        StringBuilder word = new StringBuilder();
        for (char c : content.toCharArray()) {
            if (Character.isLetter(c)) {//为字符
                word.append(c);
            } else if (word.length() > 0) {
                String finalword = word.toString();
                list.add(finalword);
                word = new StringBuilder();
            }
        }
        return list;
    }


    //TODO           单个词

    /**
     * 获取后缀
     *
     * @param path 文件路径
     * @return 后缀(类似.java)
     */
    public static String getFileSuffix(String path) {
        return path.contains(".") ? path.substring(path.indexOf(".")) : null;
    }

    /**
     * 判断是否是指定后缀文件
     *
     * @param path   文件路径
     * @param suffix 后缀
     * @return
     */
    public static Boolean isFileBySuffix(String path, String suffix) {
        return path.contains("." + suffix);
    }


    /**
     * 用get方法获取数据，首字母大写，如getName()
     */
    public static String captureName(String name) {
        char[] cs = name.toCharArray();
        //ascii 码表 ，如 n=110，N=78
        cs[0] -= 32;
        return String.valueOf(cs);
    }

    /**
     * KMP 算法--搜索字串--.indexOf<br/>
     * next[]是匹配串的前缀表<br/>
     *
     * @param ss 原串(string)
     * @param pp 匹配串(pattern)
     * @return
     */
    public static int KMP(String ss, String pp) {
        if (pp.isEmpty()) return 0;

        // 分别读取原串和匹配串的长度
        int n = ss.length(), m = pp.length();
        // 原串和匹配串前面都加空格，使其下标从 1 开始
        ss = " " + ss;
        pp = " " + pp;

        char[] s = ss.toCharArray();
        char[] p = pp.toCharArray();
        //0,0,0,1,0,0
        // 构建 next 数组，数组长度为匹配串的长度（next 数组是和匹配串相关的）
        int[] next = new int[m + 1];
        // 构造过程 i = 2，j = 0 开始，i 小于等于匹配串长度 【构造 i 从 2 开始】
        for (int i = 2, j = 0; i <= m; i++) {
            // 匹配不成功的话，j = next(j)
            while (j > 0 && p[i] != p[j + 1]) j = next[j];
            // 匹配成功的话，先让 j++
            if (p[i] == p[j + 1]) j++;
            // 更新 next[i]，结束本次循环，i++
            next[i] = j;
        }
        // 匹配过程，i = 1，j = 0 开始，i 小于等于原串长度 【匹配 i 从 1 开始】
        for (int i = 1, j = 0; i <= n; i++) {
            // 匹配不成功 j = next(j)
            while (j > 0 && s[i] != p[j + 1]) j = next[j];
            // 匹配成功的话，先让 j++，结束本次循环后 i++
            if (s[i] == p[j + 1]) j++;
            // 整一段匹配成功，直接返回下标
            if (j == m) return i - m;
        }

        return -1;
    }

    /**
     * 名称:字母异位词分组<br/>
     * 用处:先后顺序不影响结果,视为一种方案<br/>
     * 输入: strs = ["eat", "tea", "tan", "ate", "nat", "bat"]<br/>
     * 输出: [["bat"],["nat","tan"],["ate","eat","tea"]]<br/>
     *
     * @param strs
     * @return
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        // HashMap和ArrayList应用题，排序+哈希表
        // map<单词排序后的key, 同一key的的单词集合>，遍历一次strs即可，不过每个单词仍不可避免要遍历每个字符，  O(n*maxlen*logmaxlen)
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        for (String str : strs) {
            // 将每个单词转化为字符数组，排序后得到该单词的key
            char[] chr = str.toCharArray();

            Arrays.sort(chr);
            String key = new String(chr);
            // map的key对应value存储key相同的原始单词列表list，取出list，若没有当前单词的key则创建空list
            List<String> list = map.getOrDefault(key, new ArrayList());
            // 将当前单词加入list，并将key和更新后的list压入map
            list.add(str);
            map.put(key, list);
        }
        // 遍历完成后，map中已经存储了所有单词，获取map中所有value的list集合（相当于按key分好list组），构建List<list>
        return new ArrayList<List<String>>(map.values());
    }


    // TODO           功能型

    /**
     * 直接md5，唯一性不是很好
     *
     * @param url
     * @return
     */
    @Deprecated()
    public static String[] shortUrls(String url) {
        // 可以自定义生成 MD5 加密字符传前的混合 KEY
        String key = "test";
        // 要使用生成 URL 的字符

        // 对传入网址进行 MD5 加密
        String hex = md5ByHex(key + url);

        String[] resUrl = new String[4];
        for (int i = 0; i < 4; i++) {

            // 把加密字符按照 8 位一组 16 进制与 0x3FFFFFFF 进行位与运算
            String sTempSubString = hex.substring(i * 8, i * 8 + 8);

            // 这里需要使用 long 型来转换，因为 Inteper .parseInt() 只能处理 31 位 , 首位为符号位 , 如果不用long ，则会越界
            long lHexLong = 0x3FFFFFFF & Long.parseLong(sTempSubString, 16);
            String outChars = "";
            for (int j = 0; j < 6; j++) {
                // 把得到的值与 0x0000003D 进行位与运算，取得字符数组 chars 索引
                long index = 0x0000003D & lHexLong;
                // 把取得的字符相加
                outChars += CHARS[(int) index];
                // 每次循环按位右移 5 位
                lHexLong = lHexLong >> 5;
            }
            // 把字符串存入对应索引的输出数组
            resUrl[i] = outChars;
        }
        return resUrl;
    }


    public static int getRandomInt() {
        RandomGenerator aDefault = RandomGenerator.getDefault();
        return aDefault.nextInt();
    }


//    /**
//     * 使用hutool的MurmurHash加密长连接,然后对chars进行取余
//     *
//     * @param longUrl
//     * @return
//     */
//    public static String shortUrl(String longUrl) {
//        StringBuilder builder = new StringBuilder();
//        //伪随机数,其实我也不懂真随机和一一对应的哪个更好
//        int hash32 = MurmurHash.hash32(longUrl);
//        //用long是统一正数
//        long num = hash32 < 0 ? Integer.MAX_VALUE - (long) hash32 : hash32;
//        while (num > 0) {
//
//            long index = 0x0000003D & num;
//            char ch = CHARS[(int) index];
//            builder.append(ch);
//            num /= 0x0000003D;
//        }
//        return builder.toString();
//    }

    /**
     * MD5加密(32位大写)
     *
     * @param src
     * @return
     */
    public static String md5ByHex(String src) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] b = src.getBytes();
            md.reset();
            md.update(b);
            byte[] hash = md.digest();
            String hs = "";
            String stmp = "";
            for (int i = 0; i < hash.length; i++) {
                stmp = Integer.toHexString(hash[i] & 0xFF);
                if (stmp.length() == 1)
                    hs = hs + "0" + stmp;
                else {
                    hs = hs + stmp;
                }
            }
            return hs.toUpperCase();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 随机生成验证码
     *
     * @param n
     * @return
     */
    public static String creatCode(int n) {
        //3、定义一个字符串变量记录生成的随机字符
        String code = "";
        Random r = new Random();
        //2、定义一个for循环，循环n次，依次生成随机字符
        for (int i = 0; i < n; i++) {
            //i=0 1 2
            //3、生成一个随机字符，英文大、小写 数字（0 1 2 ）
            int type = r.nextInt(3);//0 1 2
            switch (type) {
                case 0:
                    //大写字符（A 65-Z 65+25）
                    char ch = (char) (r.nextInt(26) + 65);
                    code += ch;
                    break;
                case 1:
                    //小写字符（a 97-z 97+25）
                    char ch1 = (char) (r.nextInt(26) + 97);
                    code += ch1;
                    break;
                case 2:
                    //数字字符
                    code += r.nextInt(10);//0-9
                    break;
            }
        }
        return code;
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
