@import hexlet.code.dto.urls.UrlsPage
@import hexlet.code.util.NamedRoutes
@import hexlet.code.model.UrlCheck
@import hexlet.code.repository.UrlRepository
@import java.time.format.DateTimeFormatter
@param UrlsPage page

@template.layout.page(
page = page,
content = @`
    <div class="container-lg mt-5">
        <h1>Сайты</h1>
        <table class="table table-bordered table-hover mt-3">
            <thead>
            <tr>
                <th class="col-1">ID</th>
                <th>Имя</th>
                <th class="col-2">Последняя проверка</th>
                <th class="col-1">Код ответа</th>
            </tr>
            </thead>
            <tbody>
            @for(var url : page.getUrls())
                <tr>
                    <td>
                        ${url.getId()}
                    </td>
                    <td>
                        <a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a>
                    </td>
                    <td>
                        ${UrlRepository.findLastUrlCheck(url.getId())
                                        .map(check -> check.getCreatedAt().toLocalDateTime()
                                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                                        .orElse("")}
                    </td>
                    <td>
                        ${UrlRepository.findLastUrlCheck(url.getId())
                                        .map(UrlCheck::getStatusCode)
                                        .map(Object::toString)
                                        .orElse("")}
                    </td>
                </tr>
            @endfor
            </tbody>
        </table>
    </div>
`
)
