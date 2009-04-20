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
	private StringItem dist;

	private Coordinates cHere;
	private Coordinates cDest;

	public NoWhereMIDlet() {
		mMainForm = new Form("NoWhereMIDlet");

		here = new StringItem(null, "here\n");
		dest = new StringItem(null, "dest\n");
		dist = new StringItem(null, "dist\n");

		mMainForm.append(here);
		mMainForm.append(dest);
		mMainForm.append(dist);

		cHere = new Coordinates( 0.0,  0.0, (float)  0.0);
		cDest = new Coordinates(12.0, 48.0, (float)400.0);
		updateDest();

		mMainForm.addCommand(new Command("Exit", Command.EXIT, 0));
		mMainForm.setCommandListener(this);
	}

	public void startApp() {
		display = Display.getDisplay(this);
		display.setCurrent(mMainForm);

		Criteria cr = new Criteria();
		//cr.setHorizontalAccuracy(5);
		cr.setCostAllowed(false);
		LocationProvider lp = null;

		try {
			lp = LocationProvider.getInstance(cr);
		} catch (Throwable e) {}

		lp.setLocationListener(new LocationListener(){
			public void locationUpdated(LocationProvider lp, Location l){

				if (l != null) {
					Coordinates c = l.getQualifiedCoordinates();

					if (c != null) {
						updateHere(c);
					}
				}

			}
			public void providerStateChanged(LocationProvider lp, int s){}
		}, 1, 1, 1);
	}

	public void pauseApp() {}

	public void destroyApp(boolean unconditional) {}

	public void commandAction(Command c, Displayable s) {
		notifyDestroyed();
	}
	public void updateHere(Coordinates c) {

		here.setText(
			"here:\n" +
			"lat: " + c.convert(c.getLatitude() , 2) + "\n" +
			"lon: " + c.convert(c.getLongitude(), 2) + "\n" );
		cHere = c;
		updateDist();
	}
	public void updateDest() {

		dest.setText(
			"dest:\n" +
			"lat: " + cDest.convert(cDest.getLatitude() , 2) + "\n" +
			"lon: " + cDest.convert(cDest.getLongitude(), 2) + "\n" );
		//cDest = c:
		updateDist();
	}

	public void updateDist() {
		dist.setText(
			"\ndist: " + cHere.distance(cDest) + "m\n" +
			"      " + cHere.azimuthTo(cDest) + "°"
			);
	}
}
