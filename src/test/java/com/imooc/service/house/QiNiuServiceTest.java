package com.imooc.service.house;

import com.imooc.ApplicationTests;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

public class QiNiuServiceTest extends ApplicationTests {
    @Autowired
    private QiNiuService qiNiuService;

    @Test
    public void testUploadFile(){
        String fileName = "E:\\xunwu-project\\temp\\1.jpg";
        File file = new File(fileName);
        Assert.assertTrue(file.exists());
        try {
            Response response = qiNiuService.uploadFile(file);
            Assert.assertTrue(response.isOK());
        } catch (QiniuException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void testDelete(){
        String key = "FjMULcoJ80o8ub5ZvIcObrCkZl_F";
        try {
            Response r = qiNiuService.delete(key);
            Assert.assertTrue(r.isOK());
        } catch (QiniuException e) {
            e.printStackTrace();
        }
    }
}
