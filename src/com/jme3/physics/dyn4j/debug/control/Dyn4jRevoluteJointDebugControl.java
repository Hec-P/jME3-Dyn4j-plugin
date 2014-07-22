package com.jme3.physics.dyn4j.debug.control;

import org.dyn4j.dynamics.joint.Joint;

import com.jme3.material.Material;
import com.jme3.physics.dyn4j.Converter;
import com.jme3.physics.dyn4j.debug.Dyn4jDebugAppState;
import com.jme3.physics.dyn4j.debug.PhysicDebugColor;

public class Dyn4jRevoluteJointDebugControl extends Dyn4jJointDebugControl {

    public Dyn4jRevoluteJointDebugControl(final Dyn4jDebugAppState dyn4jDebugAppState, final Joint joint) {
        super(dyn4jDebugAppState, joint);

        // joint.getAnchor1() and joint.getAnchor2() are equals
        this.anchorGeom1 = createCircle("CircleGeom1", Converter.toVector3f(joint.getAnchor1()));
    }

    @Override
    protected Material getAnchorGeom1Material(final boolean isJointActive) {
        return getMaterial(isJointActive ? PhysicDebugColor.ORANGE_FILLED : PhysicDebugColor.PINK_FILLED);
    }

}
