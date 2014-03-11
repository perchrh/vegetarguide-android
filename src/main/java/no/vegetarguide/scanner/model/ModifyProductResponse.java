package no.vegetarguide.scanner.model;

import com.google.gson.annotations.Expose;

public class ModifyProductResponse {

    @Expose
    private Integer code = -1;
    @Expose
    private String message;
    @SuppressWarnings("unused")
    public ModifyProductResponse() {
        //used by Gson
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ModifyProductResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }

    public boolean isSuccess() {
        return code != null && code == 0;
    }

}