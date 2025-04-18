package genericUI;

import com.ctrlaltdefeat.annotations.actions.AddAnnotationAction;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.awt.*;
import java.io.IOException;

public class ColorAdapter extends TypeAdapter<Color> {
    @Override
    public void write(JsonWriter out, Color color) throws IOException {
        if (color == null) {
            out.nullValue();
            return;
        }
        out.beginObject();
        out.name("r").value(color.getRed());
        out.name("g").value(color.getGreen());
        out.name("b").value(color.getBlue());
        out.endObject();
    }

    @Override
    public Color read(JsonReader in) throws IOException {
        int r = 0, g = 0, b = 0;
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "r" -> r = in.nextInt();
                case "g" -> g = in.nextInt();
                case "b" -> b = in.nextInt();
            }
        }
        in.endObject();
        return new Color(r, g, b, AddAnnotationAction.DEFAULT_OPACITY);
    }
}
