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
import com.jme3.math.FastMath;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.mesh.IndexBuffer;
import com.jme3.util.BufferUtils;

/**
 * A 2D Circle, defined by it's number of segments and radius. Values by default: radius = 1 and number of segments = 5.
 * 
 * @author H
 */
public class CircleDebug extends Mesh {

    private static final int DEFAULT_SEGMENT_NUMBER = 5;
    private static final float DEFAULT_RADIUS = 1f;

    private int segmentNumber;
    private float radius;

    /**
     * Default constructor for serialization only. Do not use.
     */
    public CircleDebug() {
    }

    /**
     * Main constructor to build an instance of a circle.
     * 
     * @param radius
     *            the radius of the circle.
     * @param segmentNumber
     *            the number of segments of the circle.
     */
    public CircleDebug(final float radius, final int segmentNumber) {
        super();
        setMode(Mode.LineLoop);
        updateGeometry(radius, segmentNumber);
    }

    /**
     * Return the number of segments of the circle.
     * 
     * @return number of segments of the circle.
     */
    public int getSegmentNumber() {
        return this.segmentNumber;
    }

    /**
     * Returns the radius of the circle.
     * 
     * @return radius of the circle.
     */
    public float getRadius() {
        return this.radius;
    }

    /**
     * Rebuilds the circle based on a new set of parameters.
     * 
     * @param radius
     *            the radius of the circle.
     * @param segmentNumber
     *            the number of segments of the circle.
     */
    public void updateGeometry(final float radius, final int segmentNumber) {
        // Update values
        this.radius = radius > 0 ? radius : DEFAULT_RADIUS;
        this.segmentNumber = segmentNumber > DEFAULT_SEGMENT_NUMBER ? segmentNumber : DEFAULT_SEGMENT_NUMBER;

        // Create buffers
        this.setBuffer(Type.Position, 3,
                BufferUtils.createVector3Buffer(getFloatBuffer(Type.Position), this.segmentNumber));

        this.setBuffer(Type.Index, 1, BufferUtils.createShortBuffer(getShortBuffer(Type.Index), this.segmentNumber));

        final FloatBuffer pb = getFloatBuffer(Type.Position);
        final IndexBuffer ib = getIndexBuffer();

        final float invSegmentNum = 1 / (float) this.segmentNumber;

        for (int i = 0; i < this.segmentNumber; i++) {
            // get the current angle
            final float theta = FastMath.TWO_PI * i * invSegmentNum;

            final float x = this.radius * FastMath.cos(theta);
            final float y = this.radius * FastMath.sin(theta);

            // Set vertices
            pb.put(x).put(y).put(0);

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
        this.radius = capsule.readFloat("radius", DEFAULT_RADIUS);
        this.segmentNumber = capsule.readInt("segmentNumber", DEFAULT_SEGMENT_NUMBER);
    }

    /**
     * @see Mesh#write(JmeExporter).
     */
    @Override
    public void write(final JmeExporter e) throws IOException {
        super.write(e);
        final OutputCapsule capsule = e.getCapsule(this);
        capsule.write(this.radius, "radius", DEFAULT_RADIUS);
        capsule.write(this.segmentNumber, "segmentNumber", DEFAULT_SEGMENT_NUMBER);
    }

}
