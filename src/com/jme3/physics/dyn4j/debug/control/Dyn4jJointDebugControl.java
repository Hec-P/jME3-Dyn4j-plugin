package com.jme3.physics.dyn4j.debug.control;

import org.dyn4j.dynamics.joint.Joint;

import com.jme3.physics.dyn4j.debug.Dyn4jDebugAppState;
import com.jme3.physics.dyn4j.debug.PhysicDebugColor;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

public class Dyn4jJointDebugControl extends AbstractControl {

    protected Dyn4jDebugAppState dyn4jDebugAppState = null;
    protected Joint joint = null;
    protected Node geometry = new Node();

    public Dyn4jJointDebugControl(final Dyn4jDebugAppState dyn4jDebugAppState, final Joint joint) {
        this.dyn4jDebugAppState = dyn4jDebugAppState;
        this.joint = joint;
        this.geometry = this.dyn4jDebugAppState.getDebugShape(joint);
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
        if (!this.joint.isActive()) {
            this.geometry.setMaterial(this.dyn4jDebugAppState.getDebugMaterial(PhysicDebugColor.GRAY));
        } else {
            this.geometry.setMaterial(this.dyn4jDebugAppState.getDebugMaterial(PhysicDebugColor.MAGENTA));
        }

        // Update spatial location and rotation
        setPhysicLocation(this.joint);
        setPhysicRotation(this.joint);
    }

    private void setPhysicRotation(final Joint joint2) {

    }

    private void setPhysicLocation(final Joint joint2) {

    }

    @Override
    protected void controlRender(final RenderManager rm, final ViewPort vp) {
    }

}
