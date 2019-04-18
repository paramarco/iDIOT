package iDIOT;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.event.RenderingExceptionListener;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.exception.WWAbsentRequirementException;
import gov.nasa.worldwind.layers.CompassLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.layers.ViewControlsLayer;
import gov.nasa.worldwind.layers.ViewControlsSelectListener;
import gov.nasa.worldwind.render.Offset;
import gov.nasa.worldwind.util.BasicDragger;
import gov.nasa.worldwind.util.StatisticsPanel;
import gov.nasa.worldwind.util.StatusBar;
import gov.nasa.worldwind.util.WWUtil;
import gov.nasa.worldwind.util.layertree.LayerTree;
import gov.nasa.worldwind.util.tree.BasicFrameAttributes;
import gov.nasa.worldwind.util.tree.BasicTreeAttributes;
import gov.nasa.worldwind.util.tree.BasicTreeLayout;
import gov.nasa.worldwindx.examples.LayerPanel;
import gov.nasa.worldwindx.examples.util.HighlightController;
import gov.nasa.worldwindx.examples.util.HotSpotController;
import gov.nasa.worldwindx.examples.util.ToolTipController;

public class AppFrame extends JFrame
{
	private static final long serialVersionUID = 1L;

	private Dimension canvasSize = new Dimension(800, 700);

    protected AppPanel wwjPanel;
    protected JPanel controlPanel;
    protected LayerPanel layerPanel;
    protected StatisticsPanel statsPanel;  
               
    
    public LayerTree layerTree;
    protected RenderableLayer hiddenLayer;
    protected HotSpotController controller;       

    public AppFrame()
    {
        this.initialize(true, false, false);
    }

    public AppFrame(Dimension size)
    {
        this.canvasSize = size;
        this.initialize(true, false, false);
    }

    public AppFrame(boolean includeStatusBar, boolean includeLayerPanel, boolean includeStatsPanel)
    {
        this.initialize(includeStatusBar, includeLayerPanel, includeStatsPanel);
    }

