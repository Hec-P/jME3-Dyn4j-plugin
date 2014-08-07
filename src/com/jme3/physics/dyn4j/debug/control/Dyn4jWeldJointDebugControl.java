package com.jme3.physics.dyn4j.debug.control;

import org.dyn4j.dynamics.joint.Joint;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.physics.dyn4j.Converter;
import com.jme3.physics.dyn4j.debug.Dyn4jDebugAppState;
import com.jme3.physics.dyn4j.debug.PhysicDebugColor;

public class Dyn4jWeldJointDebugControl extends Dyn4jJointDebugControl {

    public Dyn4jWeldJointDebugControl(final Dyn4jDebugAppState dyn4jDebugAppState, final Joint joint) {
        super(dyn4jDebugAppState, joint);

        // joint.getAnchor1() and joint.getAnchor2() are equals
        final Vector3f p1 = Converter.toVector3f(joint.getAnchor1());

        this.anchorGeom1 = createCross("CrossGeom1", p1);
    }

    @Override
    protected Material getAnchorGeom1Material(final boolean isJointActive) {
        return getMaterial(isJointActive ? PhysicDebugColor.YELLOW : PhysicDebugColor.PINK_FILLED);
    }

}
