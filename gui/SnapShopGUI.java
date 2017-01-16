/*
* TCSS 305 - Autumn 2015
* Assignment 4 - Snap Shop
*/

package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import filters.EdgeDetectFilter;
import filters.EdgeHighlightFilter;
import filters.Filter;
import filters.FlipHorizontalFilter;
import filters.FlipVerticalFilter;
import filters.GrayscaleFilter;
import filters.SharpenFilter;
import filters.SoftenFilter;
import image.PixelImage;

/**
 * 
 * @author Jake Knowles
 * @version 2 November 2015
 */
public class SnapShopGUI extends JFrame {
    
    /**  A generated serial version UID for object Serialization. */
    private static final long serialVersionUID = 1L;
    
    /** My panel. */
    private static JPanel myPanel;
    
    /** The north panel. */
    private static JPanel myNorthPanel;
    
    /**  The south panel. */
    private static JPanel mySouthPanel;
    
    /**  The middle panel. */
    private static JPanel myCentralPanel;
    
    // create "image modification" buttons
    /** Edge Detect button. */
    private static JButton edgeDetect = new JButton("Edge Detect");
    
    /** Edge Highlight button. */
    private static JButton edgeHighlight = new JButton("Edge Highlight");
    
    /** Flip Horizontal button. */
    private static JButton flipHorizontal = new JButton("Flip Horizontal");  
    
    /** Flip Vertical button. */
    private static JButton flipVertical = new JButton("Flip Vertical");
    
    /** GrayScale button. */
    private static JButton grayscale = new JButton("Grayscale");
    
    /** Sharpen button. */
    private static JButton sharpen = new JButton("Sharpen");
    
    /** Soften button. */
    private static JButton soften = new JButton("Soften"); 
    
    // create "utility" buttons and put icons on 
    // my "utility" buttons (Open, Save, Exit, Undo)
    
    /** Open button. */
    private static JButton open = new JButton("Open");  

    
    /** Save As button. */
    private static JButton saveAs = new JButton("Save As..");
    
    /** Close button. */
    private static JButton closeImage = new JButton("Close");

    /** Undo button. */
    private static JButton undoImage = new JButton("Undo");        
    
    /** File chooser. */
    private static JFileChooser myFileChooser = new JFileChooser();
    
    /** Label. */
    private static JLabel label = new JLabel();
    
    /**  My Image. */
    private PixelImage myImage;
    
    /** Created for undoing picture changes. */
    private PixelImage myOriginalImage;
    
    /**  The result. */
    private int myResult;
    
    /** My constructor. */
    public SnapShopGUI() {
        super("TCSS 305 SnapShop");
    }
    
    
    /** SetUp Assignment. */
    private void beginSetup() {
        myPanel = new JPanel(new BorderLayout());
        myNorthPanel = new JPanel(new FlowLayout());
        mySouthPanel = new JPanel(new FlowLayout());
        myCentralPanel = new JPanel(new FlowLayout());

        // maintain button states
        enableButtons(false);
        undoImage.setEnabled(false);
        
        myNorthPanel.add(edgeDetect);
        myNorthPanel.add(edgeHighlight);
        myNorthPanel.add(flipHorizontal);
        myNorthPanel.add(flipVertical);
        myNorthPanel.add(grayscale);
        myNorthPanel.add(sharpen);
        myNorthPanel.add(soften);
        myCentralPanel.add(label);
        mySouthPanel.add(open);
        mySouthPanel.add(saveAs);
        mySouthPanel.add(closeImage);
        mySouthPanel.add(undoImage);
        
        myPanel.add(myNorthPanel, BorderLayout.NORTH);
        myPanel.add(myCentralPanel, BorderLayout.CENTER);
        myPanel.add(mySouthPanel, BorderLayout.SOUTH);
        
        add(myPanel);
        pack();
        
        // create button action listeners
        createImageModificationButtonListeners();
        createImageUtilityButtonListeners();
    }
        
