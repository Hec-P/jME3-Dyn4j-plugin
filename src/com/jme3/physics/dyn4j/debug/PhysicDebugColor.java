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

import com.jme3.math.ColorRGBA;

/**
 * 
 * @author H
 */
public enum PhysicDebugColor {

    /** Color for asleep bodies */
    BLUE(ColorRGBA.Blue),

    /** Color for anchors from active joints for filled square representation */
    BROWN_FILLED(ColorRGBA.Brown, false),

    /** Color for active Mouse joints */
    RED(ColorRGBA.Red),

    /** Color for inactive joints */
    GREEN(ColorRGBA.Green),

    /** Color for active joints */
    YELLOW(ColorRGBA.Yellow),

    /** Color for active bodies. */
    MAGENTA(ColorRGBA.Magenta),

    /** Color for anchors from inactive joints for filled circle representation */
    PINK_FILLED(ColorRGBA.Pink, false),

    /** Color for anchors from active joints for filled circle representation */
    ORANGE_FILLED(ColorRGBA.Orange, false),

    /** Color for anchors from inactive joints */
    PINK(ColorRGBA.Pink),

    /** Color for anchors from active joints */
    ORANGE(ColorRGBA.Orange),

    /** Color for inactive bodies */
    GRAY(ColorRGBA.Gray);

    private ColorRGBA color = null;
    private boolean wireFrame = true;

    private PhysicDebugColor(final ColorRGBA color) {
        this(color, true);
    }

    private PhysicDebugColor(final ColorRGBA color, final boolean isWireFrame) {
        this.color = color;
        this.wireFrame = isWireFrame;
    }

    public ColorRGBA getColor() {
        return this.color;
    }

    public boolean isWireFrame() {
        return this.wireFrame;
    }
}
