package cn.appsys.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.pojo.page;
import cn.appsys.service.DataDictionaryServiceimpl;

@Controller
public class DevController {

	@Resource
	private DataDictionaryServiceimpl dataDictionaryServiceimpl;

	// 跳转到app维护
	@RequestMapping(value = "appinfolist", method = RequestMethod.GET)
	public String appinfolist(Model model,
			@RequestParam(value = "querySoftwareName", required = false) String querySoftwareName,
			@RequestParam(value = "queryStatus", required = false) String queryStatus,
			@RequestParam(value = "queryFlatformId", required = false) String queryFlatformId,
			@RequestParam(value = "queryCategoryLevel1", required = false) String queryCategoryLevel1,
			@RequestParam(value = "queryCategoryLevel2", required = false) String queryCategoryLevel2,
			@RequestParam(value = "queryCategoryLevel3", required = false) String queryCategoryLevel3,
			@RequestParam(value = "pageIndex", required = false) String pageIndex) {
		// 设置分页
		page page1 = new page();
		page1.setSizePage(5);
		page1.setTotalCount(dataDictionaryServiceimpl.select(querySoftwareName, queryStatus, queryFlatformId,
				queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3));
		if (pageIndex == null || pageIndex == "") {
			pageIndex = "1";
		}
		page1.setCurrentPageNo(Integer.valueOf(pageIndex));
		List<DataDictionary> statusList = dataDictionaryServiceimpl.appState();
		List<DataDictionary> flatFormList = dataDictionaryServiceimpl.appWhat();
		List<AppCategory> categoryLevel1List = dataDictionaryServiceimpl.appCate1();

		// 分页查询，模糊查询
		List<AppInfo> appinfo = dataDictionaryServiceimpl.selectAll(querySoftwareName, queryStatus, queryFlatformId,
				queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3,
				page1.getSizePage() * (page1.getCurrentPageNo() - 1), page1.getSizePage());
		model.addAttribute("statusList", statusList);
		model.addAttribute("flatFormList", flatFormList);
		model.addAttribute("categoryLevel1List", categoryLevel1List);
		model.addAttribute("pages", page1);
		model.addAttribute("appInfoList", appinfo);
		model.addAttribute("querySoftwareName", querySoftwareName);
		model.addAttribute("queryStatus", queryStatus);
		model.addAttribute("queryFlatformId", queryFlatformId);
		model.addAttribute("queryCategoryLevel1", queryCategoryLevel1);
		model.addAttribute("queryCategoryLevel2", queryCategoryLevel2);
		model.addAttribute("queryCategoryLevel3", queryCategoryLevel3);
		return "developer/appinfolist";
	}

	// 显示级别菜单
	@RequestMapping(value = "fy", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public Object view(@RequestParam(value = "pid") String pid) {
		String count = "";
		List<AppCategory> list = null;
		try {
			if (null == pid) {
				list = dataDictionaryServiceimpl.appCate23(pid);
			} else {
				list = dataDictionaryServiceimpl.appCate1();
			}
			count = JSON.toJSONString(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	// 进入修改基本信息页面
	@RequestMapping(value = "appinfomodify", method = RequestMethod.GET)
	public String appinfomodifys() {
		//展示数据
		
		return "developer/appinfomodify";
	}
}
