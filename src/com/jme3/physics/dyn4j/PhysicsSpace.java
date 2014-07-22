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
package com.jme3.physics.dyn4j;

import java.util.List;
import java.util.logging.Logger;

import javax.vecmath.Vector2d;

import org.dyn4j.collision.Bounds;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.Capacity;
import org.dyn4j.dynamics.Settings;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.geometry.Vector2;

import com.jme3.math.Vector3f;

/**
 * 
 * @author H
 */
public class PhysicsSpace {

    private static final Logger logger = Logger.getLogger(PhysicsSpace.class.getName());

    private static final float DEFAULT_SPEED = 1f;

    private World physicsWorld;

    // TODO Dyn4j: Se puede agregar un setter para el speed pero tengo que ver si eso afecta en algo los calculos de
    // jBox2D
    protected float speed = DEFAULT_SPEED;

    public PhysicsSpace(final Capacity initialCapacity, final Bounds bounds) {
        if (initialCapacity != null) {
            this.physicsWorld = new World(initialCapacity, bounds);
        } else {
            this.physicsWorld = new World(bounds);
        }
    }

    public void addBody(final Body body) {
        this.physicsWorld.addBody(body);
    }

    public boolean removeBody(final Body body) {
        return this.physicsWorld.removeBody(body);
    }

    public boolean removeBody(final Body body, final boolean notify) {
        return this.physicsWorld.removeBody(body, notify);
    }

    public void addJoint(final Joint joint) {
        this.physicsWorld.addJoint(joint);
    }

    public boolean removeJoint(final Joint joint) {
        return this.physicsWorld.removeJoint(joint);
    }

    public void updateFixed(final float elapsedTime) {
        this.physicsWorld.update(elapsedTime);
    }

    public void clear() {
        this.physicsWorld = null;
    }

    public void setGravity(final Vector2d gravity) {
        this.physicsWorld.setGravity(new Vector2(gravity.x, gravity.y));
    }

    public void setGravity(final double x, final double y) {
        this.physicsWorld.setGravity(new Vector2(x, y));
    }

    public Vector3f getGravity() {
        return Converter.toVector3f(this.physicsWorld.getGravity());
    }

    public void setSpeed(final float speed) {
        this.speed = speed;
        this.physicsWorld.getSettings().setSleepTime(Settings.DEFAULT_STEP_FREQUENCY * speed);
    }

    public float getSpeed() {
        return this.speed;
    }

    public List<Body> getBodies() {
        return this.physicsWorld.getBodies();
    }

    public List<Joint> getJoints() {
        return this.physicsWorld.getJoints();
    }

    public World getPhysicsWorld() {
        return this.physicsWorld;
    }

}
