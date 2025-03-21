package com.example.foodordering.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AccountResponse {

    Integer accountId;

    String username;
    String password;
    String email;
    String role;
}
