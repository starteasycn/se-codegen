package cn.thinkjoy.${module}.controller;

import cn.thinkjoy.common.managerui.controller.AbstractAdminController;
import cn.thinkjoy.common.managerui.controller.helpers.ActionPermHelper;
import cn.thinkjoy.common.managerui.service.IResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ${basepackage}.common.MenuUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping(value = "/admin/${module}")
public class IndexController extends
        AbstractAdminController<IResourceService> {

    @Autowired
    private IResourceService resourceService;
    @Autowired
    private ActionPermHelper actionPermHelper;

    /**
     * 进入登陆页
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/index", method = {RequestMethod.GET,
            RequestMethod.POST})
    public ModelAndView index(HttpServletRequest request,
                              HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("module/" + getMainObjName());

        List resourceList = actionPermHelper.getResourcePerm();
        mav.addObject("resources",  MenuUtils.getTreeMenu(resourceList));

        List resourceGridList = this.resourceGridService.findList("moduleName",
                getMainObjName());
        mav.addObject("cols", resourceGridList);

        mav.addObject("bizSys", getBizSys());
        mav.addObject("mainObj", getMainObjName());
        mav.addObject("parentTitle", getParentTitle());
        mav.addObject("title", getViewTitle());
        return mav;
    }


    @Override
    public boolean getEnableDataPerm() {

        return true;
    }

    @Override
    protected String getBizSys() {

        return "${module}";
    }

    @Override
    protected String getMainObjName() {

        return "index";
    }

    @Override
    protected String getParentTitle() {
        return null;
    }

    @Override
    protected String getViewTitle() {
        return null;
    }


    @Override
    protected IResourceService getMainService() {
        return null;
    }


}
