package by.solbegsoft.urlshorteneruaa.client.controller;

import by.solbegsoft.urlshorteneruaa.client.service.UrlService;
import by.solbegsoft.urlshorteneruaa.client.dto.UrlCreateRequest;
import by.solbegsoft.urlshorteneruaa.swagger.ApiGetRedirectUrl;
import by.solbegsoft.urlshorteneruaa.swagger.ApiPostSaveUrl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

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