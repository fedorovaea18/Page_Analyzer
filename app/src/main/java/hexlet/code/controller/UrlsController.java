package hexlet.code.controller;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collections;

import hexlet.code.dto.BasePage;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

public class UrlsController {
    public static void root(Context ctx) {
        var page = new BasePage();
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        ctx.render("index.jte", Collections.singletonMap("page", page));
    }

    public static void create(Context ctx) throws SQLException {
        try {
            var userInputUrl = ctx.formParamAsClass("url", String.class)
                    .getOrDefault("");
            var url = new URI(userInputUrl).toURL();

            if (UrlRepository.isExist(String.valueOf(url))) {
                ctx.sessionAttribute("flash", "Страница уже существует");
                ctx.redirect(NamedRoutes.urlsPath());
                return;
            }

            String protocol = url.getProtocol().isEmpty() ? "null" : url.getProtocol();
            String host = url.getHost().isEmpty() ? "null" : url.getHost();
            String port = url.getPort() == -1 ? "" : ":" + url.getPort();
            String formattedUrl = String.format("%s://%s%s", protocol, host, port);

            Url objUrl = new Url(formattedUrl);
            UrlRepository.save(objUrl);

            ctx.sessionAttribute("flash", "Страница успешно добавлена");
            ctx.redirect(NamedRoutes.urlsPath());
        } catch (IllegalArgumentException | MalformedURLException | URISyntaxException e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.redirect(NamedRoutes.rootPath());
        }
    }

    public static void index(Context ctx) throws SQLException {
        String flash = ctx.consumeSessionAttribute("flash");
        var page = new UrlsPage(UrlRepository.getEntities());
        page.setFlash(flash);

        ctx.render("urls/index.jte", Collections.singletonMap("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Страница не найдена"));

        var page = new UrlPage(url);

        ctx.render("urls/show.jte", Collections.singletonMap("page", page));
    }
}
