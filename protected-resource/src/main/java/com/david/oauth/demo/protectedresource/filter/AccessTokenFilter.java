package com.david.oauth.demo.protectedresource.filter;

import com.david.oauth.demo.oauthcommons.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.david.oauth.demo.oauthcommons.constants.Constants.TOKEN_TYPE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class AccessTokenFilter extends BasicAuthenticationFilter {

    private JwtTokenUtil jwtTokenUtil;

    public AccessTokenFilter(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        super(authenticationManager);
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(AUTHORIZATION);
        if (header == null || !header.startsWith(TOKEN_TYPE)) {
            chain.doFilter(req, res);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthenticationValidatingJWT(req);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthenticationValidatingJWT(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION).replace(TOKEN_TYPE, "").trim();
        Claims tokenValidated = this.jwtTokenUtil.validateJwtAccessToken(token);
        return new UsernamePasswordAuthenticationToken(tokenValidated, null, new ArrayList<>());
    }
}
