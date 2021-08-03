package by.solbegsoft.urlshorteneruaa.client.service;

import by.solbegsoft.urlshorteneruaa.client.dto.UrlCreateRequest;
import by.solbegsoft.urlshorteneruaa.client.model.Url;
import by.solbegsoft.urlshorteneruaa.client.util.BaseUrls;
import by.solbegsoft.urlshorteneruaa.security.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlService {
    private final UserDetailServiceImpl userDetailService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private BaseUrls baseUrls;


    public Url save(UrlCreateRequest request) {
        UUID uuid = userDetailService.getCurrentUser().getUuid();
        request.setUserUuid(uuid.toString());
        return restTemplate.postForObject(baseUrls.saveUrlPost(), request, Url.class);
    }

    public List<Url> getAll() {
        UUID uuid = userDetailService.getCurrentUser().getUuid();
        return restTemplate.getForObject(baseUrls.getAllUrlByUuidGet() + uuid, List.class);
    }

    public URI getLocation(String shortUrl) {
        ResponseEntity<Object> response = restTemplate.exchange(baseUrls.redirectUrlGet() + shortUrl, HttpMethod.GET, null, Object.class);
        return response.getHeaders().getLocation();
    }
}
