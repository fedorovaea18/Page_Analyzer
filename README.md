# **Page Analyzer**
[![Actions Status](https://github.com/fedorovaea18/java-project-72/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/fedorovaea18/java-project-72/actions)
[![Maintainability](https://api.codeclimate.com/v1/badges/a2d797a293d3a5e3f80c/maintainability)](https://codeclimate.com/github/fedorovaea18/java-project-72/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/a2d797a293d3a5e3f80c/test_coverage)](https://codeclimate.com/github/fedorovaea18/java-project-72/test_coverage)
[![GitHub Actions Status](https://github.com/fedorovaea18/java-project-72/actions/workflows/github-actions.yml/badge.svg)](https://github.com/fedorovaea18/java-project-72/actions)

This project is a website that analyzes specified pages for SEO suitability. It implements basic principles of modern website construction using MVC architecture: working with routing, request handlers, and a templating system, as well as interacting with a database through ORM.

## Local start

```
git clone git@github.com:fedorovaea18/Page_Analyzer.git
cd Page_Analyzer/app
make build
make run
```

## **Run checkstyle and tests**
```
make lint
make test
```

### **Technology stack:**
- _Frontend: Bootstrap_;
- _Framework: Javalin_;
- _Database: H2(for local development), PostgreSQL(for production)_;
- _Parser: Jsoup_;
- _Testing: JUnit 5, MockWebServer_;
- _Deploy: https://render.com/ (model PaaS)_.
 
### **Deploy on Render.com:** https://java-project-72-a4vs.onrender.com
