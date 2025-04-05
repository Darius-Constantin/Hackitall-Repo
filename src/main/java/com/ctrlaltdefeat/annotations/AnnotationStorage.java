package com.ctrlaltdefeat.annotations;

import com.ctrlaltdefeat.annotations.actions.AddAnnotationAction;
import com.ctrlaltdefeat.annotations.dataContainers.AnnotationData;
import com.ctrlaltdefeat.annotations.ui.AnnotationToolWindowFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import genericUI.ColorAdapter;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

public class AnnotationStorage {
    @Getter @Setter
    private static boolean loaded = false;

    private static final Gson gson =
            new GsonBuilder().registerTypeAdapter(Color.class, new ColorAdapter())
                    .setPrettyPrinting().create();

    private static final Map<String, ArrayList<AnnotationData.SerializableAnnotation>>
            annotationDataMap = new HashMap<>();

    public static void setAnnotationDataMap(Map<String,
            ArrayList<AnnotationData.SerializableAnnotation>> map) {
        annotationDataMap.clear();
        annotationDataMap.putAll(map);
    }

    private static final Map<String, ArrayList<AnnotationData>> annotationMap = new HashMap<>();

    @Getter
    private static AnnotationData currentAnnotation = null;

    private static AnnotationToolWindowFactory.AnnotationToolWindowContent windowContent;

    public static void setWindowContent(AnnotationToolWindowFactory
                                         .AnnotationToolWindowContent windowContent) {
        AnnotationStorage.windowContent = windowContent;
    }

    public static void setCurrentAnnotation(AnnotationData currentAnnotation) {
        AnnotationStorage.currentAnnotation = currentAnnotation;
        windowContent.update();
    }

    public static AnnotationData createAnnotation(String filePath, AnnotationData annotation) {
        ArrayList<AnnotationData> container = annotationMap.computeIfAbsent(filePath,
                k -> new ArrayList<>());
        ArrayList<AnnotationData.SerializableAnnotation> dataContainer =
                annotationDataMap.computeIfAbsent(filePath, k -> new ArrayList<>());
        annotation.setContainer(container);
        annotation.setDataContainer(dataContainer);

        container.add(annotation);
        dataContainer.add(annotation.getAnnotation());

        return annotation;
    }

    public static AnnotationData addAnnotation(String filePath, AnnotationData annotation) {
        ArrayList<AnnotationData> container = annotationMap.computeIfAbsent(filePath,
                k -> new ArrayList<>());
        ArrayList<AnnotationData.SerializableAnnotation> dataContainer =
                annotationDataMap.computeIfAbsent(filePath, k -> new ArrayList<>());
        annotation.setContainer(container);
        annotation.setDataContainer(dataContainer);

        container.add(annotation);

        return annotation;
    }

    public static String getRelativePath(VirtualFile vFile, Project project) {
        return vFile.getPath()
                .replaceFirst(Pattern.quote(project.getBasePath() + "/"),
                        "");
    }

    public static void saveNotes(Project project) {
        String basePath = project.getBasePath();
        if (basePath == null) return;

        File file = new File(basePath, ".idea/NOTES.json");

        HashMap<String, ArrayList<AnnotationData.SerializableAnnotation>> map = new HashMap<>();
        for (Map.Entry<String, ArrayList<AnnotationData.SerializableAnnotation>> entry : annotationDataMap.entrySet()) {
            map.put(entry.getKey(), new ArrayList<>());
            for (AnnotationData.SerializableAnnotation annotation : entry.getValue()) {
                map.get(entry.getKey()).add(annotation);
            }
        }

        try (Writer writer = new FileWriter(file)) {
            gson.toJson(map, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public static void importAnnotationsForFile(String filePath, Editor editor) {
        ArrayList<AnnotationData.SerializableAnnotation> list = annotationDataMap.get(filePath);
        if (list != null) {
            for (AnnotationData.SerializableAnnotation annotation : list) {
                addAnnotation(filePath, new AnnotationData(annotation, editor));
            }
        }
    }

    public static void deportAnnotationsForFile(String filePath) {
        ArrayList<AnnotationData> list = annotationMap.get(filePath);
        if (list != null) {
            for (AnnotationData annotation : list) {
                annotation.getHighlighter().dispose();
            }
            list.clear();
        }
        annotationMap.remove(filePath);
    }
}