package com.pyx.crowd.mvc.handler;

import com.github.pagehelper.PageInfo;
import com.pyx.crowd.constant.CrowdConstant;
import com.pyx.crowd.entity.Admin;
import com.pyx.crowd.service.api.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class AdminHandler {

    @Autowired
    private AdminService adminService;

    @RequestMapping("/admin/update.html")
    public String update(Admin admin,
                         @RequestParam(value = "pageNum")Integer pageNum,
                         @RequestParam(value = "keyword")String  keyword){
        adminService.update(admin);
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }

    /**
     * 更新的编辑页面
     */
    @RequestMapping("/admin/to/edit/page.html")
    public String toEditPage(@RequestParam("adminId")Integer adminId,
                             ModelMap modelMap){
        Admin admin = adminService.getAdminById(adminId);
        modelMap.addAttribute("admin",admin);
        return "admin-edit";
    }

    /**
     * 添加数据
     */
    @PreAuthorize("hasAuthority('user:save')")
    @RequestMapping("/admin/save.html")
    public String save(Admin admin){
        adminService.saveAdmin(admin);
        // 添加成功后直接跳转到最后一页
        return "redirect:/admin/get/page.html?pageNum="+Integer.MAX_VALUE;
    }

    /**
     * 删除数据
     */
    @RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.html")
    public String remove(@PathVariable(value = "adminId")Integer adminId,
                         @PathVariable(value = "pageNum")Integer pageNum,
                         @PathVariable(value = "keyword")String  keyword){
        adminService.remove(adminId);
        //删除完成后回到原来分页或者查询后的页面
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }
    /**
     * 查询和分页请求
     */
    @RequestMapping("/admin/get/page.html")
    public String getPageInfo(// 搜索关键词可能有可能没有，如果没有使用默认值""
                              @RequestParam(value = "keyword",defaultValue = "")String keyword,
                              // pageNum默认值为1
                              @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
                              // pageSize默认值为5 每页显示5行
                              @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize,
                              ModelMap modelMap){
        // 调用service方法获取PageInfo对象
        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);
        modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO,pageInfo);
        return "admin-page";
    }
    /**
     * 退出用户请求
     */
    @RequestMapping("/admin/do/logout.html")
    public String doLogout(HttpSession session){
        // 使session失效
        session.invalidate();
        return "redirect:/admin/to/login/page.html";
    }

    /**
     * 登录用户请求 此方法已作废
     */
    @RequestMapping("/admin/do/login.html")
    public String doLogin(@RequestParam("loginAcct")String loginAcct,
                          @RequestParam("userPswd")String userPswd,
                          HttpSession session){
        // 调用Service方法执行登录检查
        // 这个方法如果能返回admin对象，说明登录成功
        Admin admin = adminService.getAdminByLoginAcct(loginAcct,userPswd);
        // 将登录成功的admin对象存入Session域
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN,admin);
        // 重定向到后台主页面 为了避免跳转到后台页面重复提交表单
        // 因为浏览器不能直接访问/WEB-INF目录下的文件，因此这里重定向一个链接，再跳转到页面
        return "redirect:/admin/to/main/page.html";
    }
}
