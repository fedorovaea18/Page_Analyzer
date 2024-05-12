package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

public class AppTest {

    Javalin app;
    private static MockWebServer mockServer;
    private static String urlString;

    @BeforeAll
    public static void startWebServer() throws IOException {
        mockServer = new MockWebServer();
        urlString = mockServer.url("/").toString();
        var page = Files.readString(Paths.get("./src/test/resources/fixtures/testPage.html"));
        MockResponse mockResponse = new MockResponse().setResponseCode(200).setBody(page);
        mockServer.enqueue(mockResponse);
    }

    @BeforeEach
    public final void setUp() throws IOException, SQLException {
        app = App.getApp();
    }

    @AfterAll
    public static void stopWebServer() throws IOException {
        mockServer.shutdown();
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Анализатор страниц");
        });
    }

    @Test
    public void testUrlsPage() {
        JavalinTest.test(app, ((server, client) -> {
            var url1 = new Url("http://www.yandex.ru");
            var url2 = new Url("http://www.mail.ru");
            UrlRepository.saveUrl(url1);
            UrlRepository.saveUrl(url2);
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("yandex").contains("mail");
            assertThat(UrlRepository.getUrlEntities().size()).isEqualTo(2);
        }));
    }

    @Test
    public void testUrlPage() throws SQLException {
        var url = new Url("https://www.yandex.ru", new Timestamp(new Date().getTime()));
        UrlRepository.saveUrl(url);
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/" + url.getId());
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testUrlNotFound() throws Exception {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/999999");
            assertThat(response.code()).isEqualTo(404);
        });
    }

    @Test
    public void testCreateUrl() throws SQLException {
        JavalinTest.test(app, ((server, client) -> {
            var url = "http://www.yandex.ru";
            var requestBody = "url=" + url;
            var response = client.post("/urls", requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains(url);
        }));
    }

    @Test
    public void testCreateInvalidUrl() throws SQLException {
        JavalinTest.test(app, (server, client) -> {
            var url = "www.Yandexru";
            var requestBody = "url=" + url;
            var response = client.post("/urls", requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat((response.body()).string().contains(url));
        });
    }

    @Test
    public void testUrlCheck() throws SQLException, IOException {
        Url url = new Url(urlString);
        UrlRepository.saveUrl(url);
        JavalinTest.test(app, (server, client) -> {
            var response = client.post("/urls/" + url.getId() + "/checks");
            assertThat(response.code()).isEqualTo(200);
            var check = UrlRepository.findUrlChecks(url.getId()).get(0);
            assertThat(check.getTitle()).isEqualTo("Hello World!");
            assertThat(check.getH1()).isEqualTo("World hello!");
            assertThat(check.getDescription()).isEqualTo("Test description");
        });
    }

    /*@Test
    public void testExistedUrl() throws SQLException {
        JavalinTest.test(app, (server, client) -> {
            var url = "http://www.yandex.ru";
            var url2 = "http://www.yandex.ru/";
            var requestBody = "url=" + url;
            var response = client.post("/urls", requestBody);
            var requestBody2 = "url=" + url2;
            var response2 = client.post("/urls", requestBody2);
            var str = response.body().string();

            assertTrue(str.contains(url));
            assertFalse(str.contains(url2));

            // Assuming UrlRepository.findUrl returns an Optional
            assertThat(UrlRepository.findUrl(url)).isPresent();
            assertThat(UrlRepository.findUrl(url2)).isEmpty();
        });
    }*/
}
