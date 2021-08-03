package by.solbegsoft.urlshorteneruaa.client.controller;

import by.solbegsoft.urlshorteneruaa.client.util.BaseUrls;
import by.solbegsoft.urlshorteneruaa.client.dto.UrlCreateRequest;
import by.solbegsoft.urlshorteneruaa.client.model.Url;
import by.solbegsoft.urlshorteneruaa.swagger.ApiGetRedirectUrl;
import by.solbegsoft.urlshorteneruaa.swagger.ApiPostSaveUrl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "${api.path}"+"/url")
@Tag(name = "UrlController", description = "End points for url")
public class UrlController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private BaseUrls baseUrls;

    @PostMapping("/create")
    @ApiPostSaveUrl
    public ResponseEntity<?> save(@Valid @RequestBody UrlCreateRequest request){
        return new ResponseEntity<>(restTemplate.postForObject(baseUrls.saveUrlGet(), request, Url.class),
                HttpStatus.CREATED);
    }

    @GetMapping("/redirect/{shortUrl}")
    @ApiGetRedirectUrl
    public ResponseEntity<?> redirect(@PathVariable String shortUrl){
        ResponseEntity<Object> response = restTemplate.exchange(baseUrls.redirectUrlPost(shortUrl), HttpMethod.GET, null, Object.class);
        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .location(response.getHeaders().getLocation())
                .build();
    }
}