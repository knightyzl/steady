package com.sap.psr.vulas.backend.util;

import org.springframework.stereotype.Component;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

// Component duplicated in rest-lib-utils/src/main/java/com/sap/psr/vulas/backend/util/CacheFilter.java

/**
 * <p>CacheFilter class.</p>
 *
 */
@Component
public class CacheFilter implements Filter {

    /**
    * Default destroy
    * @return void
    */
    @Override
    public void destroy() {
        // Nothing
    }

    /**
    * Appends to the response a X-Accel-Expires header equal to two hours if cache=true is present in the querystring of the request
    * @param  request  an incoming HTTP request
    * @param  response the HTTP resonse to return
    * @param  chain the chain of filters
    * @return void
    */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String cache = request.getParameter("cache");
        if (cache != null && cache.equals("true")) {
            // Instructs Nginx to cache the response for 2 hours
            HttpServletResponse httpServletResponse=(HttpServletResponse)response;
            httpServletResponse.setHeader("X-Accel-Expires", "7200");
        }
        chain.doFilter(request, response);
    }

    /**
    * Default init
    * @param  filterConfig the configuration of the filter
    * @return void
    */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Nothing
    }
}
