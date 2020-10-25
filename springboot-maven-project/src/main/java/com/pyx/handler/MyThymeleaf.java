package com.pyx.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
public class MyThymeleaf {

    @Autowired
    private ServletContext servletContext;


    @RequestMapping("/myThymeleaf")
    public String getThymeleaf(HttpSession session, ModelMap modelMap){

        modelMap.addAttribute("attrNameRequestScope","<p style='color:blue;font-size:100px;'>attrValueRequestScope");
        session.setAttribute("attrNameSessionScope","attrValueSessionScope");
        servletContext.setAttribute("attrNameAppScope","attrValueAppScope");
        ArrayList list = new ArrayList();
        list.add("1");
        list.add("2");
        list.add("3");
        modelMap.addAttribute("list",list);

        return "thymeleaf";
    }
}
