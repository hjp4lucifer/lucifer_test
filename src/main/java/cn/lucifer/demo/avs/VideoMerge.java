package cn.lucifer.demo.avs;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Comparator;

public class VideoMerge {

    private int beginIndex;
    private int endIndex;

    public VideoMerge(int beginIndex, int endIndex) {
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
    }

    public String generateVideoAvs(String folderName, String suffix) {
        StrBuilder builder = new StrBuilder();
        builder.append("LoadPlugin(\"C:\\lucifer_soft\\MeGUI-2913\\tools\\lsmash\\LSMASHSource.dll\")").append('\n');

        File[] videoFileList = getVideoFiles(folderName, suffix);

        StrBuilder videoMethod = new StrBuilder("video=");
        int endIndex = getEndIndex(videoFileList);
        for (int i = beginIndex; i <= endIndex; i++) {
            File videoFile = videoFileList[i];
            String paramName = "v" + i;
            builder.append(String.format("%s=LWLibavVideoSource(\"%s\")", paramName, videoFile.getPath())).append('\n');
            videoMethod.append(paramName).append("+");

        }
        builder.append(videoMethod.substring(0, videoMethod.size() - 1)).append('\n');
        builder.append("return video");

        return builder.toString();
    }

    protected File[] getVideoFiles(String folderName, String suffix) {
        File folder = new File(folderName);
        File[] videoFileList = folder.listFiles((dir, name) -> name.endsWith(suffix));

        if (null == videoFileList || 0 == videoFileList.length) {
            throw new RuntimeException("该目录没有匹配的视频文件, folderName=" + folderName);
        }

        Arrays.sort(videoFileList, (o1, o2) -> {
            String fn1 = StringUtils.removeEnd(o1.getName(), suffix);
            String fn2 = StringUtils.removeEnd(o2.getName(), suffix);
            return Integer.parseInt(fn1) - Integer.parseInt(fn2);
        });
        return videoFileList;
    }

    public String generateAudioAvs(String folderName, String suffix) {
        StrBuilder builder = new StrBuilder();
        builder.append("LoadPlugin(\"C:\\lucifer_soft\\MeGUI\\tools\\ffms\\ffms2.dll\")").append('\n');

        File[] videoFileList = getVideoFiles(folderName, suffix);

        StrBuilder videoMethod = new StrBuilder("audio=");
        int endIndex = getEndIndex(videoFileList);
        for (int i = beginIndex; i <= endIndex; i++) {
            File videoFile = videoFileList[i];
            String paramName = "a" + i;
            builder.append(String.format("%s=FFAudioSource(\"%s\", track=1)", paramName, videoFile.getPath())).append('\n');
            videoMethod.append(paramName).append("+");
        }
        builder.append(videoMethod.substring(0, videoMethod.size() - 1)).append('\n');
        builder.append("return audio");

        return builder.toString();
    }


    private int getEndIndex(File[] videoFileList) {
        return Math.min(this.endIndex, videoFileList.length - 1);
    }

}
