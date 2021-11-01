package top.easyblog.titan.aspect;

import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import top.easyblog.titan.annotation.ResponseWrapper;
import top.easyblog.titan.response.BaseResponse;
import top.easyblog.titan.response.ResultCode;

import java.util.Objects;

/**
 * 响应增强类
 *
 * @author: frank.huang
 * @date: 2021-11-01 18:28
 */
@ControllerAdvice
public class ResponseResultHandlerAdvice implements ResponseBodyAdvice, Ordered {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return returnType.getAnnotatedElement().isAnnotationPresent(ResponseWrapper.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType mediaType, Class selectedClassType, ServerHttpRequest request, ServerHttpResponse response) {
        String path = request.getURI().getPath();
        if (Objects.nonNull(body)) {
            if(body instanceof  BaseResponse) {
                // 如果响应返回的对象为统一响应体，则直接返回body
                ((BaseResponse) body).setPath(path);
                return body;
            }else if(body instanceof String){
                return new BaseResponse((String)body, path, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
            }
        }
        // 非JSON格式body需要组装成BaseResponse
        return new BaseResponse(body, path, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}