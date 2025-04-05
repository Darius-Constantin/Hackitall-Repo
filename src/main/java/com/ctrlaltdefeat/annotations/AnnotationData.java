package com.ctrlaltdefeat.annotations;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.ArrayList;

public class AnnotationData {
    private static final Logger log = LoggerFactory.getLogger(AnnotationData.class);

    public static class AnnotationDataBuilder {
        protected String note;
        protected RangeHighlighter highlighter;
        protected ArrayList<AnnotationData> container;

        public AnnotationDataBuilder(int startOffset, int endOffset, String note, Editor editor) {
            MarkupModel markupModel = editor.getMarkupModel();
            TextAttributes attributes = new TextAttributes();
            attributes.setBackgroundColor(new Color(163, 83, 83, 23)); // Light Yellow highlight

            this.highlighter = markupModel.addRangeHighlighter(
                    startOffset,
                    endOffset,
                    HighlighterLayer.SELECTION - 1,
                    attributes,
                    HighlighterTargetArea.EXACT_RANGE
            );

            this.note = note;
        }

        public AnnotationDataBuilder setContainer(ArrayList<AnnotationData> container) {
            this.container = container;
            return this;
        }

        public AnnotationData build() {
            if (container == null) {
                throw new RuntimeException("container is null!");
            }
            return new AnnotationData(this);
        }
    }

    private final String note;
    private final RangeHighlighter highlighter;
    private final ArrayList<AnnotationData> container;

    private AnnotationData(AnnotationDataBuilder builder) {
        this.note = builder.note;
        this.highlighter = builder.highlighter;
        this.container = builder.container;
    }

    public void remove() {
        highlighter.dispose();
        container.remove(this);
    }

    public String getNote() { return note; }
    public RangeHighlighter getHighlighter() { return highlighter; }
}