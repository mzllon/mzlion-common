package com.mzlion.poi.entity;

import com.mzlion.poi.annotation.ExcelCell;

/**
 * Created by mzlion on 2016/6/13.
 */
public class Account {

    private String id;

    @ExcelCell("账户名称")
    private String name;

    @ExcelCell("账户余额")
    private String balance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public Account() {
    }

    public Account(String id, String name, String balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Account{");
        sb.append("id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", balance='").append(balance).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
