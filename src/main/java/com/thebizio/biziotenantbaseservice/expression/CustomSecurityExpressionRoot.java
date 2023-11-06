package com.thebizio.biziotenantbaseservice.expression;

import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Component
public class CustomSecurityExpressionRoot{

    public boolean isAuthorizedIpAddress(String ipAdd) {

        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes())
                .getRequest();

        // checks X-Forwarded-For header

        Enumeration<String> er = request.getHeaderNames();
        while (er.hasMoreElements()){
            if (er.nextElement().equals("x-forwarded-for")){
                return false;
            }
        }

        IpAddressMatcher matcher = new IpAddressMatcher(ipAdd);
        try {
            return matcher.matches(request);
        } catch (UnsupportedOperationException e) {
            return false;
        }
    }
}
