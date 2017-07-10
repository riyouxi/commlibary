package com.commlibary.http.request;


import com.commlibary.http.request.Parser;

public class StringParser implements Parser<String> {

    @Override
    public String parseResponse(String response) {
        return response;
    }
}
