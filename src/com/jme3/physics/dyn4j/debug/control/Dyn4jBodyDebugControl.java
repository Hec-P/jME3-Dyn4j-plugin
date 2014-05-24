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
package com.jme3.physics.dyn4j.debug.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Convex;

import com.jme3.physics.dyn4j.control.Dyn4jBodyControl;
import com.jme3.physics.dyn4j.debug.Dyn4jDebugAppState;
import com.jme3.physics.dyn4j.debug.PhysicDebugColor;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * @author H
 */
public class Dyn4jBodyDebugControl extends Dyn4jBodyControl {

    protected Dyn4jDebugAppState dyn4jDebugAppState = null;

    protected Map<UUID, Convex> shapes = new HashMap<UUID, Convex>();
    protected List<Node> geometries = new ArrayList<Node>();

    public Dyn4jBodyDebugControl(final Dyn4jDebugAppState dyn4jDebugAppState, final Body body) {
        super(body);
        this.dyn4jDebugAppState = dyn4jDebugAppState;

        for (final BodyFixture bodyFixture : body.getFixtures()) {

            // Fixture: create spatial for the shape and add it to geometry list.
            final Convex shape = bodyFixture.getShape();
            processShape(shape);
        }
    }

    @Override
    public void setSpatial(final Spatial spatial) {
        if (spatial != null && spatial instanceof Node) {
            final Node spatialAsNode = (Node) spatial;
            for (final Node node : this.geometries) {
                spatialAsNode.attachChild(node);
            }
        } else if (spatial == null && this.spatial != null) {
            final Node spatialAsNode = (Node) this.spatial;
            for (final Node node : this.geometries) {
                spatialAsNode.detachChild(node);
            }
        }
        super.setSpatial(spatial);
    }

    @Override
    protected void controlUpdate(final float tpf) {
        final List<UUID> currentIDs = new ArrayList<UUID>();

        for (final BodyFixture bodyFixture : this.body.getFixtures()) {
            final Convex shape = bodyFixture.getShape();

            currentIDs.add(shape.getId());

            if (!this.shapes.containsKey(shape.getId())) {

                // New fixture: create spatial for the shape, add it to geometry list and attach to the root spatial.
                final Node node = processShape(shape);

                if (node != null) {
                    final Node spatialAsNode = (Node) this.spatial;
                    spatialAsNode.attachChild(node);
                }
            }
        }

        // Remove shapes that are not present on the body's features list.
        this.shapes.keySet().retainAll(currentIDs);

        if (this.body.isAsleep()) {
            for (final Node node : this.geometries) {
                final Spatial geom = node.getChild(node.getName());
                if (geom != null) {
                    geom.setMaterial(this.dyn4jDebugAppState.getDebugMaterial(PhysicDebugColor.BLUE));
                }
            }
        } else if (!this.body.isActive()) {
            for (final Node node : this.geometries) {
                final Spatial geom = node.getChild(node.getName());
                if (geom != null) {
                    geom.setMaterial(this.dyn4jDebugAppState.getDebugMaterial(PhysicDebugColor.GRAY));
                }
            }
        } else {
            for (final Node node : this.geometries) {
                final Spatial geom = node.getChild(node.getName());
                if (geom != null) {
                    geom.setMaterial(this.dyn4jDebugAppState.getDebugMaterial(PhysicDebugColor.MAGENTA));
                }
            }
        }

        // Update spatial location and rotation
        super.controlUpdate(tpf);
    }

    @Override
    protected void controlRender(final RenderManager rm, final ViewPort vp) {
    }

    private Node processShape(final Convex shape) {
        this.shapes.put(shape.getId(), shape);

        final Node node = this.dyn4jDebugAppState.getDebugShape(shape);

        if (node != null) {
            this.geometries.add(node);
        }
        return node;
    }

}
