package cn.lucifer.demo.avs;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class VideoMergeTest {

    private final static String suffix = ".ts";

    private String folderName = "E:\\百家讲坛·国史通鉴\\04 两晋南北朝篇\\20170109 国史通鉴•两晋南北朝篇（19）南朝旧事";

    private VideoMerge videoMerge = new VideoMerge(0, 30);

    @Test
    public void getVideoFiles() throws Exception {
        String videoName = StringUtils.substringAfterLast(folderName, "\\");
        String cmd = String.format("copy /b \"%s\\*.ts\" \"%s\\%s.ts\"", folderName, new File(folderName).getParent(), videoName);
        System.out.println(cmd);

        File[] videoFiles = videoMerge.getVideoFiles(folderName, suffix);
        StrBuilder builder = new StrBuilder();
        for (int i = 0; i < videoFiles.length; i++) {
            File file = videoFiles[i];
            file.renameTo(new File(file.getParentFile(), String.format("%03d.ts", i)));
//            System.out.println(String.format("%03d.ts",i));
        }
//        System.out.println(builder.toString());
//        Process ps = Runtime.getRuntime().exec("cmd /C " + cmd);
//        ps.waitFor();
//        System.out.println("finished cmd");
    }
}