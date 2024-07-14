package com.increff.chakra.controller;

import com.nextscm.commons.spring.client.AppClientException;
import com.nextscm.commons.spring.common.ApiException;
import com.nextscm.commons.spring.common.ApiStatus;
import com.nextscm.commons.spring.common.ErrorData;
import com.nextscm.commons.spring.common.FieldErrorData;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.OptimisticLockException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


@ControllerAdvice
@Log4j
public class RestAppControllerAdvice {

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({Throwable.class})
	@ResponseBody
	public ErrorData handleUnknownException(HttpServletRequest req, Throwable t) {
		log.error(Arrays.asList(t.getStackTrace()));
		t.setStackTrace(setStackTrace(t.getStackTrace()));
		log.error("Internal Server Error : " + t.getMessage() + " " + req.getRequestURI(), t);
		ErrorData data = new ErrorData();
		data.setCode(ApiStatus.UNKNOWN_ERROR);
		data.setMessage("Internal error");
		data.setDescription(fromThrowable(t));
		return data;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({BindException.class})
	@ResponseBody
	public ErrorData handleBindException(HttpServletRequest req, BindException e) {
		return handleBindingResult(e.getBindingResult());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({MethodArgumentNotValidException.class})
	@ResponseBody
	public ErrorData handleArgumentException(HttpServletRequest req, MethodArgumentNotValidException e) {
		return handleBindingResult(e.getBindingResult());
	}

	@ExceptionHandler(ApiException.class)
	@ResponseBody
	public ErrorData handleApiException(HttpServletRequest req, ApiException e, HttpServletResponse response) {

		ApiStatus status = e.getStatus();
		setResponseStatus(response, status);

		e.setStackTrace(setStackTrace(e.getStackTrace()));

		log.error("ApiException : " + e.getMessage(), e);

		ErrorData data = new ErrorData();
		data.setCode(e.getStatus());
		data.setMessage(e.getMessage());
		data.setDescription(fromThrowable(e.getCause()));
		data.setErrors(e.getErrors());
		log.error(Arrays.asList(e.getStackTrace()));
		return data;
	}

	@ExceptionHandler(AppClientException.class)
	@ResponseBody
	public ErrorData handleAppClientException(HttpServletRequest req, AppClientException e, HttpServletResponse response) {

		ErrorData error = e.getError();
		//should be handled in abstract client
		if(error == null){
			ErrorData data = new ErrorData();
			data.setMessage("Internal Server Error");
			data.setCode(ApiStatus.UNKNOWN_ERROR);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return data;
		}

		ApiStatus status = error.getCode();
		if(status == null)
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		else
			setResponseStatus(response, status);

		e.setStackTrace(setStackTrace(e.getStackTrace()));

		log.error("AppClientException : " + e.getMessage(), e);
		return e.getError();
	}

	@ExceptionHandler({ObjectOptimisticLockingFailureException.class, OptimisticLockException.class})
	@ResponseBody
	public ErrorData handleOptimisticLockingException(Exception e, HttpServletResponse response) {
		e.setStackTrace(setStackTrace(e.getStackTrace()));
		log.error("OptimisticLockingException : " + e.getMessage(), e);

		ErrorData errorData = new ErrorData();
		errorData.setMessage("Please try again");
		errorData.setCode(ApiStatus.UNKNOWN_ERROR);
		errorData.setDescription(fromThrowable(e.getCause()));
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return errorData;
	}

	public static ErrorData handleBindingResult(BindingResult br) {
		List<FieldErrorData> errors = new ArrayList();
		Iterator var2 = br.getFieldErrors().iterator();

		while(var2.hasNext()) {
			FieldError ferror = (FieldError)var2.next();
			FieldErrorData fdata = new FieldErrorData();
			fdata.setCode(ferror.getCode());
			fdata.setField(ferror.getField());
			fdata.setMessage(ferror.getDefaultMessage());
			errors.add(fdata);
		}

		ErrorData data = new ErrorData();
		data.setCode(ApiStatus.BAD_DATA);
		data.setMessage("Bad input provided");
		data.setErrors(errors);
		return data;
	}

	public static String fromThrowable(Throwable t) {
		if (t == null) {
			return null;
		} else {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			t.printStackTrace(pw);
			return sw.toString();
		}
	}

	private void setResponseStatus(HttpServletResponse response, ApiStatus status) {
		if (status.equals(ApiStatus.RESOURCE_EXISTS))
			response.setStatus(HttpStatus.CONFLICT.value());
		else if (status.equals(ApiStatus.NOT_FOUND))
			response.setStatus(HttpStatus.NOT_FOUND.value());
		else if (status.equals(ApiStatus.UNKNOWN_ERROR))
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		else if(status.equals(ApiStatus.AUTH_ERROR))
			response.setStatus(HttpStatus.FORBIDDEN.value());
		else
			response.setStatus(HttpStatus.BAD_REQUEST.value());
	}

	private static StackTraceElement[] setStackTrace(StackTraceElement[] stackTraceElements){
		
		List<StackTraceElement> stackList = Arrays.stream(stackTraceElements)
				.filter(stackTraceElement -> String.valueOf(stackTraceElement).startsWith("com.nextscm"))
				.collect(Collectors.toList());
		return stackList.toArray(new StackTraceElement[stackList.size()]);
	}

}
