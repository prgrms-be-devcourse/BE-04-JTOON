package shop.jtoon.webtoon.factory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.springframework.mock.web.MockMultipartFile;

import shop.jtoon.entity.Episode;
import shop.jtoon.entity.Gender;
import shop.jtoon.entity.LoginType;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.Role;
import shop.jtoon.entity.Webtoon;
import shop.jtoon.entity.enums.AgeLimit;
import shop.jtoon.webtoon.request.CreateEpisodeReq;
import shop.jtoon.webtoon.request.GetEpisodesReq;

public class CreatorFactory {

	public static Member createMember() {
		return Member.builder()
			.email("test@gmail.com")
			.password("Test123!")
			.name("홍길동")
			.nickname("길동")
			.gender(Gender.MALE)
			.phone("01012345678")
			.role(Role.USER)
			.loginType(LoginType.LOCAL)
			.build();
	}

	public static Webtoon createWebtoon(Member member) {
		return Webtoon.builder()
			.title("웹툰 제목")
			.description("웹툰 설명")
			.ageLimit(AgeLimit.ALL)
			.thumbnailUrl("https://webtoons/thumbnail")
			.cookieCount(3)
			.author(member)
			.build();
	}

	public static Episode createEpisode(Webtoon webtoon, int no) {
		return Episode.builder()
			.no(no)
			.title("회차 제목")
			.mainUrl("https://webtoons/episodes/main")
			.thumbnailUrl("https://webtoons/episodes/thumbnail")
			.hasComment(true)
			.openedAt(LocalDateTime.of(2023, 9, 20, 0, 0, 0))
			.webtoon(webtoon)
			.build();
	}

	public static CreateEpisodeReq createEpisodeReq() {
		return CreateEpisodeReq.builder()
			.no(1)
			.title("회차 제목")
			.hasComment(true)
			.openedAt(LocalDateTime.of(2023, 9, 20, 0, 0, 0))
			.build();
	}

	public static GetEpisodesReq createGetEpisodesReq() {
		return GetEpisodesReq.builder().build();
	}

	public static MockMultipartFile createMultipartFile() {
		Path path = Paths.get("src/test/resources/test.png");

		try {
			return new MockMultipartFile(
				"image",
				"test.png",
				"image/png",
				Files.readAllBytes(path)
			);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
