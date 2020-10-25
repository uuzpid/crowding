package com.pyx.crowd.mvc.handler;

import com.pyx.crowd.entity.Auth;
import com.pyx.crowd.entity.Role;
import com.pyx.crowd.service.api.AdminService;
import com.pyx.crowd.service.api.AuthService;
import com.pyx.crowd.service.api.RoleService;
import com.pyx.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class AssignHandler {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;

    @ResponseBody
    @RequestMapping("/assign/do/role/assign/auth.json")
    public ResultEntity<String> saveRoleAuthRelationship(
            @RequestBody Map<String,List<Integer>> map){
        authService.saveRoleAuthRelationship(map);
        return ResultEntity.successWithoutData();
    }

    @ResponseBody
    @RequestMapping("/assign/get/assigned/auth/id/by/role/id.json")
    public ResultEntity<List<Integer>> getAssignedAuthIdByRoleId(
            @RequestParam("roleId")Integer roleId){
        List<Integer> authIdList = authService.getAssignedAuthIdByRoleId(roleId);
        return ResultEntity.successWithData(authIdList);
    }


    @ResponseBody
    @RequestMapping("/assgin/get/all/auth.json")
    public ResultEntity<List<Auth>> getAllAuth(){
        List<Auth> authList = authService.getAll();
        return ResultEntity.successWithData(authList);
    }

    @RequestMapping("/assign/do/role/assign.html")
    public String saveAdminRoleRelationShip(@RequestParam("adminId")Integer adminId,
                                            @RequestParam("pageNum")Integer pageNum,
                                            @RequestParam("keyword")String keyword,
                                            // 这里是允许roleIdList为null，因此要设置 required = false
                                            @RequestParam(value = "roleIdList",required = false)List<Integer> roleIdList){
        adminService.saveAdminRoleRelationShip(adminId,roleIdList);
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }

    @RequestMapping("/assign/to/assign/role/page.html")
    public String toAssignRolePage(@RequestParam("adminId")Integer adminId,
                                   ModelMap modelMap){
        // 查询已分配的角色
        List<Role> assignedRoleList = roleService.getAssignedRole(adminId);

        // 查询未分配的角色
        List<Role> unassignedRoleList = roleService.getUnAssignedRole(adminId);

        // 存入模型
        modelMap.addAttribute("assignedRoleList",assignedRoleList);
        modelMap.addAttribute("unassignedRoleList",unassignedRoleList);
        return "assign-role";
    }
}
