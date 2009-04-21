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

	private ItemCommandListener destCl;

	public NoWhereMIDlet() {
		mMainForm = new Form("NoWhereMIDlet");

		here = new StringItem(null, "here:\n\n\n");
		dest = new StringItem(null, "dest:\n\n\n");
		dist = new StringItem(null, "dist: ");

		destCl = new ItemCommandListener(){
				public void commandAction(Command c, Item item){
					mMainForm.append(new StringItem(null, "action"));
					new Alert("hit me harder");
				}
			};
		dest.setItemCommandListener(destCl);

		mMainForm.append(here);
		mMainForm.append(dest);
		mMainForm.append(dist);

		cHere = new Coordinates( 0.0,  0.0, (float)  0.0);
		cDest = new Coordinates(48.0, 12.0, (float)400.0);
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
			"  lat:  " + c.convert(c.getLatitude() , 2) + "\n" +
			"  lon:  " + c.convert(c.getLongitude(), 2) + "\n" );
		cHere = c;
		updateDist();
	}
	public void updateDest() {

		dest.setText(
			"dest:\n" +
			"  lat:  " + cDest.convert(cDest.getLatitude() , 2) + "\n" +
			"  lon:  " + cDest.convert(cDest.getLongitude(), 2) + "\n" );
		//cDest = c:
		updateDist();
		dest.setItemCommandListener(destCl);
	}

	public void updateDist() {
		int angle = (int)cHere.azimuthTo(cDest);
		String dir = "???";

		if (angle >  23 && angle <=  67)
			dir = "NE";
		if (angle >  67 && angle <= 113)
			dir = "E";
		if (angle > 113 && angle <= 158)
			dir = "SE";
		if (angle > 158 && angle <= 203)
			dir = "S";
		if (angle > 203 && angle <= 248)
			dir = "SW";
		if (angle > 248 && angle <= 293)
			dir = "W";
		if (angle > 293 && angle <= 338)
			dir = "NW";
		if (angle > 338 || angle <=  23)
			dir = "N";

		dist.setText(
			"\ndist: " + cHere.distance(cDest) + "m  " +
			angle + "° " + dir
			);
	}
}
