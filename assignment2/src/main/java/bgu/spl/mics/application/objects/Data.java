package bgu.spl.mics.application.objects;

import java.util.Locale;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Data {
    /**
     * Enum representing the Data type.
     */
    enum Type {
        Images, Text, Tabular
    }

    private Type type;
    private int processed;
    private int size;

    public Data(String t, int size) {
//        this.type = Type.valueOf(type.substring(1).toUpperCase() + type.substring(1,type.length()));
        if(t.equals("Images") || t.equals("images"))
            this.type = Type.Images;
        if(t.equals("Text") || t.equals("text"))
            this.type = Type.Text;
        if(t.equals("Tabular") || t.equals("tabular"))
            this.type = Type.Tabular;
        this.processed = 0;
        this.size = size;
    }

    public int getSize() {
        return this.size;
    }

    public Type getType() {
        return this.type;
    }

    public int getProcessed() {return this.processed;}

    public void updateProcess() {
        this.processed++;
    }

    @Override
    public String toString() {return "Data : " + '\'' + "type = " + type + "," + '\'' + "processed = " + processed + "," + '\'' + "size = " + size;
    }
}
