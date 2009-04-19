import java.io.*;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import javax.microedition.location.*;

public class NoWhereMIDlet
	extends MIDlet
	implements CommandListener {

	private Form mMainForm;

	public StringItem here;
	public StringItem dest;
	public ItemCommandListener lDest;

	public NoWhereMIDlet() {
		mMainForm = new Form("NoWhereMIDlet");


		try {
			Criteria cr = new Criteria();
			cr.setHorizontalAccuracy(500);
			LocationProvider lp = LocationProvider.getInstance(cr);

			// get the location, one minute timeout
			Location l;
			try {
				l = lp.getLocation(60);
			} catch (Exception e) {
				// FIXME
				l = null;
			}

			Coordinates c = l.getQualifiedCoordinates();

			if (c != null) {
				// use coordinate information
				mMainForm.append(new StringItem(null, "got loc\n"));
			}

		} catch (LocationException e) {
			mMainForm.append(new StringItem(null, "no loc\n"));
		}

		here = new StringItem(null, "here\n");
		dest = new StringItem(null, "dest\n");

		//lDest.commandAction(updateDest, dest);

		dest.setItemCommandListener(lDest);

		mMainForm.append(here);
		mMainForm.append(dest);
		mMainForm.addCommand(new Command("Exit", Command.EXIT, 0));
		mMainForm.setCommandListener(this);
	}

	private void updateDest() {
		here = new StringItem(null, "there\n");
	}

	public void startApp() {
		Display.getDisplay(this).setCurrent(mMainForm);
	}

	public void pauseApp() {}

	public void destroyApp(boolean unconditional) {}

	public void commandAction(Command c, Displayable s) {
		notifyDestroyed();
	}
}

class PosReader implements Runnable{
	private NoWhereMIDlet MIDlet;

	public PosReader(NoWhereMIDlet MIDlet){
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
		cr.setHorizontalAccuracy(5);
		LocationProvider lp = null;

		try {
			lp = LocationProvider.getInstance(cr);
		} catch (Exception e) {
		}

		// get the location, one minute timeout
		Location l;
		try {
			l = lp.getLocation(60);
		} catch (Exception e) {
		}
	}

	public void start(){
		Thread thread = new Thread(this);
		try{
			thread.start();
			System.out.println("Thread Start...");
		}catch(Exception error){}
	}

	private void transmit() throws IOException{} 

}
