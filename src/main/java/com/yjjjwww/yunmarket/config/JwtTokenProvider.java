package com.yjjjwww.yunmarket.config;

import com.yjjjwww.yunmarket.common.UserType;
import com.yjjjwww.yunmarket.common.UserVo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  @Value("${jwt.secret.key}")
  private String secretKey;

  private static final long TOKEN_EXPIRE = 1000 * 60 * 60;

  private static final String USERTYPE = "userType";
  public static final String TOKEN_PREFIX = "Bearer ";

  public String createToken(String email, Long id, UserType userType) {
    Claims claims = Jwts.claims().setSubject(email).setId(id.toString());
    claims.put(USERTYPE, userType.toString());
    Date now = new Date();
    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + TOKEN_EXPIRE))
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }

  public Authentication getAuthentication(String token) {
    Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();

    String username = claims.getSubject();
    String userType = claims.get("userType", String.class);
    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("ROLE_" + userType));

    UserDetails userDetails = new User(username, "", authorities);

    return new UsernamePasswordAuthenticationToken(userDetails, "",
        userDetails.getAuthorities());
  }

  public boolean validateToken(String token) {
    try {
      Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
      return !claimsJws.getBody().getExpiration().before(new Date());
    } catch (Exception e) {
      return false;
    }
  }

  public UserVo getUserVo(String token) {
    Claims claims = Jwts.parser().setSigningKey(secretKey)
        .parseClaimsJws(token.substring(TOKEN_PREFIX.length())).getBody();
    return new UserVo(Long.parseLong(Objects.requireNonNull(claims.getId())),
        claims.getSubject());
  }
}
