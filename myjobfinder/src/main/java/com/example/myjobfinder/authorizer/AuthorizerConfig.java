package com.example.myjobfinder.authorizer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("application.properties")
public class AuthorizerConfig
{
    @Value("${clientID}")
    private String clientID = "HK7CAQIRJHKQBMR1L2KOHBJ1BLPP1N03GIV20O5JEUQIQTI43L6E32CHP8VP0H93";
    @Value("${clientSecret}")
    private String clientSecret = "MMNL64F3K647ALJ99OGV51OE70VQA5RH0H7ILG9OFQJBRP30V770M61KFP9LI3SJ";

    private String code = "LDV7VKAD7E1C0M97LLU9OI3GHEROVS92M0CG1V5ADVREK734QAIPNPOQSB1BM1BJ";

    public String accessToken;

    public String refreshToken;

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
