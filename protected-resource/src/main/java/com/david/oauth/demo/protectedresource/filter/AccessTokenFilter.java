package com.david.oauth.demo.protectedresource.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.ArrayList;

public class AccessTokenFilter extends BasicAuthenticationFilter {

    private String jwtKey;

    public AccessTokenFilter(AuthenticationManager authenticationManager, String jwtKey) {
        super(authenticationManager);
        this.jwtKey = jwtKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthenticationValidatingJWT(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthenticationValidatingJWT(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        if (token != null) {

            Claims tokenValidated = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(this.jwtKey))
                    .parseClaimsJws(token)
                    .getBody();
            System.out.println("TOKEN: " + tokenValidated.getExpiration());
            return new UsernamePasswordAuthenticationToken(tokenValidated, null, new ArrayList<>());
        }
        return null;
    }
}
