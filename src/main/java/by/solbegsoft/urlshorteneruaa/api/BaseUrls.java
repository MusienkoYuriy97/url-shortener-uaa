package by.solbegsoft.urlshorteneruaa.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BaseUrls{
    @Value("${rest.template.url}")
    private String BASE_URL;

    public String redirectUrlPOST(String shortUrl){
        return BASE_URL + "/" + shortUrl;
    }

    public String saveUrlGET(){
        return BASE_URL;
    }
}
