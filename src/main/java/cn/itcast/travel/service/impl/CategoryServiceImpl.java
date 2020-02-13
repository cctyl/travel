package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {
    private CategoryDao dao = new CategoryDaoImpl();

    /**
     * 查询所有分类
     *
     * @return
     */
    @Override
    public List<Category> findAll() {
        //1.从redis中查询
        //1.1获取jedis客户端，使用jedisutils
        Jedis jedis = JedisUtil.getJedis();
        //1.2使用排序查询，查出来的数据就是排好了顺序的
        //Set<String> categorySet = jedis.zrange("category", 0, -1); 因为同时需要cname和cid，那么这个方法已经不满足需求了，改成下面的方法
        //1.3查询sortedset中的分数（cid）和cname
        Set<Tuple> categorySet = jedis.zrangeWithScores("category", 0, -1);
        List<Category> cs = null;
        //2.判断查询的集合是否为空
        if (categorySet == null || categorySet.size() == 0) {
            //3.为空，从数据库查询，将数据存入redis
            cs = dao.findAll();
            //存进redis，以category为key
            for (int i = 0; i < cs.size(); i++) {
                //category这个key，或许相当于一张表
                jedis.zadd("category", cs.get(i).getCid(), cs.get(i).getCname());//这三个参数分别是 ： key，分数，value   分数是用来排序的
            }
        } else {
            //4.不为空，jedis中存好了数据
            //4.0但是他查到的是set集合，而我返回的是list集合，所以将set集合中的数据转存到list集合
            //4.1将set中的数据存入list
            cs = new ArrayList<Category>();

            for (Tuple tuple : categorySet) {
                Category c = new Category((int) tuple.getScore(), tuple.getElement());
                cs.add(c);
            }


        }


        //5.不为空，直接返回
        return cs;
    }
}
