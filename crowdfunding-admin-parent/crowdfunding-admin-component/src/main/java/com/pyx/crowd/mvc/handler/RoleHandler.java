package com.pyx.crowd.mvc.handler;

import com.pyx.crowd.service.api.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class RoleHandler {

    @Autowired
    private RoleService roleService;
}
