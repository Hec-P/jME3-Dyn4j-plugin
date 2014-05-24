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
 * A 2D Capsule, defined by it's width, height and number of segments. Values by default: width = 1, height = 1 and
 * number of segments = 6.
 * 
 * @author H
 */
public class CapsuleDebug extends Mesh {

    private static final int DEFAULT_SEGMENT_NUMBER = 6;
    private static final float DEFAULT_WIDTH = 1f;
    private static final float DEFAULT_HEIGHT = 1f;

    private int segmentNumber;
    private float width;
    private float height;

    /**
     * Default constructor for serialization only. Do not use.
     */
    public CapsuleDebug() {
    }

    /**
     * Main constructor to build an instance of a capsule.
     * 
     * @param width
     *            the width of segments of the capsule.
     * @param height
     *            the height of the capsule.
     * @param segmentNumber
     *            the number of segments of the capsule.
     */
    public CapsuleDebug(final float width, final float height, final int segmentNumber) {
        super();
        setMode(Mode.LineLoop);
        updateGeometry(width, height, segmentNumber);
    }

    /**
     * Return the number of segments of the capsule.
     * 
     * @return number of segments of the capsule.
     */
    public int getSegmentNumber() {
        return this.segmentNumber;
    }

    /**
     * Return the width of the capsule.
     * 
     * @return width of the capsule.
     */
    public float getWidth() {
        return this.width;
    }

    /**
     * Returns the height of the capsule.
     * 
     * @return height of the capsule.
     */
    public float getHeight() {
        return this.height;
    }

    /**
     * Rebuilds the capsule based on a new set of parameters.
     * 
     * @param width
     *            the width of segments of the capsule.
     * @param height
     *            the height of the capsule.
     * @param segmentNumber
     *            the number of segments of the capsule.
     */
    public void updateGeometry(final float width, final float height, final int segmentNumber) {
        // Update values
        this.width = width > 0 ? width : DEFAULT_WIDTH;
        this.height = height > 0 ? height : DEFAULT_HEIGHT;
        this.segmentNumber = segmentNumber > DEFAULT_SEGMENT_NUMBER ? segmentNumber : DEFAULT_SEGMENT_NUMBER;

        // Create buffers
        this.setBuffer(Type.Position, 3,
                BufferUtils.createVector3Buffer(getFloatBuffer(Type.Position), this.segmentNumber));

        this.setBuffer(Type.Index, 1, BufferUtils.createShortBuffer(getShortBuffer(Type.Index), this.segmentNumber));

        final FloatBuffer pb = getFloatBuffer(Type.Position);
        final IndexBuffer ib = getIndexBuffer();

        final int halfSegmentNumber = this.segmentNumber / 2 - 1;
        final float halfWidth = this.width / 2 - this.height;

        final float segmentRadStep = FastMath.DEG_TO_RAD * 180 / halfSegmentNumber;

        // Calculate right half of the capsule.
        calculateHalfCapsule(pb, ib, halfSegmentNumber, halfWidth, segmentRadStep, 0, 0);

        // Calculate left half of the capsule.
        calculateHalfCapsule(pb, ib, halfSegmentNumber, -halfWidth, segmentRadStep, halfSegmentNumber,
                halfSegmentNumber + 1);

        updateBound();
    }

    private void calculateHalfCapsule(final FloatBuffer pb, final IndexBuffer ib, final int halfSegmentNumber,
            final float halfWidth, final float segmentRadStep, final int angleIdx, final int idx) {
        for (int i = 0; i <= halfSegmentNumber; i++) {
            // get the current angle
            final float rad = (angleIdx + i) * segmentRadStep;

            final float x = this.height * FastMath.cos(rad);
            final float y = this.height * FastMath.sin(rad) + halfWidth;

            // Set vertices
            pb.put(x).put(y).put(0);

            // Set index
            ib.put(idx + i, idx + i);
        }
    }

    /**
     * @see Mesh#read(JmeImporter).
     */
    @Override
    public void read(final JmeImporter e) throws IOException {
        super.read(e);
        final InputCapsule capsule = e.getCapsule(this);
        this.width = capsule.readFloat("width", DEFAULT_WIDTH);
        this.height = capsule.readFloat("height", DEFAULT_HEIGHT);
        this.segmentNumber = capsule.readInt("segmentNumber", DEFAULT_SEGMENT_NUMBER);
    }

    /**
     * @see Mesh#write(JmeExporter).
     */
    @Override
    public void write(final JmeExporter e) throws IOException {
        super.write(e);
        final OutputCapsule capsule = e.getCapsule(this);
        capsule.write(this.width, "width", DEFAULT_WIDTH);
        capsule.write(this.height, "height", DEFAULT_HEIGHT);
        capsule.write(this.segmentNumber, "segmentNumber", DEFAULT_SEGMENT_NUMBER);
    }

}
