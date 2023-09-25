package shop.jtoon.factory;

import org.springframework.mock.web.MockMultipartFile;
import shop.jtoon.entity.Episode;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.Webtoon;
import shop.jtoon.entity.enums.AgeLimit;
import shop.jtoon.entity.enums.DayOfWeek;
import shop.jtoon.entity.enums.Genre;
import shop.jtoon.webtoon.request.CreateEpisodeReq;
import shop.jtoon.webtoon.request.CreateWebtoonReq;
import shop.jtoon.webtoon.request.GetEpisodesReq;
import shop.jtoon.webtoon.request.GetWebtoonsReq;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class WebtoonFactory {

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

    public static CreateWebtoonReq createWebtoonReq() {
        Set<DayOfWeek> dayOfWeeks = new HashSet<>();
        dayOfWeeks.add(DayOfWeek.MON);
        Set<Genre> genres = new HashSet<>();
        genres.add(Genre.ROMANCE);

        return CreateWebtoonReq.builder()
                .title("재윤이의 모험일기")
                .description("재윤이의 개쩌는 모험이야기, 설레지")
                .dayOfWeeks(dayOfWeeks)
                .genres(genres)
                .ageLimit(AgeLimit.ADULT)
                .cookieCount(2)
                .build();
    }

    public static GetWebtoonsReq getWebtoonsReq() {
        return GetWebtoonsReq.builder()
                .day(DayOfWeek.MON)
                .keyword("")
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
