package com.file.demo.controller;

import com.fasterxml.jackson.databind.util.ClassUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UploadController {

    @RequestMapping(value = "/uploadPage",method = RequestMethod.GET)
    public String uploadPage(){
        return "upload";
    }

    @RequestMapping(value= "/success",method = RequestMethod.GET)
    public String successPage(){
        return "success";
    }

    @RequestMapping(value="/uploadText",method = RequestMethod.GET)
    public String uploadTextPage(){
        return "uploadText";
    }

    @RequestMapping(value="/fail",method = RequestMethod.GET)
    public String failPage(){
        return "fail";
    }

    @RequestMapping(value="/videoPage/{videoName}")
    public String video(@PathVariable("videoName")String fileName,Model model){
        model.addAttribute("videoName",fileName);
        return "videoPage";
    }

//    @RequestMapping(value = "/videoPage")
//    public String videoPage(){
//        return "/video";
//    }

    @RequestMapping(value="/videoList",method = RequestMethod.GET)
    public String videoList(Model model){
        String path = "D:/localVideo/";
        File videoDir = new File(path);
        File []videos = videoDir.listFiles();
        List<String> fileList = new ArrayList<String>();
        for(File videoFile:videos){
            if(videoFile.getName().contains(" ") || videoFile.getName().contains("[")){
                String newName = videoFile.getAbsolutePath().replace(" ","");
                newName = newName.replace("[","");
                newName = newName.replace("]","");
                File newFile = new File(newName);
                videoFile.renameTo(newFile);
                videoFile = newFile;
            }
            fileList.add(videoFile.getName());
        }
        model.addAttribute("list",fileList);
        return "videoList";
    }

    @RequestMapping(value="/uploadContent",method = RequestMethod.POST)
    public String uploadContent(String text){
        Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable trans = new StringSelection(text);
        clipBoard.setContents(trans,null);

//        File file = new File("D:\\localUpload\\content.txt");
//        BufferedWriter bufferedWriter = null;
//        try {
//            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
//            bufferedWriter.write(text);
//        }catch(FileNotFoundException e){
//            e.printStackTrace();
//            return "redirect:/fail";
//        }catch(IOException e){
//            e.printStackTrace();
//            return "redirect:/fail";
//        }finally{
//            if(bufferedWriter != null){
//                try {
//                    bufferedWriter.close();
//                }catch(IOException e){
//                    e.printStackTrace();
//                    return "redirect:/fail";
//                }
//            }
//        }
//        return "redirect:/success";
        return "redirect:/uploadText";
    }

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public String upload(MultipartFile file){
        String fileName = file.getOriginalFilename();
        String filePath = "D:\\localUpload\\";
        File dest = new File(filePath+fileName);
        try {
            file.transferTo(dest);
            return "redirect:/success"; //不使用redirect会报错，因为html不支持响应头带有post的应答包
        }catch (IOException e){
            e.printStackTrace();
        }
        return "redirect:/fail";
    }
}