    /** Method to create action listeners for the many filters. */
    private void createImageModificationButtonListeners() {
        edgeDetect.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent theAction) {
                    applyFilter(new EdgeDetectFilter());
                }     
            });      
        
        edgeHighlight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theAction) {
                applyFilter(new EdgeHighlightFilter());
            }     
        });       
        
        flipHorizontal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theAction) {
                applyFilter(new FlipHorizontalFilter());
            }     
        });       
        
        flipVertical.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theAction) {
                applyFilter(new FlipVerticalFilter());
            }     
        });

        grayscale.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theAction) {
                applyFilter(new GrayscaleFilter());
            }     
        });        
        
        sharpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theAction) {
                applyFilter(new SharpenFilter());
            }     
        });        
        
        soften.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theAction) {
                applyFilter(new SoftenFilter());
            }     
        }); 
    }
    
    /** Method to create action listeners for "utility" buttons
     * underneath selected picture. 
     */
    private void createImageUtilityButtonListeners() {
        open.addActionListener(new ActionListener() { 
            public void actionPerformed(final ActionEvent theEvent) {
                myResult = myFileChooser.showOpenDialog(myPanel);
                if (myResult == JFileChooser.APPROVE_OPTION) {
                    try {
                        myImage = PixelImage.load(myFileChooser.getSelectedFile());
                        setMyOriginalImage(PixelImage.load(myFileChooser.getSelectedFile()));
                    } catch (final IOException exception) {
                        JOptionPane.showMessageDialog(
                              myPanel, "The selected file did not contain an image!",
                              "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    if (null != myImage) {
                        label.setIcon(new ImageIcon(myImage));
                        enableButtons(true);
                        pack();
                    }
                }
            }
        });
        
        saveAs.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                if (myFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    final String exportPath = 
                                    myFileChooser.getSelectedFile().getAbsolutePath();
                    try {
                        final File outFile = new File(exportPath);
                        myImage.save(outFile);
                    } catch (final IOException ioe) {
                        JOptionPane.showMessageDialog(
                                                      myPanel, "Unable to save image!",
                                                      "Output Error", 
                                                      JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        closeImage.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                myImage = null;
                label.setIcon(null);
                enableButtons(false);
                undoImage.setEnabled(false);
                pack();
            }
        });
        
        undoImage.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                if (null != getMyOriginalImage()) {
                    label.setIcon(new ImageIcon(getMyOriginalImage()));
                }
                undoImage.setEnabled(false);
                pack();
            }
        });
    }
    
    /** 
     * Method to enable all buttons on JFrame, except Undo. 
     * 
     * @param theFlag theFlag returns true or false to let
     * the buttons know if they can be enabled. 
     */
    private void enableButtons(final boolean theFlag) {
        edgeDetect.setEnabled(theFlag);
        edgeHighlight.setEnabled(theFlag);
        flipHorizontal.setEnabled(theFlag);
        flipVertical.setEnabled(theFlag);
        grayscale.setEnabled(theFlag);
        sharpen.setEnabled(theFlag);
        soften.setEnabled(theFlag);
        saveAs.setEnabled(theFlag);
        closeImage.setEnabled(theFlag);
    }
    
    /**
     * Apply Filter is a method to help undo the image.
     * 
     * @param theFilter the Filter is the passed in filter.
     */
    private void applyFilter(final Filter theFilter) {
        theFilter.filter(myImage);
        label.setIcon(new ImageIcon(myImage));
        undoImage.setEnabled(true);
    }
    
    /** 
     * Returns the original image. 
     * 
     * @return myOriginalImage myOriginalImage is the O Image.
     */
    private PixelImage getMyOriginalImage() {
        return myOriginalImage;
    }

    /**
     *  Sets the original image. 
     *
     * @param theOriginalImage theOriginalImage is the O image passed in.
     */
    private void setMyOriginalImage(final PixelImage theOriginalImage) {
        this.myOriginalImage = theOriginalImage;
    }
    
    /** Starts the GUI. */
    public void start() {
        beginSetup(); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}