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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.dyn4j.dynamics.joint.DistanceJoint;
import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.dynamics.joint.MouseJoint;
import org.dyn4j.dynamics.joint.PrismaticJoint;
import org.dyn4j.dynamics.joint.PulleyJoint;
import org.dyn4j.dynamics.joint.RevoluteJoint;
import org.dyn4j.dynamics.joint.WeldJoint;
import org.dyn4j.geometry.Capsule;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Ellipse;
import org.dyn4j.geometry.HalfEllipse;
import org.dyn4j.geometry.Slice;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.geometry.Wound;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.physics.dyn4j.Converter;
import com.jme3.physics.dyn4j.debug.control.Dyn4jDistanceJointDebugControl;
import com.jme3.physics.dyn4j.debug.control.Dyn4jJointDebugControl;
import com.jme3.physics.dyn4j.debug.control.Dyn4jMouseJointDebugControl;
import com.jme3.physics.dyn4j.debug.control.Dyn4jPrismaticJointDebugControl;
import com.jme3.physics.dyn4j.debug.control.Dyn4jPulleyJointDebugControl;
import com.jme3.physics.dyn4j.debug.control.Dyn4jRevoluteJointDebugControl;
import com.jme3.physics.dyn4j.debug.control.Dyn4jWeldJointDebugControl;
import com.jme3.physics.dyn4j.debug.shape.CapsuleDebug;
import com.jme3.physics.dyn4j.debug.shape.CircleDebug;
import com.jme3.physics.dyn4j.debug.shape.HalfEllipseDebug;
import com.jme3.physics.dyn4j.debug.shape.SliceDebug;
import com.jme3.physics.dyn4j.debug.shape.WoundDebug;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;

/**
 * 
 * @author H
 */
public class Dyn4jDebugShapeFactory {

    private static final int CIRCLE_SEGMENT_NUMBER = 24;

    private static final Logger logger = Logger.getLogger(Dyn4jDebugShapeFactory.class.getName());

    protected Map<PhysicDebugColor, Material> debugMaterials = null;

    public Dyn4jDebugShapeFactory(final AssetManager assetManager) {
        this.debugMaterials = new HashMap<PhysicDebugColor, Material>();

        for (final PhysicDebugColor physicDebugColor : PhysicDebugColor.values()) {
            createUnshadedMaterial(assetManager, physicDebugColor);
        }
    }

    private void createUnshadedMaterial(final AssetManager assetManager, final PhysicDebugColor physicDebugColor) {
        final Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(physicDebugColor.isWireFrame());
        mat.setColor("Color", physicDebugColor.getColor());

        this.debugMaterials.put(physicDebugColor, mat);
    }

