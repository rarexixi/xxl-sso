package org.xi.sso.server.resolver;

import org.xi.sso.core.model.ReturnVo;
import org.xi.sso.core.util.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 统一异常处理（Controller切面方式实现）
 * <p>
 * 1、@ControllerAdvice：扫描所有Controller；
 * 2、@ControllerAdvice(annotations=RestController.class)：扫描指定注解类型的Controller；
 * 3、@ControllerAdvice(basePackages={"com.aaa","com.bbb"})：扫描指定package下的Controller
 */
@Component
public class WebExceptionResolver implements HandlerExceptionResolver {

    private static transient Logger logger = LoggerFactory.getLogger(WebExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        logger.error("WebExceptionResolver:{}", ex);

        HandlerMethod method = (HandlerMethod) handler;
        RestController restController = method.getBeanType().getAnnotation(RestController.class);
        ResponseBody responseBody = method.getMethodAnnotation(ResponseBody.class);

        ModelAndView mv = new ModelAndView();
        // 如果返回是JSON
        if (restController != null || responseBody != null) {
            try {
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().print(JacksonUtil.toJson(new ReturnVo<>(ReturnVo.FAIL_CODE, ex.toString())));
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
            return mv;
        }

        mv.addObject("exceptionMsg", geStackTrace(ex));
        mv.setViewName("/common/exception");
        return mv;
    }

    String geStackTrace(Throwable t) {
        try (StringWriter sw = new StringWriter();
             PrintWriter pw = new PrintWriter(sw, true)) {
            t.printStackTrace(pw);
            String printMessage = sw.getBuffer().toString();
            return printMessage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}