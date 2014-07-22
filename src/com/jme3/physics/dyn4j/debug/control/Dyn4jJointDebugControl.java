package com.jme3.physics.dyn4j.debug.control;

import org.dyn4j.dynamics.joint.Joint;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.physics.dyn4j.Converter;
import com.jme3.physics.dyn4j.debug.Dyn4jDebugAppState;
import com.jme3.physics.dyn4j.debug.PhysicDebugColor;
import com.jme3.physics.dyn4j.debug.shape.CircleDebug;
import com.jme3.physics.dyn4j.debug.shape.WoundDebug;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Line;

public abstract class Dyn4jJointDebugControl extends AbstractControl {

    private static final float SQUARE_SEGMENT_SIZE = 0.2f;
    private static final int CIRCLE_SEGMENT_NUMBER = 10;
    private static final float CIRCLE_RADIUS = .1f;

    protected Dyn4jDebugAppState dyn4jDebugAppState = null;
    protected Joint joint = null;
    protected Geometry lineGeom = null;
    protected Geometry anchorGeom1 = null;
    protected Geometry anchorGeom2 = null;

    protected Node geometry = null;

    public Dyn4jJointDebugControl(final Dyn4jDebugAppState dyn4jDebugAppState, final Joint joint) {
        this.dyn4jDebugAppState = dyn4jDebugAppState;
        this.joint = joint;
        this.geometry = new Node(joint.getId().toString());
    }

    @Override
    public void setSpatial(final Spatial spatial) {
        if (spatial != null && spatial instanceof Node) {
            final Node spatialAsNode = (Node) spatial;
            spatialAsNode.attachChild(this.geometry);
        } else if (spatial == null && this.spatial != null) {
            final Node spatialAsNode = (Node) this.spatial;
            spatialAsNode.detachChild(this.geometry);
        }
        super.setSpatial(spatial);
    }

    @Override
    protected void controlUpdate(final float tpf) {
        final Vector3f p1 = Converter.toVector3f(this.joint.getAnchor1());
        final Vector3f p2 = Converter.toVector3f(this.joint.getAnchor2());

        if (this.lineGeom != null) {
            final Mesh mesh = this.lineGeom.getMesh();
            if (mesh instanceof Line) {
                ((Line) mesh).updatePoints(p1, p2);
            }
            this.lineGeom.setMaterial(getLineGeomMaterial(this.joint.isActive()));
        }

        if (this.anchorGeom1 != null) {
            this.anchorGeom1.setLocalTranslation(p1);
            this.anchorGeom1.setMaterial(getAnchorGeom1Material(this.joint.isActive()));
        }

        if (this.anchorGeom2 != null) {
            this.anchorGeom2.setLocalTranslation(p2);
            this.anchorGeom2.setMaterial(getAnchorGeom2Material(this.joint.isActive()));
        }
    }

    @Override
    protected void controlRender(final RenderManager rm, final ViewPort vp) {
    }

    protected Geometry createSquare(final String name, final Vector3f center) {
        final float size = SQUARE_SEGMENT_SIZE / 2;
        final Vector3f[] vertices = new Vector3f[4];
        vertices[0] = new Vector3f(size, size, 0);
        vertices[1] = new Vector3f(-size, size, 0);
        vertices[2] = new Vector3f(-size, -size, 0);
        vertices[3] = new Vector3f(size, -size, 0);

        final WoundDebug square = new WoundDebug(vertices);
        final Geometry squareGeom = new Geometry(name + this.joint.getId().toString(), square);
        center.x -= SQUARE_SEGMENT_SIZE / 2;
        center.y -= SQUARE_SEGMENT_SIZE / 2;
        squareGeom.setLocalTranslation(center);

        this.geometry.attachChild(squareGeom);

        return squareGeom;
    }

    protected Geometry createCircle(final String name, final Vector3f center) {
        final CircleDebug circleDebug = new CircleDebug(CIRCLE_RADIUS, CIRCLE_SEGMENT_NUMBER);
        final Geometry circleGeom = new Geometry(name + this.joint.getId().toString(), circleDebug);
        circleGeom.setLocalTranslation(center);

        this.geometry.attachChild(circleGeom);

        return circleGeom;
    }

    protected Geometry createLine(final String name, final Vector3f p1, final Vector3f p2, final float lineWidth) {
        final Line line = new Line(p1, p2);
        line.setLineWidth(lineWidth);

        final Geometry lineGeom = new Geometry(name, line);
        this.geometry.attachChild(lineGeom);

        return lineGeom;
    }

    protected Material getLineGeomMaterial(final boolean isJointActive) {
        return getMaterial(isJointActive ? PhysicDebugColor.YELLOW : PhysicDebugColor.GREEN);
    }

    protected Material getAnchorGeom1Material(final boolean isJointActive) {
        return getMaterial(isJointActive ? PhysicDebugColor.ORANGE : PhysicDebugColor.PINK);
    }

    protected Material getAnchorGeom2Material(final boolean isJointActive) {
        return getAnchorGeom1Material(isJointActive);
    }

    protected Material getMaterial(final PhysicDebugColor color) {
        return this.dyn4jDebugAppState.getDebugMaterial(color);
    }

}
