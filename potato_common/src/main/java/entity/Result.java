package entity;
import java.io.Serializable;

public class Result implements Serializable{
    // 是否成功
    private Boolean flag;
    // 返回码
    private String code;
    // 返回信息
    private String message;
    // 返回数据
    private Object data;


    public Result(Boolean flag, String code, String message) {
        this.flag = flag;
        this.code = code;
        this.message = message;
    }

    public Result(Boolean flag, String code, String message, Object data) {
        this.flag = flag;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Result() {
    }
}