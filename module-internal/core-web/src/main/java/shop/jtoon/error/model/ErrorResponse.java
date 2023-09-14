package shop.jtoon.error.model;

import java.util.List;

import org.springframework.validation.FieldError;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;

@Builder
public record ErrorResponse(
	String message,
	@JsonInclude(JsonInclude.Include.NON_EMPTY) List<FieldErrorStatus> errorStatuses
) {

	public record FieldErrorStatus(
		String field,
		String value
	) {
		public static FieldErrorStatus of(FieldError fieldError) {
			return new FieldErrorStatus(fieldError.getField(), fieldError.getDefaultMessage());
		}
	}
}
