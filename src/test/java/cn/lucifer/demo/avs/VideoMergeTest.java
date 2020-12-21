package cn.lucifer.demo.avs;

import org.junit.Test;

import static org.junit.Assert.*;

public class VideoMergeTest {

    @Test
    public void generateVideoAvs() {
        VideoMerge videoMerge = new VideoMerge();
        String folderName = "E:\\百家讲坛·国史通鉴\\01 第一部\\20140103 国史通鉴（第一部）1 远古时代";
        String suffix = ".ts";
        String avsData = videoMerge.generateVideoAvs(folderName, suffix);
        System.out.println(avsData);

    }
}