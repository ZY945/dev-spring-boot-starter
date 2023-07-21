package io.github.zy945.util.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 伍六七
 * @date 2023/5/22 9:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private Integer code;
    private T data;
    private String msg;


    public static <T> Result<T> success(T data, String msg) {
        return new Result<>(200, data, msg);
    }

    public static <T> Result<T> authFail(T data, String msg) {
        return new Result<>(401, data, msg);
    }

    public static <T> Result<T> roleFail(T data, String msg) {
        return new Result<>(403, data, msg);
    }

    /**
     * 限流
     * 429 Too Many Requests
     */
    public static <T> Result<T> requestLimitFail(String msg) {
        return new Result<>(429, null, msg);
    }


    /**
     * 服务器错误
     * 500 Server error
     */
    public static <T> Result<T> serverFail(String msg) {
        return new Result<>(500, null, msg);
    }

}
