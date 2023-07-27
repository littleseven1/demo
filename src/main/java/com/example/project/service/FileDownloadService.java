package com.example.project.service;
import com.example.project.filepath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class FileDownloadService {
@Autowired(required = false)
    public File getfile(String fileKey)  {
    File file = new File(filepath.path+fileKey);
    return file;
}

}
