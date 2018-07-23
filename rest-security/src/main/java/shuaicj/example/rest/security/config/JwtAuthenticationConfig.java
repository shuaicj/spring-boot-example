package shuaicj.example.rest.security.config;

import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Config JWT.
 * Only one property 'shuaicj.security.jwt.secret' is mandatory.
 *
 * @author shuaicj 2017/08/12
 */
@Component
@Getter
@ToString
public class JwtAuthenticationConfig {

    @Value("${shuaicj.security.jwt.url:/login}")
    private String url;

    @Value("${shuaicj.security.jwt.header:Authorization}")
    private String header;

    @Value("${shuaicj.security.jwt.prefix:Bearer}")
    private String prefix;

    @Value("${shuaicj.security.jwt.expiration:#{24*60*60}}")
    private int expiration; // default 24 hours

    @Value("${shuaicj.security.jwt.secret}")
    private String secret;
}
