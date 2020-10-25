package com.pyx.crowd.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.pyx.crowd.api.MySQLRemoteService;
import com.pyx.crowd.config.OSSProperties;
import com.pyx.crowd.constant.CrowdConstant;
import com.pyx.crowd.util.CrowdUtil;
import com.pyx.crowd.util.ResultEntity;
import com.pyx.crowd.vo.*;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;



@Controller
public class ProjectConsumerHandler {
	
	@Autowired
	private OSSProperties ossProperties;

	@Autowired
	private MySQLRemoteService mySQLRemoteService;

	@RequestMapping("/get/project/detail/{projectId}")
	public String getProjectDetail(@PathVariable("projectId")Integer projectId, Model model){

		ResultEntity<DetailProjectVO> detailProjectVORemote = mySQLRemoteService.getDetailProjectVORemote(projectId);

		if(ResultEntity.SUCCESS.equals(detailProjectVORemote.getResult())){
			DetailProjectVO detailProjectVO = detailProjectVORemote.getData();
			model.addAttribute("detailProjectVO",detailProjectVO);
		}
		return "project-show-detail";
	}

	/**
	 * 确认信息页面
	 * @param session
	 * @param memberConfirmInfoVO 包含账号和身份证信息
	 * @return
	 */
	@RequestMapping("/create/confirm")
	public String saveConfirm(ModelMap modelMap ,HttpSession session, MemberConfirmInfoVO memberConfirmInfoVO){
		// 从session读取之前临时存储的projectVO对象
		ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);

		if(projectVO==null){
			throw new RuntimeException(CrowdConstant.MESSAGE_TEMPLE_PROJECT_MISSING);
		}

		// 将确认信息数据存入到projectVO对象中
		projectVO.setMemberConfirmInfoVO(memberConfirmInfoVO);

