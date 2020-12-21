package cn.lucifer.demo.avs;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Comparator;

public class VideoMerge {

    public String generateVideoAvs(String folderName, final String suffix) {
        StrBuilder builder = new StrBuilder();
        builder.append("LoadPlugin(\"C:\\lucifer_soft\\MeGUI\\tools\\lsmash\\LSMASHSource.dll\")").append('\n');

        File folder = new File(folderName);
        File[] videoFileList = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(suffix);
            }
        });

        if (null == videoFileList || 0 == videoFileList.length) {
            throw new RuntimeException("该目录没有匹配的视频文件, folderName=" + folderName);
        }

        Arrays.sort(videoFileList, new Comparator<File>() {

            @Override
            public int compare(File o1, File o2) {
                String fn1 = StringUtils.removeEnd(o1.getName(), suffix);
                String fn2 = StringUtils.removeEnd(o2.getName(), suffix);
                return Integer.parseInt(fn1) - Integer.parseInt(fn2);
            }
        });

        StrBuilder videoMethod = new StrBuilder("video=");
        for (int i = 0; i < videoFileList.length; i++) {
            File videoFile = videoFileList[i];
            String paramName = "v" + i;
            builder.append(String.format("%s=LWLibavVideoSource(\"%s\")", paramName, videoFile.getPath())).append('\n');
            videoMethod.append(paramName).append("+");

        }
        builder.append(videoMethod.substring(0, videoMethod.size() - 1)).append('\n');
        builder.append("return video");

        return builder.toString();
    }

}
