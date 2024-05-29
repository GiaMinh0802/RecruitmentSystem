package com.fpt.recruitmentsystem.dto;

import com.google.api.client.auth.oauth2.Credential;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CredentialDTO {
    private Credential credential;
    private String url;
}
