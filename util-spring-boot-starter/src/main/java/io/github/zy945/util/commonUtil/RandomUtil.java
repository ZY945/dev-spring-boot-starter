package io.github.zy945.util.commonUtil;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.UUID;

public final class RandomUtil {
    private static final int SHORT_MAX = 65536;
    private static int counter = -1;

    private RandomUtil() {
    }

    /**
     * Creates a unique 64 bits ID by aggregating the current time in
     * milliseconds since epoch (Jan. 1, 1970) and using a 16 bits counter. The
     * counter is initialized at a random number. This generator can create up
     * to 65536 different id per millisecond.
     *
     * @return a new id.
     */
    public static synchronized long nextId() {
        long now = System.currentTimeMillis();
        if (counter == -1) {
            long seed = now ^ Thread.currentThread().getId();
            Random rnd = new Random(Long.hashCode(seed));
            counter = rnd.nextInt(SHORT_MAX);
        }
        long id = (now << 16) | counter;
        counter = (counter + 1) % SHORT_MAX;
        return id;
    }

    /**
     * 生成唯一UUID
     *
     * @return
     */
    public static synchronized String getUUID() {
        String s = UUID.randomUUID().toString();
        return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
    }

    /**
     * 指定长度的数字
     *
     * @param length
     * @return
     */
    public static String getRandStr(Integer length) {

        String str = "";

        while (str.length() != length) {

            str = (Math.random() + "").substring(2, 2 + length);

        }

        return str;

    }


    /**
     * 获取指定长度文字
     *
     * @return String
     */
    public static String getRandomChar(Integer len) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            Random random = new Random();
            Integer heightPos, lowPos; // 定义高低位
            heightPos = (176 + Math.abs(random.nextInt(39)));
            lowPos = (161 + Math.abs(random.nextInt(93)));
            byte[] bytes = new byte[2];
            bytes[0] = heightPos.byteValue();
            bytes[1] = lowPos.byteValue();
            try {
                builder.append(new String(bytes, "GBK"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return builder.toString();
    }
}
