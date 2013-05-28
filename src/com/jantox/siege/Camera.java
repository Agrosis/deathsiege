package com.jantox.siege;

import static org.lwjgl.opengl.GL11.*;

public class Camera {

    public static float MOUSE_SPEED = 0.15f;
    public static float MOVE_SPEED = 0.15f;

    private int width, height;

    public Vector3D camera;
    private float pitch, yaw; // stored in degrees

    private float delta;

    public Camera(Vector3D cam, int width, int height) {
        this.camera = cam.copy();
        this.width = width;
        this.height = height;

        pitch = 0;
        yaw = 0;
    }

    public void update(float delta) {
        this.delta = delta;
        if(Input.in) {
            yaw += MOUSE_SPEED * ((width / 2) - Input.getMouseX());
            pitch += MOUSE_SPEED * ((height / 2) - Input.getMouseY());
            this.lockCamera();

            this.move();

            Input.setMouse(width / 2, height / 2);
        }
    }

    public void applyRotation() {
        glRotatef(pitch, 1.0f, 0f, 0f);
        glRotatef(-yaw, 0f, 1.0f, 0f);
    }

    public void applyTranslation() {
        glTranslatef((float)-camera.x,(float)-camera.y,(float)-camera.z);
    }

    public void move() {
        if(Input.w)
            moveFloor(0);
        else if(Input.s)
            moveFloor(180);

        if(Input.a)
            moveFloor(90);
        else if(Input.d)
            moveFloor(270);
    }

    public void moveFloor(int direction) {
        double ang = Math.toRadians(yaw + direction);
        camera.x -= Math.sin(ang) * MOVE_SPEED * (Input.r ? 1.8 : 1.25);
        camera.z -= Math.cos(ang) * MOVE_SPEED * (Input.r ? 1.8 : 1.25);
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
}
