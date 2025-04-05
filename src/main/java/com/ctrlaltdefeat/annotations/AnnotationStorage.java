package com.ctrlaltdefeat.annotations;

import java.util.*;

public class AnnotationStorage {
    private static final Map<String, ArrayList<AnnotationData>> annotationMap = new HashMap<>();

    public static Map<String, ArrayList<AnnotationData>> getAnnotationMap() {
        return annotationMap;
    }

    public static void buildAndAddAnnotation(String filePath, AnnotationData.AnnotationDataBuilder annotationBuilder) {
        ArrayList<AnnotationData> container = annotationMap.computeIfAbsent(filePath, k -> new ArrayList<>());
        annotationBuilder.setContainer(container);
        annotationMap.get(filePath).add(annotationBuilder.build());
    }

    public static ArrayList<AnnotationData> getAnnotations(String filePath) {
        return annotationMap.getOrDefault(filePath, new ArrayList<>());
    }

    public static void removeAnnotation(String filePath, AnnotationData annotation) {
        List<AnnotationData> annotations = annotationMap.get(filePath);
        if (annotations != null) {
            annotations.remove(annotation);
            if (annotations.isEmpty()) {
                annotationMap.remove(filePath);
            }
        }
    }
}