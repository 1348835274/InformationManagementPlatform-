package cn.appsys.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.pojo.BackendUser;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.pojo.DevUser;
import cn.appsys.pojo.page;
import cn.appsys.service.BackendSerivceompl;
import cn.appsys.service.DataDictionaryServiceimpl;

@Controller
public class BackendController {
	private Logger logger = Logger.getLogger(BackendController.class);

	@Resource
	private DataDictionaryServiceimpl dataDictionaryServiceimpl;

	@Resource
	private BackendSerivceompl backendSerivceompl;

	// 跳转到后台登入界面
	@RequestMapping(value = "/backendlogin.html")
	public String backendlogin() {
		return "backendlogin";
	}

	// 跳转到前台登入界面
	@RequestMapping(value = "/devlogin.html")
	public String devlogin() {
		return "devlogin";
	}

	// redirect:/ 跳转
	// 验证登入到后台
	@RequestMapping(value = "dologin", method = RequestMethod.POST)
	public String dologin(Model model, @RequestParam(value = "userCode") String userCode,
			@RequestParam(value = "userPassword") String userPassword, HttpSession session) {
		BackendUser user = backendSerivceompl.sel_backend(userCode, userPassword);
		if (user != null) {
			session.setAttribute("userSession", user); // 传入会话
			return "backend/main"; // 跳转的主页
		} else {
			System.out.println("登入失败---------------------------------");
			return "backendlogin";
		}
	}

	// 验证登入APP开发者平台代码是否正确
	@RequestMapping(value = "dologins", method = RequestMethod.POST)
	public String dologins(Model model, @RequestParam(value = "devCode") String devCode,
			@RequestParam(value = "devPassword") String devPassword, HttpSession session) {
		DevUser user = backendSerivceompl.sel_dev(devCode, devPassword);
		if (user != null) {
			session.setAttribute("devUserSession", user); // 传入会话
			return "developer/main"; // 跳转的主页
		} else {
			System.out.println("登入失败---------------------------------");
			return "devlogin";
		}
	}

	// 注销
	@RequestMapping(value = "/manager/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		if (session != null) {
			session.removeAttribute("userSession");
			return "backendlogin";
		}
		return "backend/main";
	}

	// 注销
	@RequestMapping(value = "dev/logout", method = RequestMethod.GET)
	public String logout1(HttpSession session) {
		if (session != null) {
			session.removeAttribute("dev/logout");
			return "devlogin";
		}
		return "backend/main";
	}

	// 跳转到app审核
	@RequestMapping(value = "/manager/backend/app/list")
	public String appinfolist(Model model,
			@RequestParam(value = "querySoftwareName", required = false) String querySoftwareName,
			@RequestParam(value = "queryFlatformId", required = false) String queryFlatformId,
			@RequestParam(value = "queryCategoryLevel1", required = false) String queryCategoryLevel1,
			@RequestParam(value = "queryCategoryLevel2", required = false) String queryCategoryLevel2,
			@RequestParam(value = "queryCategoryLevel3", required = false) String queryCategoryLevel3,
			@RequestParam(value = "pageIndex", required = false) String pageIndex) {
		// 设置分页
		page page = new page();
		page.setSizePage(5); // 设置页面大小
		page.setTotalCount(backendSerivceompl.select(querySoftwareName, queryFlatformId, queryCategoryLevel1,
				queryCategoryLevel2, queryCategoryLevel3));

		// 确定当前页
		if (pageIndex == null || pageIndex == "") {
			pageIndex = "1";
		}
		page.setCurrentPageNo(Integer.valueOf(pageIndex));

		// 页面传值
		List<AppInfo> appinfo = backendSerivceompl.selectAll(querySoftwareName, queryFlatformId, queryCategoryLevel1,
				queryCategoryLevel2, queryCategoryLevel3, page.getSizePage() * (page.getCurrentPageNo() - 1),
				page.getSizePage());

		List<DataDictionary> flatFormList = dataDictionaryServiceimpl.appWhat(); // 所属平台
		List<AppCategory> categoryLevel1List = dataDictionaryServiceimpl.appCate1(); // 一级分类

		model.addAttribute("flatFormList", flatFormList);
		model.addAttribute("categoryLevel1List", categoryLevel1List);
		model.addAttribute("pages", page);
		model.addAttribute("appInfoList", appinfo);
		model.addAttribute("querySoftwareName", querySoftwareName);
		model.addAttribute("queryFlatformId", queryFlatformId);
		model.addAttribute("queryCategoryLevel1", queryCategoryLevel1);
		model.addAttribute("queryCategoryLevel2", queryCategoryLevel2);
		model.addAttribute("queryCategoryLevel3", queryCategoryLevel3);

		return "backend/applist";
	}

	// 显示23级内容
	@RequestMapping(value = "list", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object view(@RequestParam(value = "pid") String pid) {
		String count = "";
		List<AppCategory> list = null;
		try {
			list = dataDictionaryServiceimpl.appCate23(pid);
			count = JSON.toJSONString(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	// 跳转到修改界面
	@RequestMapping(value = "check", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	public String check(Model model, @RequestParam(value = "aid") String aid, @RequestParam(value = "vid") String vid) {
		AppInfo info = backendSerivceompl.selAppinfo(aid);
		AppVersion sion = backendSerivceompl.alter(vid);
		System.err.println(info.getDevName());
		model.addAttribute("appInfo", info);
		model.addAttribute("appVersion", sion);
		return "backend/appcheck";
	}

	// 确定是否审核成
	@RequestMapping(value = "checksave", method = RequestMethod.POST)
	public String check(@RequestParam(value = "id") String id, @RequestParam(value = "status") String status) {
		Boolean bool = backendSerivceompl.alterApp(id, status);
		if (bool) {
			return "redirect:/manager/backend/app/list";
		}
		return "check";
	}
}
