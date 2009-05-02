/*
 * NoWhere - a minimalistic GPS tool for geocaching
 * (c) 2009 Matthias Wenzel
 *          Simon Schoar
 *
 * licensed under GPL v2
 */
import java.io.*;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import javax.microedition.location.*;

public class NoWhereMIDlet
	extends MIDlet
	implements CommandListener {

	private Form mMainForm;

	private StringItem here;
	private TextField destLon;
	private TextField destLat;
	private StringItem dist;

	private Coordinates cHere;
	private Coordinates cDest;

	private Command mCommandExit = new Command("Exit", Command.EXIT, 0);
	private Command mCommandUpdate = new Command("Update", Command.ITEM, 0);

	public NoWhereMIDlet() {
		mMainForm = new Form("NoWhereMIDlet");

		here = new StringItem(null, "here:\n\n\n");
		destLon = new TextField("Dest Lon:", "", 150, TextField.ANY);
		destLat = new TextField("Dest Lat", "", 150, TextField.ANY);
		dist = new StringItem(null, "dist: ");

		destLon.setItemCommandListener(new ItemCommandListener() {
			public void commandAction(Command c, Item item) {
				System.err.println("event LON\n");
				cDest.setLongitude(Coordinates.convert(destLon.getString()));
				updateDist();
			}
		    }
		);
		destLat.setItemCommandListener(new ItemCommandListener() {
			public void commandAction(Command c, Item item) {
				System.err.println("event LAT\n");
				cDest.setLatitude(Coordinates.convert(destLat.getString()));
				updateDist();
			}
		    }
		);

		destLon.setDefaultCommand(mCommandUpdate);
		destLat.setDefaultCommand(mCommandUpdate);

		mMainForm.append(here);
		mMainForm.append(destLon);
		mMainForm.append(destLat);
		mMainForm.append(dist);

		cHere = new Coordinates( 0.0,  0.0, (float)  0.0);
		cDest = new Coordinates(48.0, 12.0, (float)400.0);
		updateDest();

		mMainForm.addCommand(mCommandExit);
		mMainForm.setCommandListener(this);
	}

	public void startApp() {
		Display.getDisplay(this).setCurrent(mMainForm);

		Criteria cr = new Criteria();
		//cr.setHorizontalAccuracy(5);
		cr.setCostAllowed(false);
		LocationProvider lp = null;

		try {
			lp = LocationProvider.getInstance(cr);
		} catch (Throwable e) {
		    new Alert("No LocationProvider instance available: " + e.getMessage());
		}

		lp.setLocationListener(new LocationListener(){
			public void locationUpdated(LocationProvider lp, Location l){
				if (l == null) {
				    return;
				}
				
				Coordinates c = l.getQualifiedCoordinates();

				if (c == null) {
				    return;
				}
				
				updateHere(c);

			}
			public void providerStateChanged(LocationProvider lp, int s){}
		}, 1, 1, 1);

		System.err.println("startup done\n");
	}

	public void pauseApp() {}

	public void destroyApp(boolean unconditional) {}

	public void commandAction(Command c, Displayable s) {
	    if (mCommandExit.equals(c)) {
		notifyDestroyed();
	    }
	}

	public void updateHere(Coordinates c) {
		here.setText(
			"here:\n" +
			"  lat:  " + c.convert(c.getLatitude() , Coordinates.DD_MM) + "\n" +
			"  lon:  " + c.convert(c.getLongitude(), Coordinates.DD_MM) + "\n" );
		cHere = c;
		updateDist();
	}
	
	public void updateDest() {
		destLat.setString(
			cDest.convert(cDest.getLatitude() , 2));
		destLon.setString(
			cDest.convert(cDest.getLongitude(), 2));
		//cDest = c:
		updateDist();
	}

	public void updateDist() {
		int angle = (int)cHere.azimuthTo(cDest);
		String dir = "???";

		if (angle >  23 && angle <=  67) {
			dir = "NE";
		}
		if (angle >  67 && angle <= 113) {
			dir = "E";
		}
		if (angle > 113 && angle <= 158) {
			dir = "SE";
		}
		if (angle > 158 && angle <= 203) {
			dir = "S";
		}
		if (angle > 203 && angle <= 248) {
			dir = "SW";
		}
		if (angle > 248 && angle <= 293) {
			dir = "W";
		}
		if (angle > 293 && angle <= 338) {
			dir = "NW";
		}
		if (angle > 338 || angle <=  23) {
			dir = "N";
		}
		
		dist.setText(
			"\ndist: " + cHere.distance(cDest) + "m  " +
			angle + "° " + dir
			);
	}
}

