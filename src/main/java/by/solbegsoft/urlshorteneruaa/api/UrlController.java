package by.solbegsoft.urlshorteneruaa.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@RestController
@RequestMapping(value = "${api.path}"+"/url")
public class UrlController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private BaseUrls baseUrls;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody RequestUrl requestUrl){
        Url url = restTemplate.postForObject(baseUrls.saveUrlGET(), requestUrl, Url.class);
        return new ResponseEntity<>(url, HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping("/{shortUrl}")
    public ResponseEntity<?> get(@PathVariable String shortUrl){

        ResponseEntity<Object> response = restTemplate.exchange(baseUrls.redirectUrlPOST(shortUrl), HttpMethod.GET, null, Object.class);
        URI location = response.getHeaders().getLocation();
        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .location(location)
                .build();
    }
}
