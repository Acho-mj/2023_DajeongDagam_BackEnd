package com.dagagam.dagagamweb.dto;


import lombok.Data;

@Data
public class MemberPwdDto {
    private String original;
    private String newpwd1;
    private String newpwd2;
}