    protected void initialize(boolean includeStatusBar, boolean includeLayerPanel, boolean includeStatsPanel)
    {
        // Create the WorldWindow.
        this.wwjPanel = this.createAppPanel(this.canvasSize, includeStatusBar);
        this.wwjPanel.setPreferredSize(canvasSize);

        // Put the pieces together.
        this.getContentPane().add(wwjPanel, BorderLayout.CENTER);

        /*if (includeLayerPanel)
        {
            this.controlPanel = new JPanel(new BorderLayout(10, 10));
            this.layerPanel = new LayerPanel(this.getWwd());
            //TODO
            //this.controlPanel.add(this.layerPanel, BorderLayout.CENTER);
            //this.controlPanel.add(new FlatWorldPanel(this.getWwd()), BorderLayout.NORTH);
            //this.getContentPane().add(this.controlPanel, BorderLayout.WEST);
        }

        if (includeStatsPanel || System.getProperty("gov.nasa.worldwind.showStatistics") != null)
        {
            this.statsPanel = new StatisticsPanel(this.wwjPanel.getWwd(), new Dimension(250, canvasSize.height));
            this.getContentPane().add(this.statsPanel, BorderLayout.EAST);
        }*/

        // Create and install the view controls layer and register a controller for it with the World Window.
        ViewControlsLayer viewControlsLayer = new ViewControlsLayer();
        insertBeforeCompass(getWwd(), viewControlsLayer);
        this.getWwd().addSelectListener(new ViewControlsSelectListener(this.getWwd(), viewControlsLayer));

        // Register a rendering exception listener that's notified when exceptions occur during rendering.
        this.wwjPanel.getWwd().addRenderingExceptionListener(new RenderingExceptionListener()
        {
            public void exceptionThrown(Throwable t)
            {
                if (t instanceof WWAbsentRequirementException)
                {
                    String message = "Computer does not meet minimum graphics requirements.\n";
                    message += "Please install up-to-date graphics driver and try again.\n";
                    message += "Reason: " + t.getMessage() + "\n";
                    message += "This program will end when you press OK.";

                    JOptionPane.showMessageDialog(AppFrame.this, message, "Unable to Start Program",
                        JOptionPane.ERROR_MESSAGE);
                    System.exit(-1);
                }
            }
        });

        // Search the layer list for layers that are also select listeners and register them with the World
        // Window. This enables interactive layers to be included without specific knowledge of them here.
        for (Layer layer : this.wwjPanel.getWwd().getModel().getLayers())
        {
            if (layer instanceof SelectListener)
            {
                this.getWwd().addSelectListener((SelectListener) layer);
            }
        }            
              
        this.layerTree = new LayerTree();
      
         Offset DEFAULT_OFFSET = new Offset(20d, 140d, AVKey.PIXELS, AVKey.INSET_PIXELS);
        // String DEFAULT_FRAME_IMAGE = "images/layer-manager-64x64.png";
         String DEFAULT_FRAME_TITLE = "Air Traffic Control";

        BasicTreeLayout layout = new BasicTreeLayout(this.layerTree, DEFAULT_OFFSET);
        layout.getFrame().setFrameTitle(DEFAULT_FRAME_TITLE);
       // layout.getFrame().setIconImageSource(DEFAULT_FRAME_IMAGE);

        BasicTreeAttributes attributes = new BasicTreeAttributes();
        attributes.setRootVisible(false);
        layout.setAttributes(attributes);

        BasicFrameAttributes frameAttributes = new BasicFrameAttributes();
        frameAttributes.setBackgroundOpacity(0.7);
        layout.getFrame().setAttributes(frameAttributes);

        BasicTreeAttributes highlightAttributes = new BasicTreeAttributes(attributes);
        layout.setHighlightAttributes(highlightAttributes);

        BasicFrameAttributes highlightFrameAttributes = new BasicFrameAttributes(frameAttributes);
        highlightFrameAttributes.setForegroundOpacity(1.0);
        layout.getFrame().setHighlightAttributes(highlightFrameAttributes);           		
        		
        		
        this.layerTree.setLayout(layout);
        // Set up a layer to display the on-screen layer tree in the WorldWindow.
        this.hiddenLayer = new RenderableLayer();
        this.hiddenLayer.addRenderable(this.layerTree);
        this.getWwd().getModel().getLayers().add(this.hiddenLayer);

        // Mark the layer as hidden to prevent it being included in the layer tree's model. Including the layer in
        // the tree would enable the user to hide the layer tree display with no way of bringing it back.
        this.hiddenLayer.setValue(AVKey.HIDDEN, true);
        // Add a controller to handle input events on the layer tree.
        this.controller = new HotSpotController(this.getWwd());
       

        this.pack();

        // Center the application on the screen.
        WWUtil.alignComponent(null, this, AVKey.CENTER);
        this.setResizable(true);
        
       /* 
        // Establish a select listener that causes the tooltip controller to show the picked path position's
        // ordinal.
        this.setToolTipController(new ToolTipController(this.getWwd())
        {
            @Override
            public void selected(SelectEvent event)
            {
                // Intercept the selected position and assign the selected object's display name to the picked
                // ordinal.
                PickedObject po = event.getTopPickedObject();
                if (po != null && po.getObject() instanceof Path)
                {
                    String name = (po.getValue(AVKey.ORDINAL) != null) ? "Position " + (1 + (int)po.getValue(AVKey.ORDINAL) ) : null;
                    ((Path) po.getObject()).setValue(AVKey.DISPLAY_NAME, name);
                }

                super.selected(event);
            }
        });
        
        */

    }
    
	public static void insertBeforeCompass(WorldWindow wwd, Layer layer)
    {
        // Insert the layer into the layer list just before the compass.
        int compassPosition = 0;
        LayerList layers = wwd.getModel().getLayers();
        for (Layer l : layers)
        {
            if (l instanceof CompassLayer)
                compassPosition = layers.indexOf(l);
        }
        layers.add(compassPosition, layer);
    }
    
    public void initializeSelectionMonitoring()
    {
        this.getWwd().addSelectListener(new BasicDragger(this.getWwd()));
    }       
    
    public WorldWindow getWwd()
    {
        return this.wwjPanel.getWwd();
    }
    
    protected AppPanel createAppPanel(Dimension canvasSize, boolean includeStatusBar)
    {
        return new AppPanel(canvasSize, includeStatusBar);
    }

    public Dimension getCanvasSize()
    {
        return canvasSize;
    }

    public AppPanel getWwjPanel()
    {
        return wwjPanel;
    }

    public StatusBar getStatusBar()
    {
        return this.wwjPanel.getStatusBar();
    }

    /**
     * @deprecated Use getControlPanel instead.
     * @return This application's layer panel.
     */
    public LayerPanel getLayerPanel()
    {
        return this.layerPanel;
    }

    public JPanel getControlPanel()
    {
        return this.controlPanel;
    }

    public StatisticsPanel getStatsPanel()
    {
        return statsPanel;
    }

    public void setToolTipController(ToolTipController controller)
    {
        if (this.wwjPanel.toolTipController != null)
            this.wwjPanel.toolTipController.dispose();

        this.wwjPanel.toolTipController = controller;
    }

    public void setHighlightController(HighlightController controller)
    {
        if (this.wwjPanel.highlightController != null)
            this.wwjPanel.highlightController.dispose();

        this.wwjPanel.highlightController = controller;
    }
    
    public void addLayer2TreeLayer () {
    	
    }
}