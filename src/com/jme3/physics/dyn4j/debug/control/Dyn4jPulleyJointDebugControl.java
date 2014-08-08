package com.jme3.physics.dyn4j.debug.control;

import org.dyn4j.dynamics.joint.PulleyJoint;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.physics.dyn4j.Converter;
import com.jme3.physics.dyn4j.debug.Dyn4jDebugAppState;
import com.jme3.physics.dyn4j.debug.PhysicDebugColor;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Line;

public class Dyn4jPulleyJointDebugControl extends Dyn4jJointDebugControl {

    protected Geometry pulleyLineGeom1 = null;
    protected Geometry pulleyLineGeom2 = null;
    protected Geometry pulleyAnchorGeom1 = null;
    protected Geometry pulleyAnchorGeom2 = null;

    public Dyn4jPulleyJointDebugControl(final Dyn4jDebugAppState dyn4jDebugAppState, final PulleyJoint pulleyJoint) {
        super(dyn4jDebugAppState, pulleyJoint);

        final Vector3f p1 = Converter.toVector3f(pulleyJoint.getAnchor1());
        final Vector3f p2 = Converter.toVector3f(pulleyJoint.getPulleyAnchor1());
        final Vector3f p3 = Converter.toVector3f(pulleyJoint.getPulleyAnchor2());
        final Vector3f p4 = Converter.toVector3f(pulleyJoint.getAnchor2());

        this.anchorGeom1 = createCircle("CircleGeom1", p1);
        this.pulleyAnchorGeom1 = createPulleyCross("PulleyCrossGeom1", p2);
        this.pulleyAnchorGeom2 = createPulleyCross("PulleyCrossGeom2", p3);
        this.anchorGeom2 = createCircle("CircleGeom2", p4);

        this.pulleyLineGeom1 = createLine("PulleyLineGeom1", p1, p2, 1);
        this.lineGeom = createLine("LineGeom1", p2, p3, 1);
        this.pulleyLineGeom2 = createLine("PulleyLineGeom1", p3, p4, 1);
    }

    @Override
    public void update(final float tpf) {
        final PulleyJoint pulleyJoint = (PulleyJoint) this.joint;

        final Vector3f p1 = Converter.toVector3f(pulleyJoint.getAnchor1());
        final Vector3f p2 = Converter.toVector3f(pulleyJoint.getPulleyAnchor1());
        final Vector3f p3 = Converter.toVector3f(pulleyJoint.getPulleyAnchor2());
        final Vector3f p4 = Converter.toVector3f(pulleyJoint.getAnchor2());

        final Material anchorGeomMaterial = getAnchorGeom1Material(this.joint.isActive());
        final Material lineGeomMaterial = getLineGeomMaterial(this.joint.isActive());

        this.anchorGeom1.setLocalTranslation(p1);
        this.anchorGeom1.setMaterial(anchorGeomMaterial);

        this.pulleyAnchorGeom1.setLocalTranslation(p2);
        this.pulleyAnchorGeom1.setMaterial(anchorGeomMaterial);

        this.pulleyAnchorGeom2.setLocalTranslation(p3);
        this.pulleyAnchorGeom2.setMaterial(anchorGeomMaterial);

        this.anchorGeom2.setLocalTranslation(p4);
        this.anchorGeom2.setMaterial(anchorGeomMaterial);

        ((Line) this.lineGeom.getMesh()).updatePoints(p2, p3);
        this.lineGeom.setMaterial(lineGeomMaterial);

        ((Line) this.pulleyLineGeom1.getMesh()).updatePoints(p1, p2);
        this.pulleyLineGeom1.setMaterial(lineGeomMaterial);

        ((Line) this.pulleyLineGeom2.getMesh()).updatePoints(p3, p4);
        this.pulleyLineGeom2.setMaterial(lineGeomMaterial);
    }

    @Override
    protected Material getLineGeomMaterial(final boolean isJointActive) {
        return getMaterial(isJointActive ? PhysicDebugColor.ORANGE : PhysicDebugColor.PINK);
    }

    @Override
    protected Material getAnchorGeom1Material(final boolean isJointActive) {
        return getMaterial(isJointActive ? PhysicDebugColor.GREEN : PhysicDebugColor.PINK_FILLED);
    }

}
