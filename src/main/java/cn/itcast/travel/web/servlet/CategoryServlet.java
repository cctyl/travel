package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Category;
import cn.itcast.travel.domain.Msg;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.service.impl.CategoryServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 旅游分类的servlet
 */
@WebServlet("/category/*")
public class CategoryServlet extends BaseServlet {

    private CategoryService service = new CategoryServiceImpl();

    /**
     * 查询全部分类
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void findCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.查询数据
        List<Category> categoryList = service.findAll();
        //2.返回数据
        returnMsgWithJson(Msg.success().add("list",categoryList));
    }
}
