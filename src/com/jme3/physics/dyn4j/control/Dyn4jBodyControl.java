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
package com.jme3.physics.dyn4j.control;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Transform;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.physics.dyn4j.Converter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.util.TempVars;

/**
 * 
 * @author H
 */
public class Dyn4jBodyControl extends AbstractControl {

    protected Body body = null;

    public Dyn4jBodyControl(final Body body) {
        this.body = body;
    }

    @Override
    public void setSpatial(final Spatial spatial) {
        super.setSpatial(spatial);
    }

    public Body getBody() {
        return this.body;
    }

    @Override
    protected void controlUpdate(final float tpf) {
        // Update spatial location and rotation
        setPhysicLocation(this.body);
        setPhysicRotation(this.body);
    }

    @Override
    protected void controlRender(final RenderManager rm, final ViewPort vp) {
    }

    private void setPhysicRotation(final Body physicBody) {
        final Transform transform = physicBody.getTransform();

        final float rotation = Converter.toFloat(transform.getRotation());

        final TempVars tempVars = TempVars.get();
        final Quaternion quaternion = tempVars.quat1;
        quaternion.fromAngleAxis(rotation, Vector3f.UNIT_Z);

        this.spatial.setLocalRotation(quaternion);

        tempVars.release();
    }

    private void setPhysicLocation(final Body physicBody) {
        final Transform transform = physicBody.getTransform();

        final float posX = Converter.toFloat(transform.getTranslationX());
        final float posY = Converter.toFloat(transform.getTranslationY());

        this.spatial.setLocalTranslation(posX, posY, this.spatial.getLocalTranslation().z);
    }

}
