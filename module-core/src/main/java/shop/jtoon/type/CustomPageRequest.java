package shop.jtoon.type;

import static java.lang.Math.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomPageRequest {

	private static final int MAX_SIZE = 50;

	@Builder.Default
	private Integer page = 1;

	@Builder.Default
	private Integer size = 10;

	public long getOffset() {
		return (long)(max(1, page) - 1) * min(size, MAX_SIZE);
	}
}
