import java.io.*;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import javax.microedition.location.*;

public class NoWhereMIDlet
	extends MIDlet
	implements CommandListener {

	private Form mMainForm;
	private Display display;

	private StringItem here;
	private StringItem dest;

	public NoWhereMIDlet() {
		mMainForm = new Form("NoWhereMIDlet");

		here = new StringItem(null, "here\n");
		dest = new StringItem(null, "dest\n");

		mMainForm.append(here);
		mMainForm.append(dest);
		mMainForm.addCommand(new Command("Exit", Command.EXIT, 0));
		mMainForm.setCommandListener(this);
	}

	public void startApp() {
		display = Display.getDisplay(this);
		display.setCurrent(mMainForm);

		Thread runner = new Thread(new PosReader(this));
		runner.start();
	}

	public void pauseApp() {}

	public void destroyApp(boolean unconditional) {}

	public void commandAction(Command c, Displayable s) {
		notifyDestroyed();
	}
	public void updateHere(Coordinates c) {

		here.setText("on the move lat: " +
			c.getLatitude() + "lon: " +
			c.getLongitude() + "\n");
		display.repaint();
	}
}

class PosReader implements Runnable{
	private NoWhereMIDlet MIDlet;

	public PosReader(NoWhereMIDlet MIDlet) {
		this.MIDlet = MIDlet;
		//System.out.println("Thread PosReader...");
	}

	public void run(){
		/*
		   try{
		   transmit();
		   System.out.println("Thread Run...");
		   }catch(Exception error){ 
		   System.err.println(error.toString());
		   } 
		   */

		Criteria cr = new Criteria();
		//cr.setHorizontalAccuracy(5);
		LocationProvider lp = null;

		try {
			lp = LocationProvider.getInstance(cr);
		} catch (Throwable e) {
		}

		//lp.setLocationListener();

		// get the location, one minute timeout
		while (true) {
			Location l = null;
			try {
				l = lp.getLocation(60);
			} catch (Throwable e) {}

			if (l != null) {
				Coordinates c = l.getQualifiedCoordinates();

				if (c != null) {
					MIDlet.updateHere(c);
					//mMainForm.append(new StringItem(null, "got loc\n"));
				}
			}
		}
	}


}
