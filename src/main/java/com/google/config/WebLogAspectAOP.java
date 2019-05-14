
package com.google.config;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.google.utils.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * 统一日志记录
 */

@Component
@Aspect
public class WebLogAspectAOP {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static ThreadLocal<Long> startTime = new ThreadLocal<>();

	@Pointcut(value = "execution(public * com.google.controller..*.*(..))")
	public void webLog() {
	}

	@Before("webLog()")
	public void doBefore(JoinPoint joinPoint) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		// 开始时间
		startTime.set(System.currentTimeMillis());
		String requestParam = "";
		//1.请求接口
		String requestInterface = joinPoint.getSignature().getDeclaringTypeName() + "."
				+ joinPoint.getSignature().getName();
		//2.请求Param参数-入参为文件时, 不打印log
		Map originRequestParamMap = request.getParameterMap();
		if (originRequestParamMap.size() > 0) {
			Map<String, Object> filteredFileValueMap = StreamUtils.removeSpecifiedElement(originRequestParamMap,
					new Class[]{MultipartFile.class, File.class});
			requestParam = JsonConvertUtil.formatStandardJSON(JSONObject.toJSONString(filteredFileValueMap));
		}

		//3.请求Body对象 - 入参为文件时, 不打印log
		Object[] originBodyParamArray = joinPoint.getArgs();
		Object[] filteredFileValueArray = StreamUtils.removeSpecifiedElement(originBodyParamArray,
				new Class[]{MultipartFile.class, File.class});

		Map requestBody = Maps.newHashMap();
		if (filteredFileValueArray.length >= 1) {
			if (filteredFileValueArray[0] instanceof HttpServletRequest) {
				//
			} else if (filteredFileValueArray[0] instanceof StandardMultipartHttpServletRequest) {
				requestBody = originRequestParamMap;
			} else {
				Map map = JSONObject.parseObject(JSONObject.toJSONString(filteredFileValueArray[0]));
				requestBody = StreamUtils.removeNullElement(map);
			}
		}
		StringBuilder requestSb = new StringBuilder();
		requestSb.append("\n[请求信息]:\n"
				+ "请求header=" + request.getHeader("Authorization") + "\n"
				+ "请求IP=" + getIpAddress(request) + "\n"
				+ "请求方式=" + request.getMethod() + "\n"
				+ "请求地址=" + Thread.currentThread().getName() + "：" + request.getRequestURL().toString() + "\n"
				+ "请求接口=" + requestInterface + "\n"
				+ "请求Param参数=" + requestParam + "\n"
				+ "请求Body对象= \n");
		requestSb.append(JsonConvertUtil.formatStandardJSON(JSONObject.toJSONString(requestBody)));
		logger.info(requestSb.toString());
	}

	@Around("webLog()")
	public Object doAround(ProceedingJoinPoint proceedingJoinPoint) {
		long startTime = System.currentTimeMillis();
		Object obj = null;
		try {
			obj = proceedingJoinPoint.proceed();
			logger.info("耗时 : {}", (System.currentTimeMillis() - startTime));
		} catch (Throwable throwable) {
			obj = handlerException(proceedingJoinPoint, throwable);
		}
		return obj;
	}

	@AfterReturning(value = "execution(public * com.google.controller..*.*(..))", returning = "returnValue")
	public void doAfterReturning(JoinPoint joinPoint, Object returnValue) {
		// 处理完请求，返回内容
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		String requestInterface = joinPoint.getSignature().getDeclaringTypeName() + "."
				+ joinPoint.getSignature().getName();
		long execTime = System.currentTimeMillis() - startTime.get();
		StringBuilder responseSb = new StringBuilder();
		responseSb.append("\n[后台响应结果]:\n"
				+ "URL地址=" + request.getRequestURL().toString() + "\n"
				+ "后台接口=" + requestInterface + "\n"
				+ "响应耗时[" + execTime + "ms]" + "\n"
				+ "响应数据结果:\n"
		);
		responseSb.append(JSONObject.toJSONString(returnValue));
		logger.info(responseSb.toString());
		startTime.remove();
	}

	/**
	 * 获取真实IP
	 * @param request
	 * @return
	 */
	private String getIpAddress(HttpServletRequest request) {
		String ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if ("127.0.0.1".equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress)) {
				//根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					logger.error("获取ip异常 : {}", e);
				}
				ipAddress = inet.getHostAddress();
			}
		}
		if (ipAddress != null && ipAddress.length() > 15) {
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}

	private Response handlerException(JoinPoint pjp, Throwable ex) {
		Response response = new Response();
		response.setStatus(0);
		Object[] args = pjp.getArgs();
		StringBuilder sb = new StringBuilder();
		for (Object arg : args) {
			sb.append(arg);
		}
		logger.info("参数 : {}", sb.toString());
		if (ex instanceof BaseException) {
			BaseException e = (BaseException) ex;
			response.setCode(e.getErrCode());
			response.setMsg(e.getMessage());
			logger.error("BaseException==>方法：{}，参数：{}，异常：{}", pjp.getSignature(), sb.toString(), e.getMessage());
		} else if (ex instanceof BusinessException) {
			BusinessException e = (BusinessException) ex;
			response.setCode(e.getErrCode());
			response.setMsg(e.getMessage());
			logger.error("BusinessException==>方法：{}，参数：{}，异常：{}", pjp.getSignature(), sb.toString(), ex);
		} else {
			response.setCode(SystemStatus.SERVER_ERROR_CODE.getCode());
			response.setMsg(ex.getMessage());
			logger.error("异常==>方法：{}，参数：{}，异常：{}", pjp.getSignature(), sb.toString(), ex);
		}
		return response;
	}
}