    public Node getDebugShape(final Convex shape) {

        final Node node = new Node(shape.getId().toString());
        node.attachChild(createOriginAxes(shape.getCenter()));

        if (shape instanceof Wound) {
            final Wound wound = (Wound) shape;

            final Vector3f[] vertices = Converter.toVector3f(wound.getVertices());
            final WoundDebug woundDebug = new WoundDebug(vertices);

            node.attachChild(new Geometry(shape.getId().toString(), woundDebug));

        } else if (shape instanceof Circle) {
            final Circle circle = (Circle) shape;

            final float radius = Converter.toFloat(circle.getRadius());
            final CircleDebug circleDebug = new CircleDebug(radius, CIRCLE_SEGMENT_NUMBER);

            node.attachChild(new Geometry(shape.getId().toString(), circleDebug));

        } else if (shape instanceof Capsule) {
            final Capsule capsule = (Capsule) shape;

            final float width = Converter.toFloat(capsule.getLength());
            final float height = Converter.toFloat(capsule.getCapRadius());

            final CapsuleDebug capsuleDebug = new CapsuleDebug(width, height, CIRCLE_SEGMENT_NUMBER);
            final Geometry capsuleGeom = new Geometry(shape.getId().toString(), capsuleDebug);
            node.attachChild(capsuleGeom);

        } else if (shape instanceof Ellipse) {
            final Ellipse ellipse = (Ellipse) shape;

            final float scaleX = Converter.toFloat(ellipse.getWidth());
            final float scaleY = Converter.toFloat(ellipse.getHeight());

            final CircleDebug ellipseDebug = new CircleDebug(CIRCLE_SEGMENT_NUMBER, 1);

            final Geometry ellipseGeom = new Geometry(shape.getId().toString(), ellipseDebug);
            ellipseGeom.scale(scaleX, scaleY, 1);
            node.attachChild(ellipseGeom);

        } else if (shape instanceof HalfEllipse) {
            final HalfEllipse halfEllipse = (HalfEllipse) shape;

            // The width of Dyn4j's halfEllipse is diameter on x axis. I need the radius on x axis (halfWidth).
            final float width = Converter.toFloat(halfEllipse.getHalfWidth());

            // the height of Dyn4j's halfEllipse is the radius on y axis.
            final float height = Converter.toFloat(halfEllipse.getHeight());
            final HalfEllipseDebug halfEllipseDebug = new HalfEllipseDebug(width, height, CIRCLE_SEGMENT_NUMBER / 2);

            final Geometry halfEllipseGeom = new Geometry(shape.getId().toString(), halfEllipseDebug);
            node.attachChild(halfEllipseGeom);

        } else if (shape instanceof Slice) {
            final Slice slice = (Slice) shape;

            final float radius = Converter.toFloat(slice.getSliceRadius());
            final float angle = Converter.toFloat(slice.getTheta());
            final int segmentNumber = Math.round(angle * CIRCLE_SEGMENT_NUMBER / FastMath.TWO_PI);

            final SliceDebug sliceDebug = new SliceDebug(radius, angle, segmentNumber);

            final Geometry sliceGeom = new Geometry(shape.getId().toString(), sliceDebug);
            node.attachChild(sliceGeom);

        } else {
            logger.warning(String.format("#### Shape '%s' not supported. ####", shape.getClass().getSimpleName()));
        }

        return node;
    }

    public Material getDebugMaterial(final PhysicDebugColor physicDebugColor) {
        return this.debugMaterials.get(physicDebugColor);
    }

    public Dyn4jJointDebugControl getJointDebugControl(final Dyn4jDebugAppState dyn4jDebugAppState, final Joint joint) {
        Dyn4jJointDebugControl jointControl = null;

        if (joint instanceof DistanceJoint) {
            jointControl = new Dyn4jDistanceJointDebugControl(dyn4jDebugAppState, joint);
        } else if (joint instanceof RevoluteJoint) {
            jointControl = new Dyn4jRevoluteJointDebugControl(dyn4jDebugAppState, joint);
        } else if (joint instanceof MouseJoint) {
            jointControl = new Dyn4jMouseJointDebugControl(dyn4jDebugAppState, joint);
        } else if (joint instanceof WeldJoint) {
            jointControl = new Dyn4jWeldJointDebugControl(dyn4jDebugAppState, joint);
        } else if (joint instanceof PrismaticJoint) {
            jointControl = new Dyn4jPrismaticJointDebugControl(dyn4jDebugAppState, joint);
        } else if (joint instanceof PulleyJoint) {
            jointControl = new Dyn4jPulleyJointDebugControl(dyn4jDebugAppState, (PulleyJoint) joint);
        } else {
            logger.warning(String.format("#### Joint '%s' not supported. ####", joint.getClass().getSimpleName()));
        }

        return jointControl;
    }

    private Node createOriginAxes(final Vector2 center) {
        final Node node = new Node("Origin");
        node.attachChild(createAxisArrow(Vector3f.UNIT_X.mult(.25f), PhysicDebugColor.RED));
        node.attachChild(createAxisArrow(Vector3f.UNIT_Y.mult(.25f), PhysicDebugColor.GREEN));
        node.setLocalTranslation(Converter.toVector3f(center));

        return node;
    }

    private Spatial createAxisArrow(final Vector3f direction, final PhysicDebugColor color) {
        final Arrow axis = new Arrow(direction);

        final Geometry axisGeomg = new Geometry("axis", axis);
        axisGeomg.setMaterial(getDebugMaterial(color));

        return axisGeomg;
    }

}
