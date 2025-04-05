package com.ctrlaltdefeat.annotations.dataContainers;

import com.ctrlaltdefeat.annotations.actions.AddAnnotationAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.*;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.ArrayList;

public class AnnotationData {
    public static class SerializableAnnotation {
        @Getter @Setter
        protected String note;
        @Getter
        protected int startingOffset;
        @Getter
        protected int endOffset;
        @Getter
        protected int r;
        @Getter
        protected int g;
        @Getter
        protected int b;

        public SerializableAnnotation(final String note, final int startingOffset,
                                      final int endOffset, final Color color) {
            this.note = note;
            this.startingOffset = startingOffset;
            this.endOffset = endOffset;
            this.r = color.getRed();
            this.g = color.getGreen();
            this.b = color.getBlue();
        }
    }

    @Getter @Setter
    private ArrayList<AnnotationData> container;
    @Getter @Setter
    private ArrayList<AnnotationData.SerializableAnnotation> dataContainer;
    @Getter
    protected SerializableAnnotation annotation;
    @Getter
    protected RangeHighlighter highlighter;

    public AnnotationData(final SerializableAnnotation annotation, final Editor editor) {
        this.annotation = annotation;
        MarkupModel markupModel = editor.getMarkupModel();
        TextAttributes attributes = new TextAttributes();
        attributes.setBackgroundColor(new Color(annotation.getR(), annotation.getG(),
                annotation.getB(), AddAnnotationAction.DEFAULT_OPACITY));
        highlighter = markupModel.addRangeHighlighter(
                annotation.getStartingOffset(),
                annotation.getEndOffset(),
                HighlighterLayer.SELECTION - 1,
                attributes,
                HighlighterTargetArea.EXACT_RANGE
        );
    }

    public AnnotationData(final int startOffset, final int endOffset, final String note,
                          final Color color, final Editor editor) {
        MarkupModel markupModel = editor.getMarkupModel();
        TextAttributes attributes = new TextAttributes();
        attributes.setBackgroundColor(color);
        highlighter = markupModel.addRangeHighlighter(
                startOffset,
                endOffset,
                HighlighterLayer.SELECTION - 1,
                attributes,
                HighlighterTargetArea.EXACT_RANGE
        );

        annotation = new SerializableAnnotation(note, startOffset, endOffset, color);
    }

    public void remove() {
        highlighter.dispose();
        container.remove(this);
        dataContainer.remove(this.annotation);
    }
}