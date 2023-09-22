package shop.jtoon.type;

import static java.lang.Math.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CustomPageRequest {

	private static final int MAX_SIZE = 50;

	@Builder.Default
	private int page = 1;

	@Builder.Default
	private int size = 10;

	public long getOffset() {
		return (long)(max(1, page) - 1) * min(size, MAX_SIZE);
	}
}
