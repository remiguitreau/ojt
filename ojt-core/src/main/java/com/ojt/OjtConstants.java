package com.ojt;

import java.io.File;

import javax.swing.ImageIcon;

public interface OjtConstants {
	boolean DEBUG = true;

	String VERSION = "1.1.3";

	final int MAX_COMPETITOR_PER_GROUP = 16;

	final float WEIGHT_DELTA_IN_PURCENT = 10;

	final File PERSISTANCY_DIRECTORY = new File(".." + File.separator + "datas" + File.separator
			+ "persistancy");

	final File WEIGHING_DIRECTORY = new File(".." + File.separator + "datas" + File.separator + "pesees");

	final File MODELS_DIRECTORY = new File(".." + File.separator + "datas" + File.separator + "models");

	final File EXPORT_DIRECTORY = new File(".." + File.separator + "datas" + File.separator + "export");

	final File SOURCE_DIRECTORY = new File(".." + File.separator + "datas" + File.separator + "source");
	
	final ImageIcon OJT_ICON = new ImageIcon(OjtConstants.class.getResource("ojt-icon.png"));
	final ImageIcon OJT_LOGO_67x52 = new ImageIcon(OjtConstants.class.getResource("ojt-67x52.png"));
	final ImageIcon SOURCEFORGE_150x17 = new ImageIcon(OjtConstants.class.getResource("sourceforge.net_150x17.png"));
}
