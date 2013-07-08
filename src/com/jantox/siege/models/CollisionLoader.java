package com.jantox.siege.models;

import com.jantox.siege.Matrix4;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.geometry.Quad;
import com.jantox.siege.level.Level;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import static org.lwjgl.opengl.GL11.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class CollisionLoader {

    public CollisionLoader() {

    }

    public MapCollision loadOBJModel(String filename, Matrix4 trans, Matrix4 rot, Matrix4 scale) {
        System.out.println("Loading collisions from file " + filename + "...");

        InputStream fis = null;
        BufferedReader br = null;
        String line = "";

        ArrayList<String> defs = new ArrayList<String>();

        MapCollision mc = new MapCollision();

        try {
            fis = new FileInputStream(filename);
            br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
            while ((line = br.readLine()) != null) {
                defs.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<ModelFace> faces = new ArrayList<ModelFace>();
        ArrayList<Vector3D> vertexes = new ArrayList<Vector3D>();

        for(int i = 0; i < defs.size(); i++) {
            String data = defs.get(i);
            String[] params = data.split(" ");
            if(data.startsWith("v ")) {
                float x = Float.valueOf(params[1]);
                float y = Float.valueOf(params[2]);
                float z = Float.valueOf(params[3]);
                new Vector3D(x,y,z).debug();

                vertexes.add(new Vector3D(x, y, z));
            } else if(data.startsWith("f ")) {
                int vcount = params.length - 1;
                Vector3D[] facevtxes = new Vector3D[vcount];
                Vector3D[] facetxtes = null;
                for(int f = 0; f < vcount; f++) {
                    if(data.contains("/")) {
                        facevtxes[f] = vertexes.get(Integer.valueOf(params[f+1].split("/")[0])-1);
                    } else {
                        facevtxes[f] = vertexes.get(Integer.valueOf(params[f+1])-1);
                    }
                }

                faces.add(new ModelFace(facevtxes, facetxtes,-1));
            }
        }

        for(int i = 0; i < faces.size(); i++) {
            ModelFace f = faces.get(i);

            Vector3D a = null, b = null, c = null, d = null;

            for(int v = 0; v < f.getVertexAmount(); v++) {
                Vector3D vertex = f.getVertex(v).copy();

                // apply scale, then rotation, then translate
                //vertex = scale.multiplyVectorBy(vertex);
                System.out.println("yo1");
                vertex.debug();
                vertex = scale.multiplyVectorBy(vertex);
                vertex = rot.multiplyVectorBy(vertex);
                vertex = trans.multiplyVectorBy(vertex);

                if(v == 0) {
                    a = vertex;
                } else if(v == 1) {
                    b = vertex;
                } else if(v == 2) {
                    if(f.getVertexAmount() == 3)
                        d = vertex;
                    else
                        c = vertex;
                } else if(v == 3) {
                    d = vertex;
                }
            }

            Quad q = new Quad(a,b,c,d);
            mc.addPlane(q);
        }

        faces.clear();
        vertexes.clear();
        defs.clear();

        return mc;
    }

}
