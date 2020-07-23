package com.heeexy.example.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.heeexy.example.service.ImgPathService;
import com.sun.deploy.nativesandbox.comm.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.DateUtil;
import com.heeexy.example.util.OrderIdFactory;
@RestController
public class FileController {
	@Value("${imgPath}")
	private String imgPath;
	@Value("${imgServerUrl}")
	private String imgServerUrl;
	@Autowired
	private ImgPathService imgPathService;
	@RequestMapping(value="/uploadimg", method = RequestMethod.POST)
    public @ResponseBody JSONObject uploadImg(@RequestParam("file") MultipartFile[] file,
            HttpServletRequest request) {
		String scheme = request.getScheme();//http
		String serverName = request.getServerName();//localhost
		int serverPort = request.getServerPort();//8080
		String contextPath = request.getContextPath();//项目名
		String url = scheme+"://"+serverName+":"+serverPort+contextPath;//http://127.0.0.1:8080/test
		List<String> fiList=new ArrayList<String>();
		for(MultipartFile f:file) {
			    String contentType = f.getContentType();
		        String fileName = f.getOriginalFilename();
		        String format = DateUtil.format(new Date(), "yyyMMdd")+"/";
				String filePath = imgPath+format;
				String substring = OrderIdFactory.getOrderIdByUUId()+fileName.substring(fileName.lastIndexOf("."));
		        try {
					uploadFile(f.getBytes(), filePath,substring);
					fiList.add(imgServerUrl+"/img"+"/"+format+substring);
					imgPathService.addImgPath(format+substring, request.getParameter("oldImg"),imgServerUrl);
		        } catch (Exception e) {
		            // TODO: handle exception
		        }
		}
       
        //返回json
        return CommonUtil.successJson(fiList);
    }
	@RequestMapping(value="/updateImg", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject updateImg(String oldImg) {
		imgPathService.addImgPath(null,oldImg,imgServerUrl);
		return CommonUtil.successJson();
	}
	public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception { 
        File targetFile = new File(filePath);  
        if(!targetFile.exists()){    
            targetFile.mkdirs();    
        }       
        FileOutputStream out = new FileOutputStream(filePath+fileName);
        out.write(file);
        out.flush();
        out.close();
    }
}
