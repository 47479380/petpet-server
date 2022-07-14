package xmmt.dituon.server.model;

public class Msg {
    private String msg;
    private String error;
    private int code;
    private Object data;

    public Msg(String msg, String error, int code) {
        this.msg = msg;
        this.error = error;
        this.code = code;
    }

    public Msg() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "msg='" + msg + '\'' +
                ", error='" + error + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }
}
