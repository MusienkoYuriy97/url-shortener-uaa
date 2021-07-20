package by.solbegsoft.urlshorteneruaa.api;

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

    @PostMapping("/")
    @ApiPostSaveUrl
    public ResponseEntity<?> save(@Valid @RequestBody RequestUrlDto requestUrlDto){
        Url url = restTemplate.postForObject(baseUrls.saveUrlGET(), requestUrlDto, Url.class);
        return new ResponseEntity<>(url, HttpStatus.CREATED);
    }

    @GetMapping("/{shortUrl}")
    @ApiGetRedirectUrl
    public ResponseEntity<?> redirect(@PathVariable String shortUrl){
        ResponseEntity<Object> response = restTemplate.exchange(baseUrls.redirectUrlPOST(shortUrl), HttpMethod.GET, null, Object.class);
        URI location = response.getHeaders().getLocation();
        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .location(location)
                .build();
    }
}