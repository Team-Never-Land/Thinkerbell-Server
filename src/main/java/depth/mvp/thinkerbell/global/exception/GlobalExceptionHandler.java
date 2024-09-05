package depth.mvp.thinkerbell.global.exception;

import depth.mvp.thinkerbell.global.dto.ApiResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ApiResult<String> handleBindException(MethodArgumentNotValidException exception) {
		BindingResult bindingResult = exception.getBindingResult();
		StringBuilder builder = new StringBuilder();

		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			builder.append(" [");
			builder.append(fieldError.getField());
			builder.append("] -> ");
			builder.append(fieldError.getDefaultMessage());
			builder.append(" 입력된 값: [");
			builder.append(fieldError.getRejectedValue());
			builder.append("] ");
		}

		return ApiResult.withError(ErrorCode.INVALID_INPUT_VALUE, builder.toString());
	}

	// RuntimeException
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ApiResult<String>> handleRuntimeException(RuntimeException exception) {
		ApiResult<String> apiResult = ApiResult.withError(ErrorCode.INTERNAL_SERVER_ERROR, exception.getMessage());
		return new ResponseEntity<>(apiResult, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// HttpRequestMethodNotSupportedException 처리
	// 지원되지 않는 HTTP 메서드로 요청이 들어왔을 때 발생하는 예외처리
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ApiResult<String>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
		String message = "지원하지 않는 HTTP 메서드입니다: " + exception.getMethod();
		ApiResult<String> apiResult = ApiResult.withError(ErrorCode.INVALID_INPUT_VALUE, message);
		return new ResponseEntity<>(apiResult, HttpStatus.METHOD_NOT_ALLOWED);
	}

	// IllegalArgumentException 처리
	// 잘못된 인자가 메서드에 전달되었을 때 발생하는 예외처리
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiResult<String>> handleIllegalArgumentException(IllegalArgumentException exception) {
		ApiResult<String> apiResult = ApiResult.withError(ErrorCode.INVALID_INPUT_VALUE, exception.getMessage());
		return new ResponseEntity<>(apiResult, HttpStatus.BAD_REQUEST);
	}

}