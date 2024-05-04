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
        page.setFlashColor(ctx.consumeSessionAttribute("flashColor"));
        ctx.render("index.jte", Collections.singletonMap("page", page));
    }

    public static void create(Context ctx) throws SQLException {
        var inputUrl = ctx.formParam("url");
        URL parsedUrl;
        try {
            parsedUrl = new URI(inputUrl).toURL();
        } catch (URISyntaxException | IllegalArgumentException | NullPointerException | MalformedURLException e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flashColor", "danger");
            ctx.redirect(NamedRoutes.rootPath());
            return;
        }

        String protocol = parsedUrl.getProtocol();
        String host = parsedUrl.getHost();
        String port = parsedUrl.getPort() != -1 ? ":" + parsedUrl.getPort() : "";
        String normalizedUrl = (protocol + "://" + host + port).toLowerCase();


        /*String normalizedUrl = String
                .format(
                        "%s://%s%s",
                        parsedUrl.getProtocol(),
                        parsedUrl.getHost(),
                        parsedUrl.getPort() == -1 ? "" : ":" + parsedUrl.getPort()
                )
                .toLowerCase();*/

        Url url = UrlRepository.find(Long.valueOf(normalizedUrl)).orElse(null);

        if (url == null) {
            Url newUrl = new Url(normalizedUrl);
            UrlRepository.save(newUrl);
            ctx.sessionAttribute("flash", "Страница успешно добавлена");
            ctx.sessionAttribute("flashColor", "success");
        } else {
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.sessionAttribute("flashColor", "warning");
        }
        ctx.redirect(NamedRoutes.urlsPath());
    }

    public static void index(Context ctx) throws SQLException {
        var page = new UrlsPage(UrlRepository.getEntities());
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashColor(ctx.consumeSessionAttribute("flashColor"));

        ctx.render("urls/index.jte", Collections.singletonMap("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Entity with id = " + id + " not found"));

        var page = new UrlPage(url);

        ctx.render("urls/show.jte", Collections.singletonMap("page", page));
    }
}

    /*private static String buildUrl(URL url) {
        var protocol = url.getProtocol().isEmpty() ? "null" : url.getProtocol();
        String host = url.getHost().isEmpty() ? "null" : url.getHost();
        String port = url.getPort() == -1 ? "" : ":" + url.getPort();
        return String.format("%s://%s%s", protocol, host, port);
    }*/


