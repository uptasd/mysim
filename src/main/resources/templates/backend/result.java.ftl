package ${packageName};

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
<#noparse>
public class Result<T> implements Serializable {
    private Integer code;//响应码：1 代表成功，0 代表失败
    private String msg; //描述信息
    private T data; // 返回的数据

    public static <T> Result<T> success() {
        return new Result<>(1, "success", null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
   }

    public static <T> Result<T> error(String msg) {
        return new Result<>(0, msg, null);
   }
}
</#noparse>