package kr.co.redbrush.bdd.test.domain.request;

import lombok.Builder;

@Builder
public class LoginRequest {
    private String userId;
    private String password;
}
