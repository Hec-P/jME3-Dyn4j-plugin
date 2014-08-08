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
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.mesh.IndexBuffer;
import com.jme3.util.BufferUtils;

/**
 * A 2D Cross, defined by it's segment size. The Slice is centered on x and y axis. Value of segment size by default is
 * 0.2f.
 * 
 * @author H
 */
public class CrossDebug extends Mesh {

    private static final float DEFAULT_SEGMENT_SIZE = 0.2f;

    private float segmentSize;

    /**
     * Default constructor for serialization only. Do not use.
     */
    public CrossDebug() {
    }

    /**
     * Main constructor to build an instance of a cross.
     * 
     * @param segmentSize
     *            the segment size of the cross.
     */
    public CrossDebug(final float segmentSize) {
        this(DEFAULT_SEGMENT_SIZE, Mode.Lines);
    }

    /**
     * Main constructor to build an instance of a cross.
     * 
     * @param segmentSize
     *            the segment size of the cross.
     * @param mode
     *            the mesh mode.
     */
    public CrossDebug(final float segmentSize, final Mode mode) {
        super();
        setMode(mode);
        updateGeometry(segmentSize);
    }

    /**
     * Return the segment size of the cross.
     * 
     * @return segment size of the cross.
     */
    public float getSegmentSize() {
        return this.segmentSize;
    }

    /**
     * Rebuilds the cross based on a new set of parameters.
     * 
     * @param segmentSize
     *            the segment size of the cross.
     */
    public void updateGeometry(final float segmentSize) {
        // Update values
        this.segmentSize = segmentSize > 0 ? segmentSize : DEFAULT_SEGMENT_SIZE;

        // Create buffers
        this.setBuffer(Type.Position, 3, BufferUtils.createVector3Buffer(getFloatBuffer(Type.Position), 4));

        this.setBuffer(Type.Index, 1, BufferUtils.createShortBuffer(getShortBuffer(Type.Index), 4));

        final FloatBuffer pb = getFloatBuffer(Type.Position);
        final IndexBuffer ib = getIndexBuffer();

        // Set vertices
        pb.put(this.segmentSize).put(this.segmentSize).put(0);
        pb.put(-this.segmentSize).put(this.segmentSize).put(0);
        pb.put(this.segmentSize).put(-this.segmentSize).put(0);
        pb.put(-this.segmentSize).put(-this.segmentSize).put(0);

        // Set index
        ib.put(0, 0);
        ib.put(1, 3);
        ib.put(2, 1);
        ib.put(3, 2);

        updateBound();
    }

    /**
     * @see Mesh#read(JmeImporter).
     */
    @Override
    public void read(final JmeImporter e) throws IOException {
        super.read(e);
        final InputCapsule capsule = e.getCapsule(this);
        this.segmentSize = capsule.readFloat("segmentSize", DEFAULT_SEGMENT_SIZE);
    }

    /**
     * @see Mesh#write(JmeExporter).
     */
    @Override
    public void write(final JmeExporter e) throws IOException {
        super.write(e);
        final OutputCapsule capsule = e.getCapsule(this);
        capsule.write(this.segmentSize, "segmentSize", DEFAULT_SEGMENT_SIZE);
    }

}
