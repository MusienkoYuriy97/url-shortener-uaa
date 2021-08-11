package by.solbegsoft.urlshorteneruaa.security;

import by.solbegsoft.urlshorteneruaa.exception.JwtAuthenticationException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@PropertySource("classpath:constant.properties")
public class JwtTokenProvider {
    private final UserDetailsService userDetailsService;
    @Value("${JWT_SECRET}")
    private String JWT_SECRET;
    @Value("${JWT_HEADER}")
    private String JWT_HEADER;
    @Value("${JWT_PREFIX}")
    private String JWT_PREFIX;
    @Value("${JWT_EXPIRATION}")
    private Long JWT_EXPIRATION;
    @Value("${JWT_CLAIM_UUID}")
    private String JWT_CLAIM_UUID;
    @Value("${JWT_CLAIM_ROLE}")
    private String JWT_CLAIM_ROLE;


    public JwtTokenProvider(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init(){
        JWT_SECRET = Base64.getEncoder().encodeToString(JWT_SECRET.getBytes());
    }

    public Map<String, String> createToken(String userUuid, String email, String userRole){
        Claims claims = Jwts.claims().setSubject(email);
        claims.put(JWT_CLAIM_ROLE, userRole);
        claims.put(JWT_CLAIM_UUID, userUuid);
        Date now = new Date();
        Date validity = new Date(now.getTime() + JWT_EXPIRATION * 1000L);
        final String access_token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .compact();
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("access_token", access_token);
        return tokenMap;
    }

    public boolean validateToken(String token){
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        }catch (JwtException | IllegalArgumentException e){
            throw new JwtAuthenticationException("Jwt token is expired or invalid!", HttpStatus.UNAUTHORIZED);
        }
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getEmail(String bearerToken){
        String token = bearerToken.replace(JWT_PREFIX,"");
        return Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String resolveToken(HttpServletRequest request){
        return request.getHeader(JWT_HEADER);
    }

    public String getPrefix() {
        return JWT_PREFIX;
    }
}