package event.ticket.booking.shared.response;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Map;

@RestControllerAdvice
public class ResponseWrapperAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 1. Tránh wrap lại chính ApiResponse
        if (returnType.getParameterType().equals(ApiResponse.class)) {
            return false;
        }

        // 2. QUAN TRỌNG: Kiểm tra class đang xử lý có thuộc package của Swagger/SpringDoc không
        String packageName = returnType.getContainingClass().getPackageName();
        if (packageName.contains("org.springdoc") || packageName.contains("swagger")) {
            return false;
        }

        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        // Nếu body là null, String (để tránh lỗi cast sang JSON), hoặc đã là ApiResponse thì giữ nguyên
        if (body == null || body instanceof ApiResponse || body instanceof String) {
            return body;
        }

        if (body instanceof Map && ((Map<?, ?>) body).containsKey("message")) {
            return ApiResponse.success(((Map<?, ?>) body).get("message").toString(), null);
        }

        return ApiResponse.success(body);
    }
}
