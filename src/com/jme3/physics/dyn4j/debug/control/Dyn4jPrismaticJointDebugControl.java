package com.jme3.physics.dyn4j.debug.control;

import org.dyn4j.dynamics.joint.Joint;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.physics.dyn4j.Converter;
import com.jme3.physics.dyn4j.debug.Dyn4jDebugAppState;
import com.jme3.physics.dyn4j.debug.PhysicDebugColor;

public class Dyn4jPrismaticJointDebugControl extends Dyn4jJointDebugControl {

    public Dyn4jPrismaticJointDebugControl(final Dyn4jDebugAppState dyn4jDebugAppState, final Joint joint) {
        super(dyn4jDebugAppState, joint);

        final Vector3f p1 = Converter.toVector3f(joint.getAnchor1());
        final Vector3f p2 = Converter.toVector3f(joint.getAnchor2());

        this.lineGeom = createLine(joint.getId().toString(), p1, p2, 2);
        this.anchorGeom1 = createSquare("SquareGeom1", p1);
        this.anchorGeom2 = createSquare("SquareGeom2", p2);
    }

    @Override
    protected Material getLineGeomMaterial(final boolean isJointActive) {
        return getMaterial(isJointActive ? PhysicDebugColor.BROWN_FILLED : PhysicDebugColor.PINK);
    }

    @Override
    protected Material getAnchorGeom1Material(final boolean isJointActive) {
        return getMaterial(isJointActive ? PhysicDebugColor.ORANGE : PhysicDebugColor.PINK_FILLED);
    }

}
