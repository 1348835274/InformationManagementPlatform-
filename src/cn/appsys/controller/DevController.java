package cn.appsys.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.pojo.DevUser;
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
			list = dataDictionaryServiceimpl.appCate23(pid);
			count = JSON.toJSONString(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	// 展示所属平台
	@RequestMapping(value = "data", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String datadictionarylists() {
		String count1 = "";
		List<DataDictionary> flatFormList = null;
		try {
			flatFormList = dataDictionaryServiceimpl.appWhat();
			count1 = JSON.toJSONString(flatFormList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count1;
	}

	// 进入修改基本信息页面
	@RequestMapping(value = "appinfomodify", method = RequestMethod.GET)
	public String appinfomodifys(@RequestParam(value = "id", required = false) String id, Model model) {
		// 展示数据
		AppInfo appInfo = new AppInfo();
		appInfo.setId(Integer.valueOf(id));
		AppInfo appInfo1 = dataDictionaryServiceimpl.selectId(appInfo);
		model.addAttribute("appInfo", appInfo1);
		return "developer/appinfomodify";
	}

	// 保存修改信息
	@RequestMapping(value = "appinfomodifysave", method = RequestMethod.POST)
	public String appinfomodifysave(@ModelAttribute("appinfo") AppInfo appInfo,
			@RequestParam(value = "attach", required = false) MultipartFile attach, HttpSession session,
			HttpServletRequest request) {
		String idPicPath = null;
		String fileName = null;
		// 判断文件是否为空
		if (!attach.isEmpty()) {
			String path = request.getSession().getServletContext()
					.getRealPath("statics" + File.separator + "uploadfiles");
			String oldFileName = attach.getOriginalFilename();// 原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);// 原文件后缀
			int filesize = 5000000;
			if (attach.getSize() > filesize) { // 上传文件大于500kb
				request.setAttribute("fileUploadError", "上传文件不得超过500kb");
				return "developer/appinfomodify";
			} else if (prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
					|| prefix.equalsIgnoreCase("apk") || prefix.equalsIgnoreCase("pneg")) {// 上传图片格式不正确
				fileName = System.currentTimeMillis() + "_Personal.jsp";
				File file = new File(path, fileName);
				if (!file.exists()) {
					file.mkdirs();
				}
				// 保存
				try {
					attach.transferTo(file);

				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("fileUploadError", "上传失败!");
					return "developer//appinfomodify";
				}
				idPicPath = path + File.separator + fileName;
			} else {
				request.setAttribute("fileUploadError", "上传图片格式不正确");
				return "developer//appinfomodify";
			}
		}
		appInfo.setLogoLocPath(idPicPath);
		appInfo.setLogoPicPath(fileName);
		appInfo.setUpdateDate(new Date());
		int num = dataDictionaryServiceimpl.updateAll(appInfo);
		if (num > 0) {
			return "redirect:appinfolist";
		} else {
			return "redirect:appinfomodify";
		}
	}

	// 查看详细信息
	@RequestMapping(value = "appview/{id}", method = RequestMethod.GET)
	public String viwe(@PathVariable String id, Model model) {
		AppInfo appInfo = dataDictionaryServiceimpl.selectId1(id);
		List<AppVersion> list = dataDictionaryServiceimpl.selectIds(id);
		model.addAttribute("appInfo", appInfo);
		model.addAttribute("appVersionList", list);
		return "developer/appinfoview";
	}

	// 删除
	@RequestMapping(value = "delapp", method = RequestMethod.GET)
	@ResponseBody
	public String delete(@RequestParam(value = "id", required = false) String id) {
		String count = "";
		try {
			if (dataDictionaryServiceimpl.selectIds(id).size() > 0) {
				dataDictionaryServiceimpl.delete(id);
			}
			int i = dataDictionaryServiceimpl.delete1(id);
			count = JSON.toJSONString(i);
		} catch (Exception e) {
			e.printStackTrace();
			count = "-1";
		}
		return count;
	}

	// 进入添加版本页面
	@RequestMapping(value = "appversionadd", method = RequestMethod.GET)
	public String appversionadd(@RequestParam(value = "id", required = false) String id, Model model) {
		List<AppVersion> list = dataDictionaryServiceimpl.selectIds(id);
		model.addAttribute("appVersionList", list);
		model.addAttribute("appId", id);
		return "developer/appversionadd";
	}

	// 保存添加信息
	@RequestMapping(value = "addversionsave", method = RequestMethod.POST)
	public String addversionsave(@ModelAttribute("appVersion") AppVersion appVersion, HttpSession session,
			@RequestParam(value = "a_downloadLink", required = false) MultipartFile attach,
			HttpServletRequest request) {
		String idPicPath = null;
		String fileName = null;
		String xzPath = null;
		// 判断文件是否为空
		if (!attach.isEmpty()) {
			String path = request.getSession().getServletContext()
					.getRealPath("statics" + File.separator + "uploadfiles");
			String oldFileName = attach.getOriginalFilename();// 原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);// 原文件后缀
			int filesize = 5000000;
			if (attach.getSize() > filesize) { // 上传文件大于500kb
				request.setAttribute("fileUploadError", "上传文件不得超过500kb");
				return "developer/appversionadd";
			} else if (prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
					|| prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")) {// 上传图片格式不正确
				fileName = System.currentTimeMillis() + "_Personal.jsp";
				File file = new File(path, fileName);
				if (!file.exists()) {
					file.mkdirs();
				}
				// 保存
				try {
					attach.transferTo(file);

				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("fileUploadError", "上传失败!");
					return "developer/appversionadd";
				}
				idPicPath = path + File.separator + fileName;
				xzPath = File.separator + fileName;
			} else {
				request.setAttribute("fileUploadError", "上传图片格式不正确");
				return "developer/appversionadd";
			}
		}
		if (null != fileName) {
			appVersion.setApkFileName(fileName);
			appVersion.setApkLocPath(idPicPath);
			appVersion.setDownloadLink(xzPath);
		}
		int ids = 1;
		System.err.println(ids);
		appVersion.setCreatedBy(ids);
		appVersion.setCreationDate(new Date());
		// 添加版本
		int num = dataDictionaryServiceimpl.insertVersion(appVersion);
		int num2 = dataDictionaryServiceimpl.bb(appVersion.getAppId());
		if (num > 0) {// 判断是否添加成功
			int num1 = dataDictionaryServiceimpl.updateVersionId(appVersion.getAppId(), num2);
			if (num1 > 0) {
				return "redirect:appinfolist";
			} else {
				return "redirect:appversionadd";
			}
		} else {
			return "redirect:appversionadd";
		}

	}

	// 进入修改最新版本页面
	@RequestMapping(value = "appversionmodify", method = RequestMethod.GET)
	public String appversionmodify(@RequestParam(value = "vid", required = false) String vid,
			@RequestParam(value = "aid", required = false) String aid, Model model) {
		// 获取历史版本
		List<AppVersion> list = dataDictionaryServiceimpl.selectIds(aid);
		// 获取当前版本
		AppVersion appVersion = dataDictionaryServiceimpl.selectBB(vid);
		model.addAttribute("appVersionList", list);
		model.addAttribute("appVersion", appVersion);
		return "developer/appversionmodify";
	}

	// 保存修改的最新版本信息
	@RequestMapping(value = "appversionmodifysave", method = RequestMethod.POST)
	public String updateBB(@ModelAttribute("appVersion") AppVersion appVersion, HttpSession session,
			@RequestParam(value = "attach", required = false) MultipartFile attach, HttpServletRequest request) {
		String idPicPath = null;
		String fileName = null;
		String xzPath = null;
		// 判断文件是否为空
		if (!attach.isEmpty()) {
			String path = request.getSession().getServletContext()
					.getRealPath("statics" + File.separator + "uploadfiles");
			String oldFileName = attach.getOriginalFilename();// 原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);// 原文件后缀
			int filesize = 5000000;
			if (attach.getSize() > filesize) { // 上传文件大于500kb
				request.setAttribute("fileUploadError", "上传文件不得超过500kb");
				return "developer/appinfomodify";
			} else if (prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
					|| prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")) {// 上传图片格式不正确
				fileName = System.currentTimeMillis() + "_Personal.jsp";
				File file = new File(path, fileName);
				if (!file.exists()) {
					file.mkdirs();
				}
				// 保存
				try {
					attach.transferTo(file);

				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("fileUploadError", "上传失败!");
					return "developer/appinfomodify";
				}
				idPicPath = path + File.separator + fileName;
				xzPath = File.separator + fileName;
			} else {
				request.setAttribute("fileUploadError", "上传图片格式不正确");
				return "developer/appinfomodify";
			}
		}
		appVersion.setModifyBy(1);
		appVersion.setModifyDate(new Date());
		if (null != fileName) {
			appVersion.setApkFileName(fileName);
			appVersion.setApkLocPath(idPicPath);
			appVersion.setDownloadLink(xzPath);
		}
		int num = dataDictionaryServiceimpl.updateBB(appVersion);
		if (num > 0) {
			return "redirect:appinfolist";
		} else {
			return "redirect:appversionmodifysave";
		}
	}

	// 删除图片
	@RequestMapping(value = "delfile.json", method = RequestMethod.GET)
	@ResponseBody
	public String deleteTP() {
		return JSON.toJSONString("success");
	}

	// 商品上下架
	@RequestMapping(value = "sale.json", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String sj(@RequestParam(value = "id") String id) {
		System.err.println(id);
		String status = dataDictionaryServiceimpl.selectStatus(id);
		System.err.println(status);
		int num = dataDictionaryServiceimpl.updateSp(status, id);
		Map<String, String> map = new HashMap<String, String>();
		if (num > 0) {
			map.put("errorCode", "0");
			map.put("resultMsg", "success");
		}
		return JSON.toJSONString(map);
	}

	// 跳转新增APP基础信息
	@RequestMapping(value = "/appinfoadd")
	public String appinfoadd() {
		return "developer/appinfoadd";
	}

	// 新增APP基础信息
	@RequestMapping(value = "/appinfoaddsave")
	public String appinfoaddsave(@ModelAttribute("appinfo") AppInfo appinfo, Model model, HttpSession session,
			@RequestParam(value = "a_logoPicPath", required = false) MultipartFile attach, HttpServletRequest request) {
		String idPicPath = null;
		String fileName = null;
		String xzPath = null;
		// 判断文件是否为空
		if (!attach.isEmpty()) {
			String path = request.getSession().getServletContext()
					.getRealPath("statics" + File.separator + "uploadfiles");
			String oldFileName = attach.getOriginalFilename();// 原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);// 原文件后缀
			int filesize = 5000000;
			if (attach.getSize() > filesize) { // 上传文件大于500kb
				request.setAttribute("fileUploadError", "上传文件不得超过500kb");
				return "developer/appinfomodify";
			} else if (prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
					|| prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")) {// 上传图片格式不正确
				fileName = System.currentTimeMillis() + "_Personal.jsp";
				File file = new File(path, fileName);
				if (!file.exists()) {
					file.mkdirs();
				}
				// 保存
				try {
					attach.transferTo(file);

				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("fileUploadError", "上传失败!");
					return "developer/appinfomodify";
				}
				idPicPath = path + File.separator + fileName;
				xzPath = File.separator + fileName;
			} else {
				request.setAttribute("fileUploadError", "上传图片格式不正确");
				return "developer/appinfomodify";
			}
		}
		appinfo.setAPKName(xzPath);
		appinfo.setLogoLocPath(idPicPath);
		appinfo.setLogoPicPath(fileName);
		appinfo.setDevId(1);
		appinfo.setCreationDate(new Date());
		appinfo.setCreatedBy(1);
		int count = dataDictionaryServiceimpl.addAppinfo(appinfo);
		if (count > 0) {
			return "redirect:appinfolist";
		}
		return "/dev/appinfoadd";
	}
}