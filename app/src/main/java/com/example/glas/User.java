package com.example.glas;

public class User {
    private String name, email, password;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }


    public String getName(){
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
