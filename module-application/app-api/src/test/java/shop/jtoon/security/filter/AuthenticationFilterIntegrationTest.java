package shop.jtoon.security.filter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("prod")
class AuthenticationFilterIntegrationTest {

	@Autowired
	MockMvc mockMvc;

	@Test
	@DisplayName("미인증 사용자 접근 실패")
	void unauthenticated_user_fail() throws Exception {
		mockMvc.perform(post("/members/test"))
			.andExpect(status().isUnauthorized());
	}
}
