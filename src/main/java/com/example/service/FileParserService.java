package com.example.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.example.model.TagData;

@Service
public class FileParserService {

    public List<TagData> parseCusdecContent(String content) {
        List<TagData> tagDataList = new ArrayList<>();

        // Define the pattern to match tags and their data
        Pattern pattern = Pattern.compile("(\\w{3})\\+(.*?)'");
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            TagData tagData = new TagData(matcher.group(1), matcher.group(2), "N/A");
            tagDataList.add(tagData);
        }
        return tagDataList;
    }

    public List<TagData> parseCusresContent(String content) {
        List<TagData> tagDataList = new ArrayList<>();

        // Define the pattern to match tags and their data
        Pattern pattern = Pattern.compile("(\\w{3})\\+(.*?)'");
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            TagData tagData = new TagData(matcher.group(1), "N/A", matcher.group(2));
            tagDataList.add(tagData);
        }

        return tagDataList;
    }

    // private List<TagData> parseContent(String content, boolean isCusdec) {
    //     List<TagData> tagDataList = new ArrayList<>();
    //     String pattern = "(\\w{3})\\+(.*?)'";
    //     content.lines().forEach(line -> {
    //         if (line.matches(pattern)) {
    //             String tag = line.split("\\+")[0];
    //             String data = line.split("\\+")[1].replace("'", "");
    //             tagDataList.add(new TagData(tag, isCusdec ? data : "N/A", isCusdec ? "N/A" : data));
    //         }
    //     });
    //     return tagDataList;
    // }

    // public List<TagData> mergeDataWithDuplicates(List<TagData> cusdecData, List<TagData> cusresData) {
    //     Map<String, List<TagData>> mergedMap = new LinkedHashMap<>();

    //     cusdecData.forEach(data -> mergedMap.computeIfAbsent(data.getTag(), k -> new ArrayList<>()).add(data));
    //     cusresData.forEach(data -> mergedMap.computeIfAbsent(data.getTag(), k -> new ArrayList<>()).add(data));

    //     List<TagData> mergedData = new ArrayList<>();
    //     mergedMap.values().forEach(mergedData::addAll);
    //     return mergedData;
    // }
}
