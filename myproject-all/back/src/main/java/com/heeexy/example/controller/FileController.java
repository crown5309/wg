package com.heeexy.example.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.config.system.ServerConfig;
import com.heeexy.example.service.goodsClassService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.DateUtil;
import com.heeexy.example.util.OrderCodeFactory;
@RestController
public class FileController {
	@Value("${imgPath}")
	private String imgPath;
	@RequestMapping(value="/uploadimg", method = RequestMethod.POST)
    public @ResponseBody JSONObject uploadImg(@RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
		String scheme = request.getScheme();//http
		String serverName = request.getServerName();//localhost
		int serverPort = request.getServerPort();//8080
		String contextPath = request.getContextPath();//项目名
		String url = scheme+"://"+serverName+":"+serverPort+contextPath;//http://127.0.0.1:8080/test
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        /*System.out.println("fileName-->" + fileName);
        System.out.println("getContentType-->" + contentType);*/
        String format = DateUtil.format(new Date(), "yyyMMdd")+"/";
		String filePath = imgPath+format;
		String substring = OrderCodeFactory.getOrderIdByUUId()+fileName.substring(fileName.lastIndexOf("."));
        try {
			uploadFile(file.getBytes(), filePath,substring);
        } catch (Exception e) {
            // TODO: handle exception
        }
        //返回json
        return CommonUtil.successJson(url+"/img"+"/"+format+substring);
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
