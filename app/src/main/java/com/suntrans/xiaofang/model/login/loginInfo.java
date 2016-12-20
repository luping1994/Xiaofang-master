package com.suntrans.xiaofang.model.login;

/**
 * Created by Looney on 2016/12/15.
 */

public class LoginInfo {
   public String error;
    public String message;

    public String token_type;

    public  String expires_in;
    public String access_token;
    public String refresh_token;

    public LoginInfo(){
        this.error="-1";
        this.message="-1";
    }
    @Override
    public String toString() {
        return "LoginInfo{" +
                "error='" + error + '\'' +
                ", message='" + message + '\'' +
                ", token_type='" + token_type + '\'' +
                ", expires_in='" + expires_in + '\'' +
                ", access_token='" + access_token + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                '}';
    }

}
