package com.koodo.eblog.shiro;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AccountProfile implements Serializable {

    private Long id;
    private String username;
    private String email;
    private String avatar;
    private String sign;
    private Date created;

    private String gender;

    public String getSex() {
        return "0".equals(gender) ? "女" : "男";
    }

}
