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
package com.jme3.physics.dyn4j.debug.shape;

import java.io.IOException;
import java.nio.FloatBuffer;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.mesh.IndexBuffer;
import com.jme3.util.BufferUtils;

/**
 * A 2D Wound (polygon, triangle or segment), defined by it's vertices.
 * 
 * @author H
 */
public class WoundDebug extends Mesh {

    private Vector3f[] vertices;

    /**
     * Default constructor for serialization only. Do not use.
     */
    public WoundDebug() {
    }

    /**
     * Main constructor to build an instance of a wound.
     * 
     * @param vertices
     *            the vertices of the wound.
     */
    public WoundDebug(final Vector3f[] vertices) {
        super();
        setMode(Mode.LineLoop);
        updateGeometry(vertices);
    }

    public Vector3f[] getVertices() {
        return this.vertices;
    }

    /**
     * Rebuilds the wound based on a new set of parameters.
     * 
     * @param vertices
     *            the vertices of the wound.
     */
    public void updateGeometry(final Vector3f[] vertices) {
        // Update values
        this.vertices = vertices;

        // Create buffers
        this.setBuffer(Type.Position, 3,
                BufferUtils.createVector3Buffer(getFloatBuffer(Type.Position), this.vertices.length));

        this.setBuffer(Type.Index, 1, BufferUtils.createShortBuffer(getShortBuffer(Type.Index), this.vertices.length));

        final FloatBuffer pb = getFloatBuffer(Type.Position);
        final IndexBuffer ib = getIndexBuffer();

        for (int i = 0; i < this.vertices.length; i++) {
            final Vector3f vertice = vertices[i];
            // Set vertices
            pb.put(vertice.getX()).put(vertice.getY()).put(vertice.getZ());

            // Set index
            ib.put(i, i);
        }

        updateBound();
    }

    /**
     * @see Mesh#read(JmeImporter).
     */
    @Override
    public void read(final JmeImporter e) throws IOException {
        super.read(e);
        final InputCapsule capsule = e.getCapsule(this);
        this.vertices = (Vector3f[]) capsule.readSavableArray("vertices", null);
    }

    /**
     * @see Mesh#write(JmeExporter).
     */
    @Override
    public void write(final JmeExporter e) throws IOException {
        super.write(e);
        final OutputCapsule capsule = e.getCapsule(this);
        capsule.write(this.vertices, "vertices", null);
    }

}
