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
 * A 2D half ellipse, defined by it's width (radius on x axis), height (radius on y axis) and number of segments. Values
 * by default: width = 1, height = 0.5 and number of segments = 5.
 * 
 * @author H
 */
public class HalfEllipseDebug extends Mesh {

    private static final float DEFAULT_WIDTH = 1f;
    private static final float DEFAULT_HEIGHT = .5f;
    private static final int DEFAULT_SEGMENT_NUMBER = 5;

    private float width;
    private float height;
    private int segmentNumber;

    /**
     * Default constructor for serialization only. Do not use.
     */
    public HalfEllipseDebug() {
    }

    /**
     * Main constructor to build an instance of a half ellipse.
     * 
     * @param width
     *            the width (radius on x axis) of the half ellipse.
     * @param height
     *            the height (radius on y axis) of the half ellipse.
     * @param segmentNumber
     *            the number of segments.
     */
    public HalfEllipseDebug(final float width, final float height, final int segmentNumber) {
        super();
        setMode(Mode.LineLoop);
        updateGeometry(width, height, segmentNumber);
    }

    /**
     * Return the width (radius on x axis) of the half ellipse.
     * 
     * @return width (radius on x axis) of the half ellipse.
     */
    public float getWidth() {
        return this.width;
    }

    /**
     * Returns the height (radius on y axis) of the half ellipse.
     * 
     * @return height (radius on y axis) of the half ellipse.
     */
    public float getHeight() {
        return this.height;
    }

    /**
     * Return the number of segments of the half ellipse.
     * 
     * @return number of segments of the half ellipse.
     */
    public int getSegmentNumber() {
        return this.segmentNumber;
    }

    /**
     * Rebuilds the half ellipse based on a new set of parameters.
     * 
     * @param width
     *            the width (radius on x axis) of the half ellipse.
     * @param height
     *            the height (radius on y axis) of the half ellipse.
     * @param segmentNumber
     *            the number of segments of the half ellipse.
     */
    public void updateGeometry(final float width, final float height, final int segmentNumber) {
        // Update values
        this.width = width > 0 ? width : DEFAULT_WIDTH;
        this.height = height > 0 ? height : DEFAULT_HEIGHT;
        this.segmentNumber = segmentNumber > DEFAULT_SEGMENT_NUMBER ? segmentNumber : DEFAULT_SEGMENT_NUMBER;

        // Create buffers
        this.setBuffer(Type.Position, 3,
                BufferUtils.createVector3Buffer(getFloatBuffer(Type.Position), this.segmentNumber + 1));

        this.setBuffer(Type.Index, 1, BufferUtils.createShortBuffer(getShortBuffer(Type.Index), this.segmentNumber + 1));

        final FloatBuffer pb = getFloatBuffer(Type.Position);
        final IndexBuffer ib = getIndexBuffer();

        final float segmentRadStep = FastMath.DEG_TO_RAD * 180 / this.segmentNumber;

        for (int i = 0; i <= this.segmentNumber; i++) {
            // get the current angle
            final float rad = i * segmentRadStep;

            final float x = this.width * FastMath.cos(rad);
            final float y = this.height * FastMath.sin(rad);

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
