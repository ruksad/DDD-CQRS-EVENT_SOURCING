package com.springbank.user.query.api.dto;

import com.springbank.user.core.dto.BaseResponse;
import com.springbank.user.core.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class UserLookUpResponse extends BaseResponse {
    private List<User> user;
    public UserLookUpResponse (String message){
        super(message);
    }
    public UserLookUpResponse(String message, User user){
        super(message);
        this.user=new ArrayList<>();
        this.user.add(user);
    }

    public UserLookUpResponse(User user){
        super(null);
        this.user=new ArrayList<>();
        this.user.add(user);
    }
    public UserLookUpResponse(List<User> user){
        super(null);
        this.user=new ArrayList<>();
        this.user.addAll(user);
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }
}
