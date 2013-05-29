package com.jantox.siege.geometry;

import com.jantox.siege.Vector3D;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

public class Lighting {

    public static final float LIGHTMAP_SIZE = 16f;

    private static int lightmap = -1;

    public static int generateLightmap(Vector3D light) {
        if(lightmap == -1) {
            lightmap = glGenTextures();
        }

        int[] pixels = new int[(int) (LIGHTMAP_SIZE * LIGHTMAP_SIZE)];

        Vector3D lpos = new Vector3D();
        float step = 1.0f / LIGHTMAP_SIZE;
        float xl = 0, yl = 0;

        for(int i = 0; i < LIGHTMAP_SIZE; i++) {
            for(int j = 0; j < LIGHTMAP_SIZE; j++) {
                lpos.x += step;

                Vector3D wpl = lpos.copy();
                wpl.subtract(light);

                double d = wpl.dotProduct(wpl) * 0.5;
                if(d < 1)
                    d = 1;

                float cval = 1f / (float)d;
                pixels[j * (int) LIGHTMAP_SIZE + i] = new Color((int) (255 * cval * 1),(int) (255 * cval * 1),(int) (255 * cval * 1)).getRGB();
            }
            lpos.y += step;
        }

        ByteBuffer buffer = BufferUtils.createByteBuffer((int) (LIGHTMAP_SIZE * LIGHTMAP_SIZE) * 4);

        for(int y = 0; y < LIGHTMAP_SIZE; y++){
            for(int x = 0; x < LIGHTMAP_SIZE; x++){
                int pixel = pixels[y * (int)LIGHTMAP_SIZE + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }

        buffer.flip();

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, (int)LIGHTMAP_SIZE, (int)LIGHTMAP_SIZE, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        return lightmap;
    }

}
