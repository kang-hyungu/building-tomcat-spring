package nextstep.mvc.controller.tobe;

import com.google.common.collect.Maps;
import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;

import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private final Object[] basePackage;

    private final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void initialize() {

    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        return null;
    }
}
