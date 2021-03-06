/*
 * Copyright (C) 2011 Virginia Tech Department of Computer Science
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sofia.graphics;

import org.jbox2d.dynamics.World;

//-------------------------------------------------------------------------
/**
 * The abstract base class for all Sofia classes representing JBox2D joints.
 * Most users will not need to use this class directly, unless they want to
 * implement a type of joint that Sofia does not yet support.
 *
 * @param <JointType> the type of the JBox2D joint that this class represents
 * @param <JointDefType> the type of the JBox2D joint definition that this
 *     class represents
 *
 * @author Tony Allevato
 */
public abstract class AbstractJoint<
    JointType extends org.jbox2d.dynamics.joints.Joint,
    JointDefType extends org.jbox2d.dynamics.joints.JointDef>
    implements Joint
{
    //~ Fields ................................................................

    private JointType b2Joint;
    private JointDefType b2JointDef;

    private Shape firstShape;
    private Shape secondShape;
    private boolean collideConnected;


    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Initializes a new joint with the specified shapes.
     *
     * @param firstShape  the first shape connected by this joint
     * @param secondShape the second shape connected by this joint
     */
    public AbstractJoint(Shape firstShape, Shape secondShape)
    {
        this.firstShape = firstShape;
        this.secondShape = secondShape;
    }


    //~ Public methods ........................................................

    // ----------------------------------------------------------
    /**
     * Gets the first shape connected by this joint.
     *
     * @return the first shape connected by this joint
     */
    public Shape getFirstShape()
    {
        return firstShape;
    }


    // ----------------------------------------------------------
    /**
     * Gets the second shape connected by this joint.
     *
     * @return the second shape connected by this joint
     */
    public Shape getSecondShape()
    {
        return secondShape;
    }


    // ----------------------------------------------------------
    /**
     * Gets a value indicating whether the two shapes connected by this joint
     * are allowed to collide.
     *
     * @return true if the two shapes connected by this joint are allowed to
     *     collide, otherwise false
     */
    public boolean canShapesCollide()
    {
        return collideConnected;
    }


    // ----------------------------------------------------------
    /**
     * Sets a value indicating whether the two shapes connected by this joint
     * are allowed to collide.
     *
     * @param collide true if the two shapes connected by this joint are
     *     allowed to collide, otherwise false
     */
    public void setCanShapesCollide(boolean collide)
    {
        collideConnected = collide;
    }


    // ----------------------------------------------------------
    /**
     * Activates the joint. You must call this method after creating the joint
     * object if you want it to have any effect.
     */
    public void connect()
    {
        if (b2Joint != null)
        {
            return;
        }

        if (firstShape == null || secondShape == null)
        {
            throw new IllegalStateException("The shapes being connected by "
                    + "the joint must be non-null.");
        }

        ShapeField firstField = firstShape.getShapeField();
        ShapeField secondField = secondShape.getShapeField();

        if (firstField == null || secondField == null)
        {
            throw new IllegalStateException("The shapes being connected by "
                    + "the joint must be added to a ShapeField.");
        }
        if (firstField != secondField)
        {
            throw new IllegalStateException("The shapes being connected by "
                    + "the joint must be in the same ShapeField.");
        }
        else
        {
            createJoint();
        }
    }


    // ----------------------------------------------------------
    /**
     * Deactivates the joint, releasing the connection between the two shapes.
     */
    public void disconnect()
    {
        if (b2Joint != null)
        {
            b2Joint.m_bodyA.m_world.destroyJoint(b2Joint);
            b2Joint = null;
        }
    }


    // ----------------------------------------------------------
    /**
     * Gets the underlying JBox2D joint object. For advanced usage only.
     *
     * @return the underlying JBox2D joint object
     */
    public JointType getB2Joint()
    {
        return b2Joint;
    }


    // ----------------------------------------------------------
    /**
     * Gets the underlying JBox2D joint definition object. For advanced usage
     * only.
     *
     * @return the underlying JBox2D joint definition object
     */
    public JointDefType getB2JointDef()
    {
        if (b2JointDef == null)
        {
            b2JointDef = createB2JointDef();
            b2JointDef.collideConnected = collideConnected;
            b2JointDef.userData = this;
        }

        return b2JointDef;
    }


    //~ Protected methods .....................................................

    // ----------------------------------------------------------
    /**
     * Subclasses must override this method to create the appropriate Box2D
     * {@code JointDef} instance that represents the specific type of joint.
     * This method should fill in all required properties of the joint,
     * including the {@code bodyA} and {@code bodyB} references.
     *
     * @return the subclass of {@code JointDef} that represents this specific
     *     type of joint
     */
    protected abstract JointDefType createB2JointDef();


    //~ Private methods .......................................................

    // ----------------------------------------------------------
    /**
     * Creates the actual joint.
     */
    @SuppressWarnings("unchecked")
    private void createJoint()
    {
        World world = firstShape.getShapeField().getB2World();
        b2Joint = (JointType) world.createJoint(getB2JointDef());
    }
}
