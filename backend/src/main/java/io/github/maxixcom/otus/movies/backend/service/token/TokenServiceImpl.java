package io.github.maxixcom.otus.movies.backend.service.token;

import io.github.maxixcom.otus.movies.backend.config.JwtConfigProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {
    private static final String CLAIM_AUTHORITIES="authorities";
    private final JwtConfigProperties jwtConfigProperties;

    @Override
    public String generateToken(Long principalId, Set<GrantedAuthority> authorities) {
        Map<String, Object> claims = Map.of(
                CLAIM_AUTHORITIES,
                authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet())
        );

        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfigProperties.getKey()));
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + TimeUnit.SECONDS.toMillis(jwtConfigProperties.getExpirationSec()));

        return Jwts.builder()
                .signWith(secretKey)
                .setSubject(String.valueOf(principalId))
                .setIssuedAt(now)
                .setAudience(jwtConfigProperties.getAudience())
                .setIssuer(jwtConfigProperties.getIssuer())
                .setExpiration(expirationDate)
                .addClaims(claims)
                .compact();
    }

    @Override
    public TokenData extractData(String token) {
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfigProperties.getKey()));
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Set<GrantedAuthority> authorities = ((List<String>) claims.get(CLAIM_AUTHORITIES))
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        return new TokenData(
                Long.valueOf(claims.getSubject()),
                authorities
        );
    }
}
