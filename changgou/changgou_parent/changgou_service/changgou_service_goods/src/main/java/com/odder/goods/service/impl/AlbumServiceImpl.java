package com.odder.goods.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.odder.goods.dao.AlbumMapper;
import com.odder.goods.pojo.Album;
import com.odder.goods.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Description
 * @Author Odder
 * @Date 2019/12/20 0:14
 * @Version 1.0
 */

@Service
public class AlbumServiceImpl implements AlbumService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private AlbumMapper albumMapper;

    @Override
    public List<Album> findAll() {
        return albumMapper.selectAll();
    }

    @Override
    public void add(Album album) {
        albumMapper.insert(album);
    }

    @Override
    public List<Album> findByRule(Album album) {
        Example example = getExample(album);
        return albumMapper.selectByExample(example);
    }

    private Example getExample(Album album) {
        Example example = new Example(Album.class);
        Example.Criteria criteria = example.createCriteria();
        if (album != null) {

            // 编号
            if (!StringUtils.isEmpty(album.getId())) {
                criteria.andEqualTo("id", album.getId());
            }
            // 相册名称
            if (!StringUtils.isEmpty(album.getTitle())) {
                criteria.andLike("title", "%" + album.getTitle() + "%");
            }
            // 相册封面
            if (!StringUtils.isEmpty(album.getImage())) {
                criteria.andEqualTo("image", album.getImage());
            }
            // 图片列表
            if (!StringUtils.isEmpty(album.getImageItems())) {
                criteria.andEqualTo("imageItems", album.getImageItems());
            }

        }
        return example;
    }

    @Override
    public PageInfo<Album> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        List<Album> albums = albumMapper.selectAll();
        return new PageInfo<>(albums);
    }

    @Override
    public PageInfo<Album> findPageAndRule(int page, int size, Album album) {
        PageHelper.startPage(page, size);
        Example example = getExample(album);

        return new PageInfo<>(albumMapper.selectByExample(example));
    }

    @Override
    public Album findById(int id) {
        return albumMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateById(Album album) {
        albumMapper.updateByPrimaryKeySelective(album);
    }

    @Override
    public void deleteById(int id) {
        albumMapper.deleteByPrimaryKey(id);
    }
}
