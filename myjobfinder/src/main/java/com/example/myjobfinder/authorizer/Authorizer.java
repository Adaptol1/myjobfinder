package com.example.myjobfinder.authorizer;

import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.myjobfinder.authorizer.AuthorizerConfig;


import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.stream.Collectors;

@Service
public class Authorizer {

    private AuthorizerConfig config;

    public Authorizer (AuthorizerConfig config)
    {
        this.config = config;
    }

    public String authorize ()
    {
        return "https://hh.ru/oauth/authorize?response_type=code&client_id=" + config.getClientID();
    }

    @GetMapping("/")
    public void getCode(@RequestParam String code)
    {
        config.setCode(code);
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
//            OutputStream outStream = request.getOutputStream();
//            OutputStreamWriter outStreamWriter = new OutputStreamWriter(outStream, charset);
//            outStreamWriter.write(requestData);
//            outStreamWriter.flush();
//            outStreamWriter.close();
//            outStream.close();
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
