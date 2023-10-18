package org.jmh.model.request;

import lombok.Data;

import java.io.Serializable;
@Data
public class UserLogin implements Serializable {
    private static final long serialVersionUID = 1443578493800982999L;

    private String userAccount;

    private String userPassword;

}
