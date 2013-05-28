package com.jantox.siege.models;

import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Entity;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import static org.lwjgl.opengl.GL11.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class ObjectLoader {

    public ObjectLoader() {

    }

    public int loadOBJModel(String filename) {
        System.out.println("Loading model from file " + filename + "...");

        InputStream fis = null;
        BufferedReader br = null;
        String line = "";

        ArrayList<String> defs = new ArrayList<String>();
        ArrayList<Material> mats = null;

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
        ArrayList<Vector3D> normals = new ArrayList<Vector3D>();
        ArrayList<Vector3D> textures = new ArrayList<Vector3D>();

        int curmat = -1;

        for(int i = 0; i < defs.size(); i++) {
            String data = defs.get(i);
            String[] params = data.split(" ");
            if(data.startsWith("v ")) {
                float x = Float.valueOf(params[1]);
                float y = Float.valueOf(params[2]);
                float z = Float.valueOf(params[3]);
                vertexes.add(new Vector3D(x, y, z));
            } else if(data.startsWith("vn ")) {
                float x = Float.valueOf(params[1]);
                float y = Float.valueOf(params[2]);
                float z = Float.valueOf(params[3]);
                normals.add(new Vector3D(x, y, z));
            } else if(data.startsWith("vt ")) {
                float u = Float.valueOf(params[1]);
                float v = Float.valueOf(params[2]);

                textures.add(new Vector3D(u, v, 0));
            } else if(data.startsWith("f ")) {
                int vcount = params.length - 1;
                Vector3D[] facevtxes = new Vector3D[vcount];
                Vector3D[] facetxtes = null;

                // check for normals, texture cords, or both
                //if(data.matches("f [0-9] [0-9] [0-9]") || data.matches("f [0-9] [0-9] [0-9] [0-9]")) {
                for(int f = 0; f < vcount; f++) {
                    if(data.contains("/")) {
                        facevtxes[f] = vertexes.get(Integer.valueOf(params[f+1].split("/")[0])-1);
                    } else {
                        facevtxes[f] = vertexes.get(Integer.valueOf(params[f+1])-1);
                    }
                }

                if(data.contains("/")) { // preliminary check for textures
                    facetxtes = new Vector3D[vcount];
                    for(int j = 1; j < params.length; j++) {
                        int tid = Integer.valueOf(params[j].split("/")[1]);
                        facetxtes[j-1] = textures.get(tid-1);
                    }
                }

                faces.add(new ModelFace(facevtxes, facetxtes, curmat));
            } else if(data.startsWith("mtllib ")) {
                mats = this.loadMaterialLibrary(params[1]);
            } else if(data.startsWith("usemtl ")) {
                if(params.length > 1)
                    curmat = this.getMaterialID(mats, params[1]);
            }
        }

        int num = glGenLists(1);
        glNewList(num, GL_COMPILE);
        for(int i = 0; i < faces.size(); i++) {
            ModelFace f = faces.get(i);
            Material m = null;
            if(f.getMaterial() != -1) {
                m = mats.get(f.getMaterial());
                glColor3f((float)m.diffuse.x, (float)m.diffuse.y, (float)m.diffuse.z);

                if(m.isTextured()) {
                    glEnable(GL_TEXTURE_2D);
                    glBindTexture(GL_TEXTURE_2D, m.texture.getTextureID());
                } else {
                    glDisable(GL_TEXTURE_2D);
                }
            }
            if(f.getVertexAmount() == 4) {
                glBegin(GL_QUADS);

                for(int v = 0; v < f.getVertexAmount(); v++) {
                    Vector3D vertex = f.getVertex(v);
                    if(m != null)
                        if(m.isTextured()) {
                            Vector3D tcoord = f.getTextureCoordinate(v);
                            glTexCoord2f((float)tcoord.x, (float)tcoord.y);
                        }
                    glVertex3f((float) vertex.x,(float) vertex.y,(float) vertex.z);
                }

                glEnd();
            } else if(f.getVertexAmount() == 3) {
                glBegin(GL_TRIANGLES);

                for(int v = 0; v < f.getVertexAmount(); v++) {
                    Vector3D vertex = f.getVertex(v);
                    if(m != null)
                        if(m.isTextured()) {
                            Vector3D tcoord = f.getTextureCoordinate(v);
                            glTexCoord2f((float)tcoord.x, (float)tcoord.y);
                        }
                    glVertex3f((float) vertex.x,(float) vertex.y,(float) vertex.z);
                }

                glEnd();
            }
        }

        glEndList();

        textures.clear();
        faces.clear();
        normals.clear();
        vertexes.clear();
        defs.clear();

        return num;
    }

    public ArrayList<Material> loadMaterialLibrary(String mtllib) {
        ArrayList<Material> materials = new ArrayList<Material>();
        ArrayList<String> defs = new ArrayList<String>();

        FileInputStream fis = null;
        BufferedReader br = null;
        String line = "";

        try {
            fis = new FileInputStream("models/" + mtllib);
            br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
            while ((line = br.readLine()) != null) {
                defs.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String mtlname = "";
        Vector3D ka = null, kd = null, ks = null;
        Texture txture = null;
        for(int i = 0; i < defs.size(); i++) {
            String l = defs.get(i);
            String[] params = l.split(" ");

            if(l.startsWith("Ka ")) {
                ka = new Vector3D(Float.valueOf(params[1]), Float.valueOf(params[2]), Float.valueOf(params[3]));
            } else if(l.startsWith("Kd ")) {
                kd = new Vector3D(Float.valueOf(params[1]), Float.valueOf(params[2]), Float.valueOf(params[3]));
            } else if(l.startsWith("Ks ")) {
                ks = new Vector3D(Float.valueOf(params[1]), Float.valueOf(params[2]), Float.valueOf(params[3]));
            } else if(l.startsWith("map_Kd ")) {
                try {
                    txture = TextureLoader.getTexture("BMP", new FileInputStream(new File("textures/" + params[1])), GL11.GL_NEAREST);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(l.startsWith("newmtl ")) {
                if(!mtlname.equals("")) { // first material
                    materials.add(new Material(mtlname, ka, kd, ks, txture));
                    txture = null;
                }
                if(params.length > 1)
                    mtlname = params[1];
            }
        }
        materials.add(new Material(mtlname, ka, kd, ks, txture));

        return materials;
    }

    public int getMaterialID(ArrayList<Material> mats, String s) {
        for(int i = 0; i < mats.size(); i++) {
            if(mats.get(i).mtlname.equals(s)) {
                return i;
            }
        }
        return -1;
    }

}
