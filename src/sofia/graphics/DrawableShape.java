package sofia.graphics;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

//-------------------------------------------------------------------------
/**
 * A shape that is rendered using an Android {@link Drawable} object.
 * 
 * @author  Tony Allevato
 * @author  Last changed by $Author$
 * @version $Date$
 */
public class DrawableShape extends Shape
{
	//~ Instance/static variables .............................................

	private Drawable drawable;


	//~ Constructors ..........................................................

	// ----------------------------------------------------------
	/**
	 * Creates a new {@code DrawableShape} with the specified drawable and
	 * bounds.
	 * 
	 * @param drawable the {@link Drawable}
	 * @param bounds the bounding rectangle
	 */
	public DrawableShape(Drawable drawable, RectF bounds)
	{
		this.drawable = drawable;

		setBounds(bounds);
	}


	//~ Methods ...............................................................

	// ----------------------------------------------------------
	/**
	 * Gets the drawable rendered by this shape.
	 * 
	 * @return the drawable rendered by this shape
	 */
	public Drawable getDrawable()
	{
		return drawable;
	}


	// ----------------------------------------------------------
	/**
	 * Sets the drawable rendered by this shape.
	 * 
	 * @param newDrawable the drawable to be rendered by this shape
	 */
	public void setDrawable(Drawable newDrawable)
	{
		drawable = newDrawable;
	}


	// ----------------------------------------------------------
	@Override
	public void draw(Canvas canvas)
	{
		if (drawable != null)
		{
			drawable.setBounds((int) getX(), (int) getY(),
					(int) getX2(), (int) getY2());
			drawable.draw(canvas);
		}
	}
}
