package hexlet.code.controller;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;

import hexlet.code.dto.BasePage;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.Unirest;

public class UrlController {
    public static void root(Context ctx) {
        var page = new BasePage();
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashColor(ctx.consumeSessionAttribute("flashColor"));
        ctx.render("index.jte", Collections.singletonMap("page", page));
    }

    public static void create(Context ctx) throws SQLException {
        var inputUrl = ctx.formParam("url");

        try {
            URL parsedUrl = new URL(inputUrl);

            if (parsedUrl == null || !parsedUrl.toURI().equals(parsedUrl.toURI().normalize())) {
                throw new MalformedURLException("Invalid URL");
            }

            String normalizedUrl = parsedUrl.toString().toLowerCase();

            if (UrlRepository.isExist(normalizedUrl)) {
                ctx.sessionAttribute("flash", "Страница уже существует");
                ctx.sessionAttribute("flashColor", "info");
                ctx.redirect(NamedRoutes.urlsPath());
                return;
            }

            Url newUrl = new Url(normalizedUrl);
            UrlRepository.save(newUrl);

            ctx.sessionAttribute("flash", "Страница успешно добавлена");
            ctx.sessionAttribute("flashColor", "success");
            ctx.redirect(NamedRoutes.urlsPath());
        } catch (MalformedURLException e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flashColor", "danger");
            ctx.redirect(NamedRoutes.rootPath());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
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

    public static void check(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Entity with id = " + id + " not found"));

        try {
            var response = Unirest.get(url.getName()).asString();
            var statusCode = response.getStatus();
            var createdAt = new Timestamp(System.currentTimeMillis());
            UrlCheck newCheck = new UrlCheck(statusCode, createdAt);
            newCheck.setUrlId(id);
            UrlRepository.saveCheck(newCheck);
            ctx.sessionAttribute("flash", "Страница успешно проверена");
            ctx.sessionAttribute("flashColor", "success");
        } catch (Exception e) {
            ctx.sessionAttribute("flash", "Ошибка при проверке URL");
            ctx.sessionAttribute("flashColor", "danger");
        }

        ctx.redirect(NamedRoutes.urlPath(id));
    }
}
