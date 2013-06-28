package com.jantox.siege.models;

import com.jantox.siege.Vector3D;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class DSMLoader {

    public DSMLoader() {

    }

    public int loadDSMModel(String modelloc) {
        ArrayList<Vector3D> vertexes = new ArrayList<Vector3D>();
        ArrayList<Vector3D> faces = new ArrayList<Vector3D>();

        ByteBuffer buffer;
        try {
            buffer = loadData(modelloc);
            //buffer.flip();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

        int dsmversion = buffer.get();
        int commentlength = buffer.get();
        for(int i = 0; i < commentlength + 16; i++)
            buffer.get();

        int vertexptr = buffer.getInt();
        int faceptr = buffer.getInt();
        int partptr = buffer.getInt();

        buffer.position(vertexptr);

        while(true) {
            int check = buffer.getInt();
            if(check == 0xFFFFFFFF) {
                break;
            }

            float x = buffer.getFloat();
            float y = buffer.getFloat();
            float z = buffer.getFloat();

            vertexes.add(new Vector3D(x,y,z));
        }

        buffer.position(faceptr);
        while(true) {
            int check = buffer.getInt();
            if(check == 0xFFFFFFFF) {
                break;
            }
            buffer.position(buffer.position()-4);

            float x = buffer.getFloat();
            float y = buffer.getFloat();
            float z = buffer.getFloat();

            System.out.println(x + " " + y + " " + z);

            faces.add(new Vector3D(x,y,z));
        }

        int mid = glGenLists(1);
        glNewList(mid, GL_COMPILE);
        glColor3f(1,0,0);
        glBegin(GL_TRIANGLES);
        for(int i = 0; i < faces.size(); i++) {
            Vector3D f = faces.get(i);

            Vector3D a = vertexes.get((int)f.x);
            Vector3D b = vertexes.get((int)f.x);
            Vector3D c = vertexes.get((int)f.x);

            glVertex3f((float)a.x, (float)a.y, (float)a.z);
            glVertex3f((float)b.x, (float)b.y, (float)b.z);
            glVertex3f((float)c.x, (float)c.y, (float)c.z);
        }
        glEnd();

        glEndList();

        return mid;
    }

    public ByteBuffer loadData(String name) throws IOException {
        File file=new File(name);
        int size = (int) file.length();
        byte[] contents=new byte[size];
        FileInputStream in=new FileInputStream(file);
        in.read(contents);
        in.close();

        ByteBuffer bb = ByteBuffer.allocate(size);
        bb.put(contents);

        bb.flip();

        return bb;
    }

}
