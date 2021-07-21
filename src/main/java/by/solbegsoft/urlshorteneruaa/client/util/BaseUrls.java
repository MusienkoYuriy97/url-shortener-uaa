package by.solbegsoft.urlshorteneruaa.client.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BaseUrls{
    @Value("${rest.template.url}")
    private String BASE_URL;

    public String redirectUrlPost(String shortUrl){
        return BASE_URL + "/" + shortUrl;
    }

    public String saveUrlGet(){
        return BASE_URL;
    }
}
