package top.easyboot.titan.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import top.easyboot.titan.constant.Constants;
import top.easyboot.titan.util.IdGenerator;
import top.easyboot.titan.util.NetWorkUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: frank.huang
 * @date: 2021-11-06 16:40
 */
@Component
public class RequestIdGeneratorFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;

        try{
            String requestId = request.getHeader(Constants.REQUEST_ID_HEADER);
            if (StringUtils.isEmpty(requestId)) {
                requestId = IdGenerator.getRequestId();
            }

            MDC.put(Constants.REQUEST_ID, requestId);
            response.setHeader(Constants.REQUEST_ID_HEADER, requestId);
            // 放入ip
            MDC.put(Constants.IP, NetWorkUtils.getRequestSourceIp(request));
            MDC.put(Constants.REQUEST_URL, request.getRequestURI());
            filterChain.doFilter(request,response);
        }finally {
            MDC.clear();
        }
    }

}
