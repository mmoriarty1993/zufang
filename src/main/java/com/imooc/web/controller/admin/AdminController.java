package com.imooc.web.controller.admin;

import com.google.gson.Gson;
import com.imooc.base.ApiResponse;
import com.imooc.service.house.QiNiuService;
import com.imooc.web.dto.QiNiuPutRet;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class AdminController {
    @Autowired
    private QiNiuService qiNiuService;
    @Autowired
    private Gson gson;
    @GetMapping("/admin/center")
    public String adminCenterPage(){
        return "admin/center";
    }
    @GetMapping("/admin/welcome")
    public String adminWelcomePage(){
        return "admin/welcome";
    }
    @GetMapping("/admin/login")
    public String adminLoginPage(){
        return "admin/login";
    }

    @GetMapping("/admin/add/house")
    public String addHousePage(){
        return "admin/house-add";
    }
    @PostMapping(value = "/admin/upload/photo",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ApiResponse uploadPhoto(@RequestParam("file")MultipartFile file){
        if (file.isEmpty()){
            return ApiResponse.ofStatus(ApiResponse.Status.NOT_VALID_PARAM);
        }
        String fileName = file.getOriginalFilename();
        try {
            InputStream inputStream = file.getInputStream();
            Response response = qiNiuService.uploadFile(inputStream);
            if (response.isOK()){
                QiNiuPutRet ret = gson.fromJson(response.bodyString(), QiNiuPutRet.class);
                return ApiResponse.ofSuccess(ret);
            }else {
                return ApiResponse.ofMessage(response.statusCode,response.getInfo());
            }

        } catch (QiniuException e){
            Response response = e.response;
            try {
                return ApiResponse.ofMessage(response.statusCode,response.bodyString());
            } catch (QiniuException ex) {
                return  ApiResponse.ofStatus(ApiResponse.Status.INTERNAL_SERVER_ERROR);
            }
        }
        catch (IOException e) {
            return  ApiResponse.ofStatus(ApiResponse.Status.INTERNAL_SERVER_ERROR);
        }
//        File target = new File("E:\\xunwu-project\\temp\\"+fileName);
//        try {
//            file.transferTo(target);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ApiResponse.ofStatus(ApiResponse.Status.INTERNAL_SERVER_ERRPR);
//        }
//        return ApiResponse.ofSuccess(null);
    }

}
