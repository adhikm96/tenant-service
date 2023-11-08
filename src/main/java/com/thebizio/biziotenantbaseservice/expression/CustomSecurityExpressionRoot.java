package com.thebizio.biziotenantbaseservice.expression;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

@Component
public class CustomSecurityExpressionRoot{

    @Value("${x-priv-pwd}")
    private String xPrivatePassword;
    public final String X_PRIV_PWD = "X-PRIV-PWD";

    public boolean isAuthorizedIpAddress(String ipAdd) {

        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes())
                .getRequest();

        HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes())
                .getResponse();

        // check auth
        if(SecurityContextHolder.getContext().getAuthentication() == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // checks X-Forwarded-For header

        Enumeration<String> er = request.getHeaderNames();
        while (er.hasMoreElements()){
            if (er.nextElement().equals("x-forwarded-for")){
                return false;
            }
        }

        IpAddressMatcher matcher = new IpAddressMatcher(ipAdd);
        try {
            if(!matcher.matches(request)) {
                return false;
            }
        } catch (UnsupportedOperationException e) {
            return false;
        }

        // check X-PRIV-PWD header
        return (request.getHeader(X_PRIV_PWD) != null && request.getHeader(X_PRIV_PWD).equals(xPrivatePassword));
    }
}
