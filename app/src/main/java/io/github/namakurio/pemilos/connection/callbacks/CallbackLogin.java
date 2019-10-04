package io.github.namakurio.pemilos.connection.callbacks;

import java.util.List;

import io.github.namakurio.pemilos.model.User;

public class CallbackLogin {

    String status;
    String msg;
    List<User> data;

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public List<User> getData() {
        return data;
    }
}
