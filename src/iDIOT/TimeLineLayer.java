package iDIOT;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.text.SimpleDateFormat;
import java.util.Date;


import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import gov.nasa.worldwind.View;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.geom.Intersection;
import gov.nasa.worldwind.geom.Line;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.layers.AbstractLayer;
import gov.nasa.worldwind.pick.PickSupport;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.OrderedRenderable;
import gov.nasa.worldwind.render.TextRenderer;
import gov.nasa.worldwind.util.Logging;
import gov.nasa.worldwind.util.OGLTextRenderer;
import gov.nasa.worldwind.view.orbit.OrbitView;


	public class TimeLineLayer extends AbstractLayer implements  SelectListener
	{
	    // Follow constants
	    public final static String FOLLOW_VIEW = "gov.nasa.worldwind.TerrainProfileLayer.FollowView";
	    public final static String FOLLOW_EYE = "gov.nasa.worldwind.TerrainProfileLayer.FollowEye";
	    public final static String FOLLOW_CURSOR = "gov.nasa.worldwind.TerrainProfileLayer.FollowCursor";
	    public final static String FOLLOW_NONE = "gov.nasa.worldwind.TerrainProfileLayer.FollowNone";
	    public final static String FOLLOW_PATH = "gov.nasa.worldwind.TerrainProfileLayer.FollowPath";
	    // Graph size when minimized
	    protected final static int MINIMIZED_SIZE = 32;

	    // GUI pickable objects
	    protected final String buttonMinimize = "iDIOT.TimeLineLayer.ButtonMinimize";
	    protected final String buttonMaximize = "iDIOT.TimeLineLayer.ButtonMaximize";
	    protected final String setButton = "iDIOT.TimeLineLayer.setButton";
	    
	    // Display parameters
	    protected Dimension size = new Dimension(400, 100);
	    protected Color color = Color.white;
	    protected int borderWidth = 20;
	    protected String position = AVKey.SOUTHEAST;
	    protected String resizeBehavior = AVKey.RESIZE_SHRINK_ONLY;
	    protected Font defaultFont = Font.decode("Arial-PLAIN-12");
	    protected double toViewportScale = 1;
	    protected Point locationCenter = null;
	    protected Vec4 locationOffset = null;
	    protected PickSupport pickSupport = new PickSupport();
	    protected boolean initialized = false;
	    protected boolean isMinimized = false;     // True when graph is minimized to an icon
	    protected boolean isMaximized = false;     // True when graph is 'full screen'
	    protected int pickedSample = -1;           // Picked sample number if not -1
	    protected boolean keepProportions = false; // Keep graph distance/elevation proportions
	    protected String follow = FOLLOW_VIEW;     // Profile position follow behavior
	    protected double profileLengthFactor = 1;  // Applied to default profile length (zoom on profile)	    
	    
	    // TimeLine data
	    protected int samples = 250;              	// Number of position samples	    
	    protected double length;                  	// Profile length along great circle in meter
	    protected Date firstDate;					// first date to display in the Time-line
	    protected Date lastDate;					// last date to display in the Time-line
	    protected Date pickedTime;					// computed time on the Time-line

	    // Worldwind
	    protected WorldWindow wwd;

	    // Draw it as ordered with an eye distance of 0 so that it shows up in front of most other things.
	    protected OrderedIcon orderedImage = new OrderedIcon();

	    protected class OrderedIcon implements OrderedRenderable
	    {
	        public double getDistanceFromEye()
	        {
	            return 0;
	        }

	        public void pick(DrawContext dc, Point pickPoint)
	        {
	            TimeLineLayer.this.drawTimeline(dc);
	        }

	        public void render(DrawContext dc)
	        {
	            TimeLineLayer.this.drawTimeline(dc);
	        }
	    }
	    
	    protected TimeLineListener listener;

	    /** Renders a terrain profile graphic in a screen corner. */
	    public TimeLineLayer()
	    {
	    }

	    // ** Public properties ************************************************************

	    
	    public void addListener(TimeLineListener myListener) {
	    	this.listener = myListener;
	    }
	    /**
	     * Get whether the profile graph is minimized.
	     *
	     * @return true if the profile graph is minimized.
	     */
	    @SuppressWarnings({})
	    public boolean getIsMinimized()
	    {
	        return this.isMinimized;
	    }

	    /**
	     * Set whether the profile graph should be minimized. <p>Note that the graph can be both minimized and maximized at
	     * the same time. The minimized state will take precedence and the graph will display as an icon. When
	     * 'un-minimized' it will display as maximized.</p>
	     *
	     * @param state true if the profile should be minimized.
	     */
	    public void setIsMinimized(boolean state)
	    {
	        this.isMinimized = state;
	        this.pickedSample = -1;  // Clear picked position
	    }

	    /**
	     * Get whether the profile graph is maximized - displays over the whole viewport.
	     *
	     * @return true if the profile graph is maximized.
	     */
	    public boolean getIsMaximized()
	    {
	        return this.isMaximized;
	    }

	    /**
	     * Set whether the profile graph should be maximized - displays over the whole viewport.
	     *
	     * @param state true if the profile should be maximized.
	     */
	    public void setIsMaximized(boolean state)
	    {
	        this.isMaximized = state;
	    }  
	 
	    /**
	     * Set the graphic Color.
	     *
	     * @param color the graphic Color.
	     */
	    public void setColor(Color color)
	    {
	        if (color == null)
	        {
	            String msg = Logging.getMessage("nullValue.ColorIsNull");
	            Logging.logger().severe(msg);
	            throw new IllegalArgumentException(msg);
	        }
	        this.color = color;
	    }

	    /**
	     * Sets the layer opacity, which is applied to the layer's chart. The opacity is modified slightly for various
	     * elements of the chart to distinguish among them.
	     *
	     * @param opacity the current opacity value, which is ignored by this layer.
	     *
	     * @see #setColor
	     */
	    @Override
	    public void setOpacity(double opacity)
	    {
	        super.setOpacity(opacity);
	    }

	    /**
	     * Returns the layer's opacity value. The opacity is modified slightly for various elements of the chart to
	     * distinguish among them.
	     *
	     * @return The layer opacity, a value between 0 and 1.
	     *
	     * @see #getColor
	     */
	    @Override
	    public double getOpacity()
	    {
	        return super.getOpacity();
	    }

	    public String getPosition()
	    {
	        return this.position;
	    }

	    /**
	     * Sets the relative viewport location to display the graphic. Can be one of {@link AVKey#NORTHEAST}, {@link
	     * AVKey#NORTHWEST}, {@link AVKey#SOUTHEAST}, or {@link AVKey#SOUTHWEST} (the default). These indicate the corner of
	     * the viewport.
	     *
	     * @param position the desired graphic position.
	     */
	    public void setPosition(String position)
	    {
	        if (position == null)
	        {
	            String msg = Logging.getMessage("nullValue.PositionIsNull");
	            Logging.logger().severe(msg);
	            throw new IllegalArgumentException(msg);
	        }
	        this.position = position;
	    }

	    /**
	     * Get the screen location of the graph center if set (can be null).
	     *
	     * @return the screen location of the graph center if set (can be null).
	     */
	    public Point getLocationCenter()
	    {
	        return this.locationCenter;
	    }

	    /**
	     * Set the screen location of the graph center - overrides {@link #setPosition} if not null.
	     *
	     * @param point the screen location of the graph center (can be null).
	     */
	    public void setLocationCenter(Point point)
	    {
	        this.locationCenter = point;
	    }

	    /**
	     * Returns the current location offset. See {@link #setLocationOffset} for a description of the offset and its
	     * values.
	     *
	     * @return the location offset. Will be null if no offset has been specified.
	     */
	    public Vec4 getLocationOffset()
	    {
	        return locationOffset;
	    }

	    /**
	     * Specifies a placement offset from the layer position on the screen.
	     *
	     * @param locationOffset the number of pixels to shift the layer image from its specified screen position. A
	     *                       positive X value shifts the image to the right. A positive Y value shifts the image up. If
	     *                       null, no offset is applied. The default offset is null.
	     *
	     * @see #setLocationCenter
	     * @see #setPosition
	     */
	    public void setLocationOffset(Vec4 locationOffset)
	    {
	        this.locationOffset = locationOffset;
	    }

	    /**
	     * Returns the layer's resize behavior.
	     *
	     * @return the layer's resize behavior.
	     */
	    public String getResizeBehavior()
	    {
	        return resizeBehavior;
	    }

	    /**
	     * Sets the behavior the layer uses to size the graphic when the viewport size changes, typically when the World
	     * Wind window is resized. If the value is {@link AVKey#RESIZE_KEEP_FIXED_SIZE}, the graphic size is kept to the
	     * size specified in its Dimension scaled by the layer's current icon scale. If the value is {@link
	     * AVKey#RESIZE_STRETCH}, the graphic is resized to have a constant size relative to the current viewport size. If
	     * the viewport shrinks the graphic size decreases; if it expands then the graphic enlarges. If the value is {@link
	     * AVKey#RESIZE_SHRINK_ONLY} (the default), graphic sizing behaves as for {@link AVKey#RESIZE_STRETCH} but it will
	     * not grow larger than the size specified in its Dimension.
	     *
	     * @param resizeBehavior the desired resize behavior
	     */
	    public void setResizeBehavior(String resizeBehavior)
	    {
	        this.resizeBehavior = resizeBehavior;
	    }

	    public int getBorderWidth()
	    {
	        return borderWidth;
	    }

	    /**
	     * Sets the graphic offset from the viewport border.
	     *
	     * @param borderWidth the number of pixels to offset the graphic from the borders indicated by {@link
	     *                    #setPosition(String)}.
	     */
	    public void setBorderWidth(int borderWidth)
	    {
	        this.borderWidth = borderWidth;
	    }

	  
	    /**
	     * Get the graphic legend Font.
	     *
	     * @return the graphic legend Font.
	     */
	    public Font getFont()
	    {
	        return this.defaultFont;
	    }

	    /**
	     * Set the graphic legend Font.
	     *
	     * @param font the graphic legend Font.
	     */
	    public void setFont(Font font)
	    {
	        if (font == null)
	        {
	            String msg = Logging.getMessage("nullValue.FontIsNull");
	            Logging.logger().severe(msg);
	            throw new IllegalArgumentException(msg);
	        }
	        this.defaultFont = font;
	    }

	    /**
	     * Get whether distance/elevation proportions are maintained.
	     *
	     * @return true if the graph maintains distance/elevation proportions.
	     */
	    public boolean getKeepProportions()
	    {
	        return this.keepProportions;
	    }

	    /**
	     * Set whether distance/elevation proportions are maintained.
	     *
	     * @param state true if the graph should maintains distance/elevation proportions.
	     */
	    public void setKeepProportions(boolean state)
	    {
	        this.keepProportions = state;
	    }

	    public Date getFirstDate() {
			return firstDate;
		}

		public void setFirstDate(Date firstDate) {
			this.firstDate = firstDate;
		}

		public Date getLastDate() {
			return lastDate;
		}

		public void setLastDate(Date lastDate) {
			this.lastDate = lastDate;
		}
		
	    // Sets the wwd local reference and add us to the position listeners

	    public void setEventSource(WorldWindow wwd)
	    {
	        if (this.wwd != null)
	        {
	            this.wwd.removeSelectListener(this);
	        }
	        this.wwd = wwd;
	        if (this.wwd != null)
	        {	    
	            this.wwd.addSelectListener(this);    
	        }
	    }
	    
	    // ** Select listener impl. ************************************************************

	    public void selected(SelectEvent event)
	    {	        
	        if (event.hasObjects() && event.getEventAction().equals(SelectEvent.LEFT_CLICK))
	        {
	            if (event.getMouseEvent() != null && event.getMouseEvent().isConsumed())
	                return;

	            Object o = event.getTopObject();
	            if (o == this.buttonMinimize)
	            {
	                if (this.isMaximized)
	                    this.setIsMaximized(false);
	                else
	                    this.setIsMinimized(true);
	            }

	            else if (o == this.buttonMaximize)
	            {
	                this.setIsMaximized(true);
	            }

	            else if (o == this && this.isMinimized)
	            {
	                this.setIsMinimized(false);
	            }
	            
	            else if (o == this.setButton)
	            {
	            	this.listener.onTimeLineAdjusted(this.pickedTime);
	            }
	            
	        }
	    }
	    // *************************************************************************


		// ** Rendering ************************************************************
	    @Override
	    public void doRender(DrawContext dc)
	    {
	        // Delegate graph rendering to OrderedRenderable list
	        dc.addOrderedRenderable(this.orderedImage);
	    }

	    @Override
	    public void doPick(DrawContext dc, Point pickPoint)
	    {
	        dc.addOrderedRenderable(this.orderedImage);

	    }
	    
	    

	    protected void initialize(DrawContext dc)
	    {
	        if (this.initialized )
	            return;

	        if (this.wwd != null)
	        {
	        	this.computeProfileLength();
	        }

	        this.initialized = true;
	    }

	    // Profile graph rendering - ortho

	    public void drawTimeline(DrawContext dc)
	    {
	        this.computeProfileLength();

	        if (!this.initialized)
	            this.initialize(dc);

	        GL2 gl = dc.getGL().getGL2(); // GL initialization checks for GL2 compatibility.

	        boolean attribsPushed = false;
	        boolean modelviewPushed = false;
	        boolean projectionPushed = false;

	        try
	        {
	            gl.glPushAttrib(GL2.GL_DEPTH_BUFFER_BIT
	                | GL2.GL_COLOR_BUFFER_BIT
	                | GL2.GL_ENABLE_BIT
	                | GL2.GL_TRANSFORM_BIT
	                | GL2.GL_VIEWPORT_BIT
	                | GL2.GL_CURRENT_BIT);
	            attribsPushed = true;

	            gl.glDisable(GL.GL_TEXTURE_2D);        // no textures

	            gl.glEnable(GL.GL_BLEND);
	            gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
	            gl.glDisable(GL.GL_DEPTH_TEST);

	            Rectangle viewport = dc.getView().getViewport();
	            Dimension drawSize = isMinimized ? new Dimension(MINIMIZED_SIZE, MINIMIZED_SIZE) :
	                isMaximized ? new Dimension(viewport.width - this.borderWidth * 2,
	                    viewport.height * 2 / 3 - this.borderWidth * 2) : this.size;
	            double width = drawSize.width;
	            double height = drawSize.height;

	            // Load a parallel projection with xy dimensions (viewportWidth, viewportHeight)
	            // into the GL projection matrix.
	            
	            gl.glMatrixMode(GL2.GL_PROJECTION);
	            gl.glPushMatrix();
	            projectionPushed = true;
	            gl.glLoadIdentity();
	            double maxwh = width > height ? width : height;
	            gl.glOrtho(0d, viewport.width, 0d, viewport.height, -0.6 * maxwh, 0.6 * maxwh);

	            gl.glMatrixMode(GL2.GL_MODELVIEW);
	            gl.glPushMatrix();
	            modelviewPushed = true;
	            gl.glLoadIdentity();

	            // Scale to a width x height space
	            // located at the proper position on screen
	            double scale = this.computeScale(viewport);
	            Vec4 locationSW = this.computeLocation(viewport, scale);
	            gl.glTranslated(locationSW.x(), locationSW.y(), locationSW.z());
	            gl.glScaled(scale, scale, 1d);	            

	            Color color = dc.getUniquePickColor();
                int colorCode = color.getRGB();

	            if (!dc.isPickingMode())
	            {
	                // Draw grid - Set color using current layer opacity
	                this.drawGrid(dc, drawSize);

	                // Draw vertical line on the time-line
	                this.drawGraph(dc, drawSize);

	                if (!isMinimized)
	                {
	                    // Draw GUI buttons
	                    drawGUI(dc, drawSize,locationSW);	                    
	                }
	            }
	            else
	            {
	                // Picking
	                this.pickSupport.clearPickList();
	                this.pickSupport.beginPicking(dc);
	                // Draw unique color across the rectangle
	                 color = dc.getUniquePickColor();
	                 colorCode = color.getRGB();
	                if (!isMinimized)
	                {
	                    // Update graph's picked sample
	                    computePickPosition(dc, locationSW, new Dimension((int) (width * scale), (int) (height * scale)));
	                    // Draw GUI buttons
	                    drawGUI(dc, drawSize, locationSW);	                    
	                }
	                else
	                {
	                    // Add graph to the pickable list for 'un-minimize' click
	                    this.pickSupport.addPickableObject(colorCode, this);
	                    gl.glColor3ub((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
	                    gl.glBegin(GL2.GL_POLYGON);
	                    gl.glVertex3d(0, 0, 0);
	                    gl.glVertex3d(width, 0, 0);
	                    gl.glVertex3d(width, height, 0);
	                    gl.glVertex3d(0, height, 0);
	                    gl.glVertex3d(0, 0, 0);
	                    gl.glEnd();
	                }
	                // Done picking
	                this.pickSupport.endPicking(dc);
	                this.pickSupport.resolvePick(dc, dc.getPickPoint(), this);
	            }
	        }
	        catch (Exception e)
	        {
	            //e.printStackTrace();
	        }
	        finally
	        {
	            if (projectionPushed)
	            {
	                gl.glMatrixMode(GL2.GL_PROJECTION);
	                gl.glPopMatrix();
	            }
	            if (modelviewPushed)
	            {
	                gl.glMatrixMode(GL2.GL_MODELVIEW);
	                gl.glPopMatrix();
	            }
	            if (attribsPushed)
	                gl.glPopAttrib();
	        }
	    }

	    // Draw grid graphic

	    protected void drawGrid(DrawContext dc, Dimension dimension)
	    {            
	        // Background color
	        Color backColor = getBackgroundColor(this.color);            
	        drawFilledRectangle(dc, new Vec4(0, 0, 0), dimension, new Color(backColor.getRed(),
	            backColor.getGreen(), backColor.getBlue(), (int) (backColor.getAlpha() * .5))); // Increased transparency
	        // Grid - minimal
	        float[] colorRGB = this.color.getRGBColorComponents(null);
	        GL2 gl = dc.getGL().getGL2(); // GL initialization checks for GL2 compatibility.
	        gl.glColor4d(colorRGB[0], colorRGB[1], colorRGB[2], this.getOpacity());
	        drawVerticalLine(dc, dimension, 0);
	        drawVerticalLine(dc, dimension, dimension.getWidth());
	        drawHorizontalLine(dc, dimension, 0);
	    }

	    // Draw profile graphic

	    protected void drawGraph(DrawContext dc, Dimension dimension)
	    {
	        GL2 gl = dc.getGL().getGL2(); // GL initialization checks for GL2 compatibility.

	        double stepX = dimension.getWidth() / this.length;

	        double lengthStep = this.length / (this.samples - 1);
	        double x = 0;
	        // Filled graph
	        gl.glColor4ub((byte) this.color.getRed(), (byte) this.color.getGreen(),
	            (byte) this.color.getBlue(), (byte) 100);
	        gl.glBegin(GL2.GL_TRIANGLE_STRIP);
	         
	        gl.glEnd();
	        // Line graph
	        float[] colorRGB = this.color.getRGBColorComponents(null);
	        gl.glColor4d(colorRGB[0], colorRGB[1], colorRGB[2], this.getOpacity());
	        gl.glBegin(GL2.GL_LINE_STRIP);
	        
	        gl.glEnd();
	        // Middle vertical line
	        gl.glColor4d(colorRGB[0], colorRGB[1], colorRGB[2], this.getOpacity() * .3); // increased transparency here
	        if (!this.follow.equals(FOLLOW_PATH))
	            drawVerticalLine(dc, dimension, x / 2);
	        

            double pickedX = this.pickedSample * lengthStep * stepX;
            double pickedY = dimension.getHeight() / 2 ;
            gl.glColor4d(colorRGB[0], colorRGB[1], colorRGB[2] * .5, this.getOpacity() * .8); // yellower color
            drawVerticalLine(dc, dimension, pickedX);            
            
            SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
            drawLabel(dc, ft.format(this.pickedTime), new Vec4(pickedX + 5, pickedY - 12, 0), -1); // left aligned	           

	    }

	    protected void drawGUI(DrawContext dc, Dimension dimension, Vec4 locationSW )
	    {
	        GL2 gl = dc.getGL().getGL2(); // GL initialization checks for GL2 compatibility.
	        int buttonSize = 16;
	        int hs = buttonSize / 2;
	        int buttonBorder = 4;
	        Dimension buttonDimension = new Dimension(buttonSize, buttonSize);
	        Color highlightColor = new Color(color.getRed(), color.getGreen(),
	            color.getBlue(), (int) (color.getAlpha() * .5));
	        Color backColor = getBackgroundColor(this.color);
	        backColor = new Color(backColor.getRed(), backColor.getGreen(),
	            backColor.getBlue(), (int) (backColor.getAlpha() * .5)); // Increased transparency
	        Color drawColor;
	        int y = dimension.height - buttonDimension.height - buttonBorder;
	        int x = dimension.width;
	        Object pickedObject = dc.getPickedObjects() != null ? dc.getPickedObjects().getTopObject() : null;
	        // Maximize button
	        if (!isMaximized)
	        {
	            x -= buttonDimension.width + buttonBorder;
	            if (dc.isPickingMode())
	            {
	                drawColor = dc.getUniquePickColor();
	                int colorCode = drawColor.getRGB();
	                this.pickSupport.addPickableObject(colorCode, this.buttonMaximize, null, false);
	            }
	            else
	                drawColor = this.buttonMaximize == pickedObject ? highlightColor : backColor;
	            drawFilledRectangle(dc, new Vec4(x, y, 0), buttonDimension, drawColor);
	            if (!dc.isPickingMode())
	            {
	                gl.glColor4ub((byte) color.getRed(), (byte) color.getGreen(),
	                    (byte) color.getBlue(), (byte) color.getAlpha());
	                // Draw '+'
	                drawLine(dc, x + 3, y + hs, x + buttonDimension.width - 3, y + hs); // Horizontal line
	                drawLine(dc, x + hs, y + 3, x + hs, y + buttonDimension.height - 3); // Vertical line
	            }
	        }

	        // Minimize button
	        x -= buttonDimension.width + buttonBorder;
	        if (dc.isPickingMode())
	        {
	            drawColor = dc.getUniquePickColor();
	            int colorCode = drawColor.getRGB();
	            this.pickSupport.addPickableObject(colorCode, this.buttonMinimize, null, false);
	        }
	        else
	            drawColor = this.buttonMinimize == pickedObject ? highlightColor : backColor;
	        drawFilledRectangle(dc, new Vec4(x, y, 0), buttonDimension, drawColor);
	        if (!dc.isPickingMode())
	        {
	            gl.glColor4ub((byte) color.getRed(), (byte) color.getGreen(),
	                (byte) color.getBlue(), (byte) color.getAlpha());
	            // Draw '-'
	            drawLine(dc, x + 3, y + hs, x + buttonDimension.width - 3, y + hs); // Horizontal line
	        }
	        
	        // 'set' button 
	        Dimension buttonSetTimeDimension = new Dimension(buttonSize * 8, buttonSize);
	        x -= buttonDimension.width * 14 ;
	        y = - buttonDimension.height ;
	        if (dc.isPickingMode())
	        {
	            drawColor = dc.getUniquePickColor();
	            int colorCode = drawColor.getRGB();
	            this.pickSupport.addPickableObject(colorCode, this.setButton, null, false);
	        }
	        else
	            drawColor = this.setButton == pickedObject ? highlightColor : backColor;
	        drawFilledRectangle(dc, new Vec4(x, y, 0), buttonSetTimeDimension , drawColor);
	        if (!dc.isPickingMode())
	        {
	            gl.glColor4ub((byte) color.getRed(), (byte) color.getGreen(),
	                (byte) color.getBlue(), (byte) color.getAlpha());
	            // Draw 'set'
	            drawLabel(dc, "set", new Vec4(x + buttonSize*4, y +5   , 0), 0); // right aligned
	        }
	        
	        Rectangle viewport = dc.getView().getViewport();
            Dimension drawSize = isMinimized ? new Dimension(MINIMIZED_SIZE, MINIMIZED_SIZE) :
                isMaximized ? new Dimension(viewport.width - this.borderWidth * 2,
                    viewport.height * 2 / 3 - this.borderWidth * 2) : this.size;
            double width = drawSize.width;

	        
            // Draw labels for first & last date
            gl.glLoadIdentity();
            gl.glDisable(GL.GL_CULL_FACE);
            
            SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
            drawLabel(dc, ft.format(this.firstDate), locationSW.add3(new Vec4(0, -12, 0)), -1); // left aligned
            drawLabel(dc, ft.format(this.lastDate), locationSW.add3(new Vec4(width, -12, 0)), 1); // right aligned
	        
	    }

	    protected void drawHorizontalLine(DrawContext dc, Dimension dimension, double y)
	    {
	        drawLine(dc, 0, y, dimension.getWidth(), y);
	    }

	    protected void drawVerticalLine(DrawContext dc, Dimension dimension, double x)
	    {
	        drawLine(dc, x, 0, x, dimension.getHeight());
	    }

	    protected void drawFilledRectangle(DrawContext dc, Vec4 origin, Dimension dimension, Color color)
	    {
	        GL2 gl = dc.getGL().getGL2(); // GL initialization checks for GL2 compatibility.
	        gl.glColor4ub((byte) color.getRed(), (byte) color.getGreen(),
	            (byte) color.getBlue(), (byte) color.getAlpha());
	        gl.glDisable(GL.GL_TEXTURE_2D);        // no textures
	        gl.glBegin(GL2.GL_POLYGON);
	        gl.glVertex3d(origin.x, origin.y, 0);
	        gl.glVertex3d(origin.x + dimension.getWidth(), origin.y, 0);
	        gl.glVertex3d(origin.x + dimension.getWidth(), origin.y + dimension.getHeight(), 0);
	        gl.glVertex3d(origin.x, origin.y + dimension.getHeight(), 0);
	        gl.glVertex3d(origin.x, origin.y, 0);
	        gl.glEnd();
	    }

	    protected void drawLine(DrawContext dc, double x1, double y1, double x2, double y2)
	    {
	        GL2 gl = dc.getGL().getGL2(); // GL initialization checks for GL2 compatibility.
	        gl.glBegin(GL2.GL_LINE_STRIP);
	        gl.glVertex3d(x1, y1, 0);
	        gl.glVertex3d(x2, y2, 0);
	        gl.glEnd();
	    }

	    // Draw a text label
	    // Align = -1: left, 0: center and 1: right

	    protected void drawLabel(DrawContext dc, String text, Vec4 screenPoint, int align)
	    {
           
	        TextRenderer textRenderer = OGLTextRenderer.getOrCreateTextRenderer(dc.getTextRendererCache(),
	            this.defaultFont);

	        Rectangle2D nameBound = textRenderer.getBounds(text);
	        int x = (int) screenPoint.x();  // left
	        if (align == 0)
	            x = (int) (screenPoint.x() - nameBound.getWidth() / 2d);  // centered
	        if (align > 0)
	            x = (int) (screenPoint.x() - nameBound.getWidth());  // right
	        int y = (int) screenPoint.y();

	        textRenderer.begin3DRendering();

	        textRenderer.setColor(this.getBackgroundColor(this.color));
	        textRenderer.draw(text, x + 1, y - 1);
	        textRenderer.setColor(this.color);
	        textRenderer.draw(text, x, y);

	        textRenderer.end3DRendering();
	        
	    }

	    // Compute background color for best contrast

	    protected Color getBackgroundColor(Color color)
	    {
	        float[] compArray = new float[4];
	        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), compArray);
	        if (compArray[2] > 0.5)
	            return new Color(0, 0, 0, (int) (this.color.getAlpha() * 0.7f));
	        else
	            return new Color(255, 255, 255, (int) (this.color.getAlpha() * 0.7f));
	    }

	    // ** Dimensions and positionning ************************************************************

	    protected double computeScale(java.awt.Rectangle viewport)
	    {
	        if (this.resizeBehavior.equals(AVKey.RESIZE_SHRINK_ONLY))
	        {
	            return Math.min(1d, (this.toViewportScale) * viewport.width / this.size.width);
	        }
	        else if (this.resizeBehavior.equals(AVKey.RESIZE_STRETCH))
	        {
	            return (this.toViewportScale) * viewport.width / this.size.width;
	        }
	        else if (this.resizeBehavior.equals(AVKey.RESIZE_KEEP_FIXED_SIZE))
	        {
	            return 1d;
	        }
	        else
	        {
	            return 1d;
	        }
	    }

	    protected Vec4 computeLocation(java.awt.Rectangle viewport, double scale)
	    {
	        double scaledWidth = scale * (isMinimized ? MINIMIZED_SIZE :
	            isMaximized ? viewport.width - this.borderWidth * 2 : this.size.width);
	        double scaledHeight = scale * (isMinimized ? MINIMIZED_SIZE :
	            isMaximized ? viewport.height * 2 / 3 - this.borderWidth * 2 : this.size.height);

	        double x;
	        double y;

	        if (this.locationCenter != null)
	        {
	            x = this.locationCenter.x - scaledWidth / 2;
	            y = this.locationCenter.y - scaledHeight / 2;
	        }
	        else if (this.position.equals(AVKey.NORTHEAST))
	        {
	            x = viewport.getWidth() - scaledWidth - this.borderWidth;
	            y = viewport.getHeight() - scaledHeight - this.borderWidth;
	        }
	        else if (this.position.equals(AVKey.SOUTHEAST))
	        {
	            x = viewport.getWidth() - scaledWidth - ( 9 * this.borderWidth) ;
	            y = 0d + this.borderWidth;
	        }
	        else if (this.position.equals(AVKey.NORTHWEST))
	        {
	            x = 0d + this.borderWidth;
	            y = viewport.getHeight() - scaledHeight - this.borderWidth;
	        }
	        else if (this.position.equals(AVKey.SOUTHWEST))
	        {
	            x = 0d + this.borderWidth;
	            y = 0d + this.borderWidth;
	        }
	        else // use North East
	        {
	            x = viewport.getWidth() - scaledWidth / 2 - this.borderWidth;
	            y = viewport.getHeight() - scaledHeight / 2 - this.borderWidth;
	        }

	        if (this.locationOffset != null)
	        {
	            x += this.locationOffset.x;
	            y += this.locationOffset.y;
	        }

	        return new Vec4(x, y, 0);
	    }

	    /**
	     * Computes the Position of the pickPoint over the graph and updates pickedSample indice
	     *
	     * @param dc         the current DrawContext
	     * @param locationSW the screen location of the bottom left corner of the graph
	     * @param mapSize    the graph screen dimension in pixels
	     *
	     * @return the picked Position
	     */
	    protected Position computePickPosition(DrawContext dc, Vec4 locationSW, Dimension mapSize)
	    {
	        Position pickPosition = null;
	        //this.pickedSample = -1;
	        Point pickPoint = dc.getPickPoint();
	        if (pickPoint != null && !this.follow.equals(FOLLOW_CURSOR))
	        {
	            Rectangle viewport = dc.getView().getViewport();
	            // Check if pickpoint is inside the graph
	            if (pickPoint.getX() >= locationSW.getX()
	                && pickPoint.getX() < locationSW.getX() + mapSize.width
	                && viewport.height - pickPoint.getY() >= locationSW.getY()
	                && viewport.height - pickPoint.getY() < locationSW.getY() + mapSize.height)
	            {
	                // Find sample - Note: only works when graph expends over the full width
	                int sample = (int) (((double) (pickPoint.getX() - locationSW.getX()) / mapSize.width) * this.samples);
	                if (sample >= 0 && sample < this.samples)
	                {
	                    this.pickedSample = sample;
	                    long timeSelected = ((this.lastDate.getTime() - this.firstDate.getTime() ) / this.samples ) * pickedSample + this.firstDate.getTime();
	                    this.pickedTime = new Date( timeSelected );
	                }
	            }
	        }        

	        
	        return pickPosition;
	    }	   
	   

	   


	    protected Position computeViewCenterPosition(DrawContext dc)
	    {
	        View view = dc.getView();
	        Line ray = view.computeRayFromScreenPoint(view.getViewport().getWidth() / 2,
	            view.getViewport().getHeight() / 2);
	        Intersection[] inters = dc.getSurfaceGeometry().intersect(ray);
	        if (inters.length > 0)
	            return dc.getGlobe().computePositionFromPoint(inters[0].getIntersectionPoint());

	        return null;
	    }

	    protected void computeProfileLength()
	    {
	        if (this.wwd == null)
	            return;
	        View view = this.wwd.getView();
	        // Compute profile length
	        if (view instanceof OrbitView) 
	        {
	            this.length = Math.min(((OrbitView) view).getZoom() * .8 * this.profileLengthFactor,
	                this.wwd.getModel().getGlobe().getRadius() * Math.PI);
	        }
	        else
	        {
	            this.length = Math.min(Math.abs(view.getEyePosition().getElevation()) * .8 * this.profileLengthFactor,
	                this.wwd.getModel().getGlobe().getRadius() * Math.PI);
	        }
	      
	    }

	    protected Position computeCursorPosition(DrawContext dc)
	    {
	        Position pos = this.wwd.getCurrentPosition();
	        if (pos == null && dc.getPickPoint() != null)
	        {
	            // Current pos is null, try intersection with terrain geometry
	            Line ray = dc.getView().computeRayFromScreenPoint(dc.getPickPoint().x, dc.getPickPoint().y);
	            Intersection[] inter = dc.getSurfaceGeometry().intersect(ray);
	            if (inter != null && inter.length > 0)
	                pos = dc.getGlobe().computePositionFromPoint(inter[0].getIntersectionPoint());
	        }
	        if (pos == null && dc.getPickPoint() != null)
	        {
	            // Position is still null, fallback on intersection with ellipsoid.
	            pos = dc.getView().computePositionFromScreenPoint(dc.getPickPoint().x, dc.getPickPoint().y);
	        }
	        return pos;
	    }

	    @Override
	    public String toString()
	    {
	        return "TimeLineLayer";
	    }
	}
