// package com.example.service;

// import com.example.model.TagData;
// import org.springframework.stereotype.Service;

// import java.util.*;

// @Service
// public class FileParserService {

//     public List<TagData> parseCusdecContent(String content) {
//         return parseContent(content, true);
//     }

//     public List<TagData> parseCusresContent(String content) {
//         return parseContent(content, false);
//     }

//     private List<TagData> parseContent(String content, boolean isCusdec) {
//         List<TagData> tagDataList = new ArrayList<>();
//         String pattern = "(\\w{3})\\+(.*?)'";
//         content.lines().forEach(line -> {
//             if (line.matches(pattern)) {
//                 String tag = line.split("\\+")[0];
//                 String data = line.split("\\+")[1].replace("'", "");
//                 tagDataList.add(new TagData(tag, isCusdec ? data : "N/A", isCusdec ? "N/A" : data));
//             }
//         });
//         return tagDataList;
//     }

//     public List<TagData> mergeDataWithDuplicates(List<TagData> cusdecData, List<TagData> cusresData) {
//         Map<String, List<TagData>> mergedMap = new LinkedHashMap<>();

//         cusdecData.forEach(data -> mergedMap.computeIfAbsent(data.getTag(), k -> new ArrayList<>()).add(data));
//         cusresData.forEach(data -> mergedMap.computeIfAbsent(data.getTag(), k -> new ArrayList<>()).add(data));

//         List<TagData> mergedData = new ArrayList<>();
//         mergedMap.values().forEach(mergedData::addAll);
//         return mergedData;
//     }
// }
