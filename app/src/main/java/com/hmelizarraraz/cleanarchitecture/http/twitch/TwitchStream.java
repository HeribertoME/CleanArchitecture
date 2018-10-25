package com.hmelizarraraz.cleanarchitecture.http.twitch;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TwitchStream {

    @SerializedName("data")
    @Expose
    private List<Stream> stream = null;
    @SerializedName("pagination")
    @Expose
    private Pagination pagination;

    public List<Stream> getStream() {
        return stream;
    }

    public void setStream(List<Stream> stream) {
        this.stream = stream;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

}
