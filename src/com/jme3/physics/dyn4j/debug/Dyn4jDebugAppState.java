/*
 * Copyright (c) 2009-2014 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jme3.physics.dyn4j.debug;

import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.geometry.Convex;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.physics.dyn4j.PhysicsSpace;
import com.jme3.physics.dyn4j.debug.control.Dyn4jBodyDebugControl;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * 
 * @author H
 */
public class Dyn4jDebugAppState extends AbstractAppState {

    protected static final Logger logger = Logger.getLogger(Dyn4jDebugAppState.class.getName());
    protected Application app;
    protected AssetManager assetManager;
    protected final PhysicsSpace space;
    protected final Node physicsDebugRootNode = new Node("Physics Debug Root Node");
    protected ViewPort viewPort;
    protected RenderManager rm;

    protected HashMap<Body, Spatial> bodies = new HashMap<Body, Spatial>();
    protected HashMap<Joint, Spatial> joints = new HashMap<Joint, Spatial>();

    protected Dyn4jDebugShapeFactory debugShapeFactory = null;

    public Dyn4jDebugAppState(final PhysicsSpace space) {
        this.space = space;
    }

    @Override
    public void initialize(final AppStateManager stateManager, final Application app) {
        super.initialize(stateManager, app);
        this.app = app;
        this.rm = app.getRenderManager();
        this.assetManager = app.getAssetManager();
        this.physicsDebugRootNode.setCullHint(Spatial.CullHint.Never);
        this.viewPort = this.rm.createMainView("Physics Debug Overlay", app.getCamera());
        this.viewPort.setClearFlags(false, true, false);
        this.viewPort.attachScene(this.physicsDebugRootNode);
        this.debugShapeFactory = new Dyn4jDebugShapeFactory(app.getAssetManager());
    }

    @Override
    public void cleanup() {
        this.rm.removeMainView(this.viewPort);
        super.cleanup();
    }

    @Override
    public void update(final float tpf) {
        super.update(tpf);

        // Update all object links
        updateRigidBodies();
        updateJoints();

        // Update debug root node
        this.physicsDebugRootNode.updateLogicalState(tpf);
        this.physicsDebugRootNode.updateGeometricState();
    }

    @Override
    public void render(final RenderManager rm) {
        super.render(rm);
        if (this.viewPort != null) {
            rm.renderScene(this.physicsDebugRootNode, this.viewPort);
        }
    }

    private void updateRigidBodies() {
        final HashMap<Body, Spatial> oldBodies = this.bodies;
        this.bodies = new HashMap<Body, Spatial>();
        final Collection<Body> currentBodies = this.space.getBodies();

        // Create new map of bodies
        for (final Body body : currentBodies) {

            if (oldBodies.containsKey(body)) {

                // Copy existing spatial
                final Spatial spatial = oldBodies.get(body);
                this.bodies.put(body, spatial);
                oldBodies.remove(body);

            } else {

                // if (filter == null || filter.displayObject(physicsObject)) {
                logger.log(Level.FINE, "Create new debug RigidBody");
                // Create new spatial
                final Node node = new Node(body.toString());
                node.addControl(new Dyn4jBodyDebugControl(this, body));
                this.bodies.put(body, node);
                this.physicsDebugRootNode.attachChild(node);
                // }

            }
        }

        // Remove leftover spatials
        for (final Spatial spatial : oldBodies.values()) {
            spatial.removeFromParent();
        }
    }

    private void updateJoints() {
        final HashMap<Joint, Spatial> oldObjects = this.joints;
        this.joints = new HashMap<Joint, Spatial>();
        final Collection<Joint> currentJoints = this.space.getJoints();

        // Create new map of joints
        for (final Joint joint : currentJoints) {

            if (oldObjects.containsKey(joint)) {

                // Copy existing spatial
                final Spatial spat = oldObjects.get(joint);
                this.joints.put(joint, spat);
                oldObjects.remove(joint);

            } else {

                // if (filter == null || filter.displayObject(physicsObject)) {
                logger.log(Level.FINE, "Create new debug Joint");
                // Create new spatial
                final Node node = new Node(joint.toString());
                node.addControl(this.debugShapeFactory.getJointDebugControl(this, joint));
                this.joints.put(joint, node);
                this.physicsDebugRootNode.attachChild(node);
                // }

            }
        }

        // Remove leftover spatials
        for (final Spatial spatial : oldObjects.values()) {
            spatial.removeFromParent();
        }
    }

    public Material getDebugMaterial(final PhysicDebugColor physicDebugColor) {
        return this.debugShapeFactory.getDebugMaterial(physicDebugColor);
    }

    public Node getDebugShape(final Convex shape) {
        return this.debugShapeFactory.getDebugShape(shape);
    }

}
