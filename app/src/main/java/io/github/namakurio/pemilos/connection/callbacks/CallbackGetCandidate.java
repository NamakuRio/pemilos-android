package io.github.namakurio.pemilos.connection.callbacks;

import java.util.List;

import io.github.namakurio.pemilos.model.Candidate;

public class CallbackGetCandidate {

    String status;
    String msg;
    List<Candidate> data;

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public List<Candidate> getData() {
        return data;
    }

}
