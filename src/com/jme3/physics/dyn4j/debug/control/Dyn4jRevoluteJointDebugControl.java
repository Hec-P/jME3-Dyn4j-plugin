package com.jme3.physics.dyn4j.debug.control;

import org.dyn4j.dynamics.joint.Joint;

import com.jme3.math.Vector3f;
import com.jme3.physics.dyn4j.Converter;
import com.jme3.physics.dyn4j.debug.Dyn4jDebugAppState;
import com.jme3.physics.dyn4j.debug.PhysicDebugColor;
import com.jme3.physics.dyn4j.debug.shape.CircleDebug;
import com.jme3.scene.Geometry;

public class Dyn4jRevoluteJointDebugControl extends Dyn4jJointDebugControl {

    protected Geometry circleGeom1 = null;

    public Dyn4jRevoluteJointDebugControl(final Dyn4jDebugAppState dyn4jDebugAppState, final Joint joint) {
        super(dyn4jDebugAppState, joint);

        // joint.getAnchor1() and joint.getAnchor2() are equals
        final Vector3f p1 = Converter.vector2ToVector3f(joint.getAnchor1());
        this.circleGeom1 = createCircle("CircleGeom1", p1);
        this.geometry.attachChild(this.circleGeom1);
    }

    @Override
    protected void controlUpdate(final float tpf) {
        final Vector3f p1 = Converter.vector2ToVector3f(this.joint.getAnchor1());

        this.circleGeom1.setLocalTranslation(p1);

        if (!this.joint.isActive()) {
            this.circleGeom1.setMaterial(this.dyn4jDebugAppState.getDebugMaterial(PhysicDebugColor.PINK));
        } else {
            this.circleGeom1.setMaterial(this.dyn4jDebugAppState.getDebugMaterial(PhysicDebugColor.ORANGE));
        }
    }

    protected Geometry createCircle(final String name, final Vector3f center) {
        final CircleDebug circleDebug = new CircleDebug(.1f, 10);
        final Geometry circleGeom = new Geometry(name + this.joint.getId().toString(), circleDebug);
        circleGeom.setLocalTranslation(center);

        return circleGeom;
    }

}
