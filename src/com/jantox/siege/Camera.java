package com.jantox.siege;

import com.jantox.siege.entities.Catapult;
import com.jantox.siege.entities.Entity;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

public class Camera {

    public static float MOUSE_SPEED = 0.15f;
    public static float MOVE_SPEED = 0.4f;

    private int width, height;

    public Vector3D camera;
    private float pitch, yaw; // stored in degrees

    private Entity focus = null;
    private boolean third = false;
    private float thirdangle = 0;

    private float delta;

    private boolean running;
    int perspective = 68;

    private float pitchRecoil = 0;
    private int pdir = 2;

    public Camera(Vector3D cam, int width, int height) {
        this.camera = cam.copy();
        this.width = width;
        this.height = height;

        pitch = 0;
        yaw = 0;
    }

    public void setFocus(Entity e) {
        third = true;
        this.focus = e;
    }

    public void update(float delta) {
        if(third) {
            yaw += MOUSE_SPEED * ((width / 2) - Input.getMouseX());
            Input.setMouse(width / 2, height / 2);

            Vector3D epos = focus.getPosition().copy();
            Vector3D move = new Vector3D(Math.cos(Math.toRadians(-yaw+90)) * 15, 8, Math.sin(Math.toRadians(-yaw+90)) * 15);
            epos.add(move);

            this.camera = epos.copy();
            //camera.debug();
            //camera.y = 30;

            ((Catapult)focus).setAngle(yaw-180);
            pitch = 10;
        } else {
            this.delta = delta;
            if(Input.in) {
                yaw += MOUSE_SPEED * ((width / 2) - Input.getMouseX());
                pitch += MOUSE_SPEED * ((height / 2) - Input.getMouseY());
                this.lockCamera();

                this.move();

                Input.setMouse(width / 2, height / 2);
            }

            if(pdir == 0) {
                pitch -= pitchRecoil;
                pitchRecoil -= 0.1f;
                if(pitchRecoil <= 0) {
                    pdir = 1;
                    pitchRecoil = 0;
                }
            } else if(pdir == 1) {
                pitch += pitchRecoil;
                pitchRecoil += 0.07f;
                if(pitchRecoil > 0.5f) {
                    pdir = 2;
                }
            }

            if(Input.shift) {
                running = true;
            } else {
                running = false;
            }

            if(running) {
                if(perspective < 75) {
                    perspective++;
                    glViewport(0, 0, width, height);
                    glMatrixMode(GL_PROJECTION);
                    glLoadIdentity();
                    gluPerspective(perspective, (float) 1066 / (float) 600, 1.0f, 2000.0f);
                    glMatrixMode(GL_MODELVIEW);
                }
            } else {
                if(perspective > 68) {
                    perspective--;
                    glViewport(0, 0, width, height);
                    glMatrixMode(GL_PROJECTION);
                    glLoadIdentity();
                    gluPerspective(perspective, (float) 1066 / (float) 600, 1.0f, 2000.0f);
                    glMatrixMode(GL_MODELVIEW);
                }
            }
        }
    }

    public boolean isThirdPerson() {
        return third;
    }

    public void applyRotation() {
        glRotatef(pitch, 1.0f, 0f, 0f);
        glRotatef(-yaw, 0f, 1.0f, 0f);
    }

    public void applyTranslation() {
        glTranslatef((float)-camera.x,(float)-camera.y,(float)-camera.z);
    }

    public void move() {
        if(Input.w)  {
            moveFloor(0);
        } else if(Input.s)
            moveFloor(180);

        if(Input.a)
            moveFloor(90);
        else if(Input.d)
            moveFloor(270);
    }

    public void moveFloor(int direction) {
        double ang = Math.toRadians(yaw + direction);
        camera.x -= Math.sin(ang) * MOVE_SPEED * 0.8 * (direction == 0 && running ? 1.5f : 1);
        camera.z -= Math.cos(ang) * MOVE_SPEED * 0.8 * (direction == 0 && running ? 1.5f : 1);
    }

    public void lockCamera() {
        if(pitch > 90)
            pitch = 90;
        if(pitch < -90)
            pitch = -90;

        if(yaw > 360.0)
            yaw -= 360.0;
        if(yaw < 0.0)
            yaw += 360.0;
    }

    public Vector3D getCamera() {
        return camera.copy();
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public Vector3D getDirectionVector() {
        double pitchRadians = Math.toRadians(pitch);
        double yawRadians = Math.toRadians(yaw);

        double sinPitch = Math.sin(pitchRadians);
        double cosPitch = Math.cos(pitchRadians);
        double sinYaw = Math.sin(yawRadians);
        double cosYaw = Math.cos(yawRadians);

        return new Vector3D(-cosPitch * sinYaw, -sinPitch, -cosPitch * cosYaw);
    }

    public Vector3D getHoldingPosition() {
        Vector3D cam = this.camera.copy();
        Vector3D dir = this.getDirectionVector();
        return new Vector3D(cam.x + dir.x, cam.y + dir.y, cam.z + dir.z);
    }

    public void setCamera(Vector3D camera) {
        this.camera = camera;
    }

    public void setPitchRecoil(float pitchRecoil) {
        pdir = 0;
        this.pitchRecoil = pitchRecoil;
    }
}
