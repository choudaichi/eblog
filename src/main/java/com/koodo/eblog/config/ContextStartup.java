package com.koodo.eblog.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.koodo.eblog.entity.Category;
import com.koodo.eblog.service.CategoryService;
import com.koodo.eblog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.util.List;

/**
 * 实现 ApplicationRunner 接口使得项目启动时查询category表中的内容，
 * 动态显示header-panel的内容。
 * <p>
 * 实现 ServletContextAware 接口获得 servletContext，
 * 将查询到的内容放到 servletContext中。
 */
@Component
public class ContextStartup implements ApplicationRunner, ServletContextAware {

    @Autowired
    CategoryService categoryService;

    @Autowired
    PostService postService;

    ServletContext servletContext;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //获得 status 是 0 的，然后放到 servletContext中
        List<Category> categories = categoryService.list(new QueryWrapper<Category>().eq("status", 0));
        servletContext.setAttribute("categorys", categories);

        postService.initWeekRank();
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
