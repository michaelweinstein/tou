package miweinst.tou;

import miweinst.engine.util.App;

public class TouMain {

	public static void main(String[] args) {
		App app = new App("tou", false);
		app.setScreen(new MenuScreen(app));
		app.startup();
	}
}