		// 从session域读取当前登录的用户
		MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);

		Integer memberId = memberLoginVO.getId();

		// 调用远程方法保存projectVO
		ResultEntity<String> saveResultEntity = mySQLRemoteService.saveProjectVORemote(projectVO,memberId);

		if(ResultEntity.FAILED.equals(saveResultEntity.getResult())){
			modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,saveResultEntity.getMessage());
			return "project-confirm";
		}

		// 将临时的projectVO对象从session域移除
		session.removeAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);
		return "redirect:http://localhost/project/create/success";

	}

	@ResponseBody
	@RequestMapping("/create/save/return.json")
	public ResultEntity<String> saveReturn(ReturnVO returnVO,HttpSession session){

		try {
			// 从session域中读取之前存储的ProjectVO对象
			ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);

			if(projectVO==null){
				return ResultEntity.failed(CrowdConstant.MESSAGE_TEMPLE_PROJECT_MISSING);
			}

			// 从projectVO对象中获取存储回报信息的集合
			List<ReturnVO> returnVOList = projectVO.getReturnVOList();

			if(returnVOList==null||returnVOList.size()==0){
				returnVOList = new ArrayList<>();
				projectVO.setReturnVOList(returnVOList);
			}

			returnVOList.add(returnVO);

			// 把数据有变化的ProjectVO对象重新存入Session域 以确保新的数据能够存入redis。因为这里用的是SpringSession
			session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT,projectVO);
			return ResultEntity.successWithoutData();

		} catch (Exception e){
			return ResultEntity.failed(e.getMessage());
		}
	}

	/**
	 * 回报信息填写中的上传图片
	 * @param returnPicture formData.append("returnPicture", file);前端页面发送时是returnPicture 因此要保证名字一致
	 */
	@ResponseBody
	@RequestMapping("/create/upload/return/picture.json")
	public ResultEntity<String> uploadReturnPicture(
			// 接受用户上传的文件
			@RequestParam("returnPicture") MultipartFile returnPicture) throws IOException {

		// 执行文件上传
		ResultEntity<String> uploadReturnPicResultEntity = CrowdUtil.uploadFileToOss(
				ossProperties.getEndPoint(),
				ossProperties.getAccessKeyId(),
				ossProperties.getAccessKeySecret(),
				returnPicture.getInputStream(),
				ossProperties.getBucketName(),
				ossProperties.getBucketDomain(),
				returnPicture.getOriginalFilename());

		// 判断上传结果是否成功 无论成功失败都是返回uploadReturnPicResultEntity
		return uploadReturnPicResultEntity;
	}

	/**
	 * 项目发起人信息功能
	 * @param projectVO
	 * @param headerPicture
	 * @param detailPictureList
	 * @param session
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/create/project/information")
	public String saveProjectBasicInfo(
			
			// 接收除了上传图片之外的其他普通数据
			ProjectVO projectVO,
			
			// 接收上传的头图
			MultipartFile headerPicture, 
			
			// 接收上传的详情图片
			List<MultipartFile> detailPictureList, 
			
			// 用来将收集了一部分数据的ProjectVO对象存入Session域
			HttpSession session,
			
			// 用来在当前操作失败后返回上一个表单页面时携带提示消息
			ModelMap modelMap
			) throws IOException {
		
		// 一、完成头图上传
		// 1.获取当前headerPicture对象是否为空
		boolean headerPictureIsEmpty = headerPicture.isEmpty();
		
		if(headerPictureIsEmpty) {
			
			// 2.如果没有上传头图则返回到表单页面并显示错误消息
			modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_HEADER_PIC_EMPTY);
			
			return "project-launch";
			
		}
		// 3.如果用户确实上传了有内容的文件，则执行上传
		ResultEntity<String> uploadHeaderPicResultEntity = CrowdUtil.uploadFileToOss(
				ossProperties.getEndPoint(), 
				ossProperties.getAccessKeyId(), 
				ossProperties.getAccessKeySecret(), 
				headerPicture.getInputStream(), 
				ossProperties.getBucketName(), 
				ossProperties.getBucketDomain(), 
				headerPicture.getOriginalFilename());
		
		String result = uploadHeaderPicResultEntity.getResult();
		
		// 4.判断头图是否上传成功
		if(ResultEntity.SUCCESS.equals(result)) {
			
			// 5.如果成功则从返回的数据中获取图片访问路径
			String headerPicturePath = uploadHeaderPicResultEntity.getData();
			
			// 6.存入ProjectVO对象中
			projectVO.setHeaderPicturePath(headerPicturePath);
		} else {
			
			// 7.如果上传失败则返回到表单页面并显示错误消息
			modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_HEADER_PIC_UPLOAD_FAILED);
			
			return "project-launch";
			
		}
		
		// 二、上传详情图片
		// 1.创建一个用来存放详情图片路径的集合
		List<String> detailPicturePathList = new ArrayList<String>();
		
		// 2.检查detailPictureList是否有效
		if(detailPictureList == null || detailPictureList.size() == 0) {
			modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_DETAIL_PIC_EMPTY);
			
			return "project-launch";
		}
		
		// 3.遍历detailPictureList集合
		for (MultipartFile detailPicture : detailPictureList) {
			
			// 4.当前detailPicture是否为空
			if(detailPicture.isEmpty()) {
				
				// 5.检测到详情图片中单个文件为空也是回去显示错误消息
				modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_DETAIL_PIC_EMPTY);
				
				return "project-launch";
			}
			
			// 6.执行上传
			ResultEntity<String> detailUploadResultEntity = CrowdUtil.uploadFileToOss(
					ossProperties.getEndPoint(), 
					ossProperties.getAccessKeyId(), 
					ossProperties.getAccessKeySecret(), 
					detailPicture.getInputStream(), 
					ossProperties.getBucketName(), 
					ossProperties.getBucketDomain(), 
					detailPicture.getOriginalFilename());
			
			// 7.检查上传结果
			String detailUploadResult = detailUploadResultEntity.getResult();
			
			if(ResultEntity.SUCCESS.equals(detailUploadResult)) {
				
				String detailPicturePath = detailUploadResultEntity.getData();
				
				// 8.收集刚刚上传的图片的访问路径
				detailPicturePathList.add(detailPicturePath);
			} else {
				
				// 9.如果上传失败则返回到表单页面并显示错误消息
				modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_DETAIL_PIC_UPLOAD_FAILED);
				
				return "project-launch";
			}
			
		}
		
		// 10.将存放了详情图片访问路径的集合存入ProjectVO中
		projectVO.setDetailPicturePathList(detailPicturePathList);
		
		// 三、后续操作
		// 1.将ProjectVO对象存入Session域
		session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT, projectVO);
		
		// 2.以完整的访问路径前往下一个收集回报信息的页面
		return "redirect:http://localhost/project/return/info/page";
	}

}
