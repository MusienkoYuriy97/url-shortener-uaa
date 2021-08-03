package by.solbegsoft.urlshorteneruaa.client.controller;

import by.solbegsoft.urlshorteneruaa.client.service.UrlService;
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
    private final UrlService urlService;


    @PostMapping("/create")
    @ApiPostSaveUrl
    public ResponseEntity<?> save(@Valid @RequestBody UrlCreateRequest request){
        return new ResponseEntity<>(urlService.save(request),
                HttpStatus.CREATED);
    }

    @GetMapping("/getall")
    @ApiPostSaveUrl
    public ResponseEntity<?> getAll(){
        return new ResponseEntity<>(urlService.getAll(),
                HttpStatus.CREATED);
    }

    @GetMapping("/redirect/{shortUrl}")
    @ApiGetRedirectUrl
    public ResponseEntity<?> redirect(@PathVariable String shortUrl){
        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .location(Objects.requireNonNull(urlService.getLocation(shortUrl)))
                .build();
    }
}