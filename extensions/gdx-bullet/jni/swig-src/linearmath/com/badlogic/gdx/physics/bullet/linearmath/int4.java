/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.10
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.badlogic.gdx.physics.bullet.linearmath;

import com.badlogic.gdx.physics.bullet.BulletBase;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;

public class int4 extends BulletBase {
	private long swigCPtr;
	
	protected int4(final String className, long cPtr, boolean cMemoryOwn) {
		super(className, cPtr, cMemoryOwn);
		swigCPtr = cPtr;
	}
	
	/** Construct a new int4, normally you should not need this constructor it's intended for low-level usage. */ 
	public int4(long cPtr, boolean cMemoryOwn) {
		this("int4", cPtr, cMemoryOwn);
		construct();
	}
	
	@Override
	protected void reset(long cPtr, boolean cMemoryOwn) {
		if (!destroyed)
			destroy();
		super.reset(swigCPtr = cPtr, cMemoryOwn);
	}
	
	public static long getCPtr(int4 obj) {
		return (obj == null) ? 0 : obj.swigCPtr;
	}

	@Override
	protected void finalize() throws Throwable {
		if (!destroyed)
			destroy();
		super.finalize();
	}

  @Override protected synchronized void delete() {
		if (swigCPtr != 0) {
			if (swigCMemOwn) {
				swigCMemOwn = false;
				LinearMathJNI.delete_int4(swigCPtr);
			}
			swigCPtr = 0;
		}
		super.delete();
	}

  public void setX(int value) {
    LinearMathJNI.int4_x_set(swigCPtr, this, value);
  }

  public int getX() {
    return LinearMathJNI.int4_x_get(swigCPtr, this);
  }

  public void setY(int value) {
    LinearMathJNI.int4_y_set(swigCPtr, this, value);
  }

  public int getY() {
    return LinearMathJNI.int4_y_get(swigCPtr, this);
  }

  public void setZ(int value) {
    LinearMathJNI.int4_z_set(swigCPtr, this, value);
  }

  public int getZ() {
    return LinearMathJNI.int4_z_get(swigCPtr, this);
  }

  public void setW(int value) {
    LinearMathJNI.int4_w_set(swigCPtr, this, value);
  }

  public int getW() {
    return LinearMathJNI.int4_w_get(swigCPtr, this);
  }

  public int4() {
    this(LinearMathJNI.new_int4__SWIG_0(), true);
  }

  public int4(int _x, int _y, int _z, int _w) {
    this(LinearMathJNI.new_int4__SWIG_1(_x, _y, _z, _w), true);
  }

}
