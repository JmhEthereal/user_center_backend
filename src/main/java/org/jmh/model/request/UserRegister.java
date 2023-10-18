package org.jmh.model.request;

import lombok.Data;

import java.io.Serializable;
@Data
public class UserRegister implements Serializable {

    private static final long serialVersionUID = -9023248487210947549L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private String planetCode;
}
