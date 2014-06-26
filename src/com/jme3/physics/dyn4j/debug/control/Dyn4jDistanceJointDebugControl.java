package com.jme3.physics.dyn4j.debug.control;

import org.dyn4j.dynamics.joint.Joint;

import com.jme3.math.Vector3f;
import com.jme3.physics.dyn4j.Converter;
import com.jme3.physics.dyn4j.debug.Dyn4jDebugAppState;
import com.jme3.physics.dyn4j.debug.PhysicDebugColor;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Line;

public class Dyn4jDistanceJointDebugControl extends Dyn4jRevoluteJointDebugControl {

    protected Line lineDebug = null;
    protected Geometry lineGeom = null;
    protected Geometry circleGeom2 = null;

    public Dyn4jDistanceJointDebugControl(final Dyn4jDebugAppState dyn4jDebugAppState, final Joint joint) {
        super(dyn4jDebugAppState, joint);

        final Vector3f p1 = Converter.vector2ToVector3f(joint.getAnchor1());
        final Vector3f p2 = Converter.vector2ToVector3f(joint.getAnchor2());
        this.lineDebug = new Line(p1, p2);
        this.lineDebug.setLineWidth(2);

        this.lineGeom = new Geometry(joint.getId().toString(), this.lineDebug);
        this.geometry.attachChild(this.lineGeom);

        this.circleGeom2 = createCircle("CircleGeom2", p2);
        this.geometry.attachChild(this.circleGeom2);
    }

    @Override
    protected void controlUpdate(final float tpf) {
        final Vector3f p1 = Converter.vector2ToVector3f(this.joint.getAnchor1());
        final Vector3f p2 = Converter.vector2ToVector3f(this.joint.getAnchor2());

        this.lineDebug.updatePoints(p1, p2);
        this.circleGeom2.setLocalTranslation(p2);

        if (!this.joint.isActive()) {
            this.lineGeom.setMaterial(this.dyn4jDebugAppState.getDebugMaterial(PhysicDebugColor.GREEN));
            this.circleGeom2.setMaterial(this.dyn4jDebugAppState.getDebugMaterial(PhysicDebugColor.PINK));
        } else {
            this.lineGeom.setMaterial(this.dyn4jDebugAppState.getDebugMaterial(PhysicDebugColor.YELLOW));
            this.circleGeom2.setMaterial(this.dyn4jDebugAppState.getDebugMaterial(PhysicDebugColor.ORANGE));
        }

        super.controlUpdate(tpf);
    }

}
