package by.solbegsoft.urlshorteneruaa.client.controller;

import by.solbegsoft.urlshorteneruaa.client.util.BaseUrls;
import by.solbegsoft.urlshorteneruaa.client.dto.UrlCreateRequest;
import by.solbegsoft.urlshorteneruaa.client.model.Url;
import by.solbegsoft.urlshorteneruaa.security.UserDetailServiceImpl;
import by.solbegsoft.urlshorteneruaa.swagger.ApiGetRedirectUrl;
import by.solbegsoft.urlshorteneruaa.swagger.ApiPostSaveUrl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping(value = "${api.path}"+"/url")
@RequiredArgsConstructor
@Tag(name = "UrlController", description = "End points for url")
public class UrlController {
    private final UserDetailServiceImpl userDetailService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private BaseUrls baseUrls;

    @PostMapping("/create")
    @ApiPostSaveUrl
    public ResponseEntity<?> save(@Valid @RequestBody UrlCreateRequest request){
        final UUID uuid = userDetailService.getCurrentUser().getUuid();
        request.setUserUuid(uuid.toString());
        return new ResponseEntity<>(restTemplate.postForObject(baseUrls.saveUrlPost(), request, Url.class),
                HttpStatus.CREATED);
    }

    @GetMapping("/getall")
    @ApiPostSaveUrl
    public ResponseEntity<?> getAll(){
        final UUID uuid = userDetailService.getCurrentUser().getUuid();
        return new ResponseEntity<>(restTemplate.getForObject(baseUrls.getAllUrlByUuidGet() + uuid, List.class),
                HttpStatus.CREATED);
    }

    @GetMapping("/redirect/{shortUrl}")
    @ApiGetRedirectUrl
    public ResponseEntity<?> redirect(@PathVariable String shortUrl){
        ResponseEntity<Object> response = restTemplate.exchange(baseUrls.redirectUrlGet() + shortUrl, HttpMethod.GET, null, Object.class);
        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .location(Objects.requireNonNull(response.getHeaders().getLocation()))
                .build();
    }
}