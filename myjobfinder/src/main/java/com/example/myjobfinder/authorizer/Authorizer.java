package com.example.myjobfinder.authorizer;

import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

@Controller
public class Authorizer {

    private AuthorizerConfig config;

    public Authorizer (AuthorizerConfig config)
    {
        this.config = config;
    }

    public AuthorizerConfig getConfig() {
        return config;
    }

    public String authorize ()
    {
        return "https://hh.ru/oauth/authorize?response_type=code&client_id=" + config.getClientID();
    }

    @GetMapping("/")
    public void setCodeAndTokens(@RequestParam String code)
    {
        config.setCode(code);
        getTokens();
        System.out.println(config.getAccessToken());
        System.out.println(config.getRefreshToken());
    }

    public void getTokens()
    {
        try
        {
            String charset = "UTF-8";
            String requestData = String.format("grant_type=%s&code=%s&client_id=%s&client_secret=%s",
                    "authorization_code",
                    config.getCode(),
                    config.getClientID(),
                    config.getClientSecret());
            String url = "https://hh.ru/oauth/token?" + requestData;
            HttpsURLConnection request = (HttpsURLConnection) new URL(url).openConnection();
            request.setRequestMethod("POST");
            request.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            request.setDoOutput(true);
            request.connect();
            request.getContent();

            if (request.getResponseCode() != 200)
            {
                System.err.println("connection failed:");
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), charset));
            String response = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            unparseResponse(response);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void unparseResponse (String response)
    {
        try {
            JSONObject json = new JSONObject(response);
            config.setAccessToken(json.getString("access_token"));
            config.setRefreshToken(json.getString("refresh_token"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
