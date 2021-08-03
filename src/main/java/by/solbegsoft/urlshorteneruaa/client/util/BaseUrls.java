package by.solbegsoft.urlshorteneruaa.client.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BaseUrls{
    @Value("${rest.template.url}")
    private String BASE_URL;

    public String redirectUrlGet(){
        return BASE_URL + "/url/redirect/";
    }

    public String saveUrlPost(){
        return BASE_URL + "/url/save";
    }

    public String getAllUrlByUuidGet(){
        return BASE_URL + "/url/getall/";
    }
}
