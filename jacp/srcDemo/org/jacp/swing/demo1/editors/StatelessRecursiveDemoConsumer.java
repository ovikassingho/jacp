package org.jacp.swing.demo1.editors;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.swing.demo1.util.DropShadowPanel;
import org.jacp.swing.rcp.component.AStatelessComponent;
import org.jdesktop.swingx.graphics.GraphicsUtilities;

public class StatelessRecursiveDemoConsumer extends AStatelessComponent {

    private final static int THUMB_WIDTH = 88;
    private final static int THUMB_HEIGHT = 66;

    @Override
    public Object handleAction(IAction<ActionEvent, Object> action) {
	Object message = action.getLastMessage();
	if (message instanceof File) {
	    System.out.println("Recursive : " + this + " Path:"
		    + ((File) action.getLastMessage()).getPath());
	    File dir = (File) message;
	    for (final File file : dir.listFiles()) {
		if (file.isDirectory()) {
		    final IActionListener<ActionListener, ActionEvent, Object> listener = getActionListener();
		    listener.getAction().setMessage(file);
		    listener.getListener().actionPerformed(
			    listener.getAction().getActionEvent());
		} else {
		    if (file.getName().toLowerCase().contains(".jpg")) {
			    System.out.println("Recursive : " + this + " File:"
				    + file.getPath());
				final IActionListener<ActionListener, ActionEvent, Object> listener = getActionListener();
				listener.getAction().addMessage("id08",
						getPanel(file));
				listener.getListener().actionPerformed(
						listener.getAction().getActionEvent());
		    }
		}
	    }
	}

	return null;
    }

    /**
     * get image from file and create shadow Panel
     * 
     * @param file
     * @return
     */
    private org.jacp.swing.demo1.util.DropShadowPanel getPanel(File file) {
	BufferedImage myPicture = null;
	synchronized (file) {
        try {
                myPicture = ImageIO.read(file);
        } catch (final IOException e) {
                e.printStackTrace();
        }
        //System.out.println(myPicture.getSampleModel());
        // 88 vs 66 -> 4:3
        if (myPicture.getWidth() > THUMB_WIDTH
                        && myPicture.getHeight() > THUMB_HEIGHT) {
                // check landscape or portrait
                double direction = (double) myPicture.getWidth()
                                / (double) myPicture.getHeight();
                if (direction > 1)
                        myPicture = GraphicsUtilities.createThumbnail(
                                        myPicture,
                                        (int) (THUMB_HEIGHT * direction),
                                        THUMB_HEIGHT);
                else if (direction < 1)
                        myPicture = GraphicsUtilities.createThumbnail(
                                        myPicture,
                                        (int) (THUMB_WIDTH * direction),
                                        THUMB_WIDTH);
                else
                        myPicture = GraphicsUtilities.createThumbnail(
                                        myPicture, THUMB_WIDTH, THUMB_WIDTH);

        }
}
        return new DropShadowPanel(myPicture);
    }

}
