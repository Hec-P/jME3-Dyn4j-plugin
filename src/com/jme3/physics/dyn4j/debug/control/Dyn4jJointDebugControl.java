package com.jme3.physics.dyn4j.debug.control;

import org.dyn4j.dynamics.joint.Joint;

import com.jme3.math.Vector3f;
import com.jme3.physics.dyn4j.Converter;
import com.jme3.physics.dyn4j.debug.Dyn4jDebugAppState;
import com.jme3.physics.dyn4j.debug.PhysicDebugColor;
import com.jme3.physics.dyn4j.debug.shape.CircleDebug;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Line;

public class Dyn4jJointDebugControl extends AbstractControl {

    protected Dyn4jDebugAppState dyn4jDebugAppState = null;
    protected Joint joint = null;
    protected Node geometry = null;
    protected Line lineDebug = null;
    protected Geometry lineGeom = null;
    protected Geometry circleGeom1 = null;
    protected Geometry circleGeom2 = null;

    public Dyn4jJointDebugControl(final Dyn4jDebugAppState dyn4jDebugAppState, final Joint joint) {
        this.dyn4jDebugAppState = dyn4jDebugAppState;
        this.joint = joint;
        // this.geometry = this.dyn4jDebugAppState.getDebugShape(joint);
        this.geometry = getDebugShape(joint);
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
        final Vector3f p1 = Converter.vector2ToVector3f(this.joint.getAnchor1());
        final Vector3f p2 = Converter.vector2ToVector3f(this.joint.getAnchor2());

        this.lineDebug.updatePoints(p1, p2);
        this.circleGeom1.setLocalTranslation(p1);
        this.circleGeom2.setLocalTranslation(p2);

        if (!this.joint.isActive()) {
            this.lineGeom.setMaterial(this.dyn4jDebugAppState.getDebugMaterial(PhysicDebugColor.GREEN));
            this.circleGeom1.setMaterial(this.dyn4jDebugAppState.getDebugMaterial(PhysicDebugColor.PINK));
            this.circleGeom2.setMaterial(this.dyn4jDebugAppState.getDebugMaterial(PhysicDebugColor.PINK));
        } else {
            this.lineGeom.setMaterial(this.dyn4jDebugAppState.getDebugMaterial(PhysicDebugColor.YELLOW));
            this.circleGeom1.setMaterial(this.dyn4jDebugAppState.getDebugMaterial(PhysicDebugColor.ORANGE));
            this.circleGeom2.setMaterial(this.dyn4jDebugAppState.getDebugMaterial(PhysicDebugColor.ORANGE));
        }
    }

    @Override
    protected void controlRender(final RenderManager rm, final ViewPort vp) {
    }

    private Node getDebugShape(final Joint joint) {
        final Node node = new Node(joint.getId().toString());

        final Vector3f p1 = Converter.vector2ToVector3f(joint.getAnchor1());
        final Vector3f p2 = Converter.vector2ToVector3f(joint.getAnchor2());
        this.lineDebug = new Line(p1, p2);
        this.lineDebug.setLineWidth(2);

        this.lineGeom = new Geometry(joint.getId().toString(), this.lineDebug);
        node.attachChild(this.lineGeom);

        this.circleGeom1 = createCircle("CircleGeom1", p1);
        node.attachChild(this.circleGeom1);

        this.circleGeom2 = createCircle("CircleGeom2", p2);
        node.attachChild(this.circleGeom2);

        return node;
    }

    private Geometry createCircle(final String name, final Vector3f center) {
        final CircleDebug circleDebug = new CircleDebug(.1f, 10);
        final Geometry circleGeom = new Geometry(name + this.joint.getId().toString(), circleDebug);
        circleGeom.setLocalTranslation(center);

        return circleGeom;
    }

}
