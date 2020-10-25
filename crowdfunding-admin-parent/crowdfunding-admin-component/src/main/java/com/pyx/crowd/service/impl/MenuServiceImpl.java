package com.pyx.crowd.service.impl;

import com.pyx.crowd.entity.Menu;
import com.pyx.crowd.entity.MenuExample;
import com.pyx.crowd.mapper.MenuMapper;
import com.pyx.crowd.service.api.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {



    @Autowired
    private MenuMapper menuMapper;

    @Override
    public void removeMenu(Integer id) {
        menuMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void updateMenu(Menu menu) {
        menuMapper.updateByPrimaryKeySelective(menu);
    }

    @Override
    public void saveMenu(Menu menu) {
        menuMapper.insert(menu);
    }

    @Override
    public List<Menu> getAll() {

        List<Menu> menus = menuMapper.selectByExample(new MenuExample());
        return menus;
    }
}
