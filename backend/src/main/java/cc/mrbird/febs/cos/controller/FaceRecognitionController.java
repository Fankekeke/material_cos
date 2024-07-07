package cc.mrbird.febs.cos.controller;

import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.utils.FileUtil;
import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.StudentInfo;
import cc.mrbird.febs.cos.service.FaceRecognition;
import cc.mrbird.febs.cos.service.IStudentInfoService;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.service.LoginLogService;
import cc.mrbird.febs.system.service.UserService;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 人脸识别
 */
@RestController
@RequestMapping("/cos/face")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FaceRecognitionController {

    private final FaceRecognition faceRecognition;

    private final UserService userService;

    private final IStudentInfoService studentInfoService;

    private final LoginLogService loginLogService;

    /**
     * 人脸注册
     *
     * @param file 图片
     * @param name 名称
     * @return
     */
    @PostMapping("/registered")
    public R registered(@RequestParam("avatar") MultipartFile file, @RequestParam("name") String name, @RequestParam("userId") Integer userId) throws IOException {
        BASE64Encoder base64Encoder = new BASE64Encoder();
        String base64EncoderImg = base64Encoder.encode(file.getBytes());
        String result = faceRecognition.registered(base64EncoderImg, name);
        if ("success".equals(result)) {
            String localPath = "G:/Project/仓储管理系统/db";
            String fileName = file.getOriginalFilename();
            String newFileName = FileUtil.upload(file, localPath, fileName);
            studentInfoService.update(Wrappers.<StudentInfo>lambdaUpdate().set(StudentInfo::getImages, newFileName).eq(StudentInfo::getId, userId));
        }
        return R.ok(result);
    }

    /**
     * 人脸搜索
     *
     * @param file 图片
     * @return
     */
    @PostMapping("/verification")
    public FebsResponse verification(@RequestParam("file") String file, HttpServletRequest request) throws Exception {
//        BASE64Encoder base64Encoder =new BASE64Encoder();
//        String base64EncoderImg = base64Encoder.encode(file.getBytes());

        String result = faceRecognition.verification(file);
        System.out.println(result);
        if ("error".equals(result)) {
            return new FebsResponse().message("人脸识别未通过");
        } else {
            // 获取用户
            StudentInfo studentInfo = studentInfoService.getOne(Wrappers.<StudentInfo>lambdaQuery().eq(StudentInfo::getName, result));
            if (studentInfo == null) {
                return new FebsResponse().message("人脸不匹配");
            }

            User user = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUserId, studentInfo.getUserId()));
            if (user == null) {
                return new FebsResponse().message("人脸不匹配");
            }
            return loginLogService.faceLogin(user, request);
        }
    }

    /**
     * test
     *
     * @param text
     * @return
     */
    @PostMapping("/sendFile")
    public R test(String text) throws IOException {
        BASE64Encoder base64Encoder = new BASE64Encoder();
        String fileList = JSONUtil.parseObj(text).get("fileName").toString();
        String path = JSONUtil.parseObj(text).get("path").toString();
        // 识别人脸信息
        for (String s : fileList.split(",")) {
            System.out.println("=====>" + s);
            if (".jpg".equals(s.substring(s.length() - 4))) {
                System.out.println(path + s);
                File file = new File("D:/saber/new.jpg");
                FileInputStream inputStream = new FileInputStream(file);
                MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(),
                        ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
                String result = faceRecognition.verification(base64Encoder.encode(multipartFile.getBytes()));
                if (!"error".equals(result)) {
                    System.out.println("识别成功=====>   " + result);
                    return R.ok(result);
                }
            }
        }
        return R.ok(true);
    }

    /**
     * 人脸检测
     *
     * @param img 图片Base64码
     */
    @RequestMapping("/faceDetection")
    public R faceDetection(String img) {
        return R.ok(faceRecognition.faceDetection(img));
    }
}
