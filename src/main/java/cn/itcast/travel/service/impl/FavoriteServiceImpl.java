package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.service.FavoriteService;

public class FavoriteServiceImpl implements FavoriteService {

    private FavoriteDao favoriteDao = new FavoriteDaoImpl();
    /**
     * 判断是否收藏
     * @param rid
     * @param uid
     * @return
     */
    @Override
    public boolean isFavorite(String rid, String uid) {

        Favorite favorite = favoriteDao.findByUidAndRid(rid, uid);
        if (favorite==null){
            //为空，没收藏，返回false
            return false;
        }
        //到这里，说明不为空，收藏了，返回true
        return true;



    }

    /**
     * 修改收藏状态
     * @param rid
     * @param uid
     * @return
     */
    @Override
    public boolean changeFavorite(String rid, String uid) {

        try {
            //1.查询是否收藏
            Favorite byUidAndRid = favoriteDao.findByUidAndRid(rid, uid);
            if (byUidAndRid==null){
                System.out.println("changeFavorite 没有收藏"+byUidAndRid);
                //2.没有收藏，就添加收藏
                favoriteDao.add(rid,uid);
            }else {
                //3.已收藏，那就取消收藏
                System.out.println("changeFavorite 已经收藏"+byUidAndRid);
                favoriteDao.cancel(rid,uid);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            //发送异常，更改失败
            return false;
        }


    }
}
