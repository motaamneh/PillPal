package com.motaamneh.pillpal.io;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    public String email;
    public String token;
}
