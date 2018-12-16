package ir.component.web.controller;

import ir.component.core.dao.model.News;
import ir.component.core.engine.NewsDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * @author Zahra Afsharinia
 */
@Controller("adminNewsController")
@Scope("view")
public class AdminNewsController implements Serializable {

    private News news = new News();
    @Resource
    private NewsDao newsDao;

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public void save() {
        newsDao.persist(news);
    }

    public List<News> getAllNews() {
        return newsDao.allNews();
    }
}
