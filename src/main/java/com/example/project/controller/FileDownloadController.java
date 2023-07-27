package com.example.project.controller;

import com.example.project.filepath;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.*;

@RestController
public class FileDownloadController {

    @Autowired
    private HttpServletResponse response;

    @GetMapping("/downloadData")
    public void downloadFile(@RequestParam("key")String fileKey){
        String path = filepath.path+fileKey;
        File file = new File(path);
        if(file.exists()){
            String fileName ="dataSet";
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
            download(response,file);
        }
    }

    public void download(HttpServletResponse response,File file){
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;

        try {
            os = response.getOutputStream();
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            byte[] buffer = new byte[1024];
            int len = 0;
            while((len = fis.read(buffer)) != -1){
                os.write(buffer, 0, len);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            bis.close();
            fis.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
