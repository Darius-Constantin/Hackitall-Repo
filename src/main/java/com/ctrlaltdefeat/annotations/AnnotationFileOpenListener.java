package com.ctrlaltdefeat.annotations;

import com.ctrlaltdefeat.annotations.dataContainers.AnnotationData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class AnnotationFileOpenListener {
    private static final String NOTES_FILE = ".idea/NOTES.json";

    public AnnotationFileOpenListener(Project project) {
        EditorFactory.getInstance().addEditorFactoryListener(new EditorFactoryListener() {
            @Override
            public void editorCreated(@NotNull EditorFactoryEvent event) {
                VirtualFile file = event.getEditor().getVirtualFile();;
                if (file != null) {
                    System.out.println(file.getPath());
                    if (!AnnotationStorage.isLoaded()) {
                        try (FileReader reader = new FileReader(project.getBasePath() + "/" + NOTES_FILE,
                                StandardCharsets.UTF_8)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<HashMap<String,
                                    ArrayList<AnnotationData.SerializableAnnotation>>>(){}.getType();
                            HashMap<String, ArrayList<AnnotationData.SerializableAnnotation>> annotations =
                                    gson.fromJson(reader, type);
                            AnnotationStorage.setAnnotationDataMap(annotations);
                        } catch (IOException ignored) {}
                        AnnotationStorage.setLoaded(true);
                    }
                    AnnotationStorage
                            .importAnnotationsForFile(AnnotationStorage
                                    .getRelativePath(file, project), event.getEditor());
                }
            }

            @Override
            public void editorReleased(@NotNull EditorFactoryEvent event) {
                AnnotationStorage
                        .deportAnnotationsForFile(AnnotationStorage
                                .getRelativePath(event.getEditor().getVirtualFile(), project));
            }
        }, project);
    }
}
