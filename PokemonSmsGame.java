package com.google.sites.clibonlineprogram.pokemonsms;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;

import javax.crypto.Cipher;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.sqlite.JDBC;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.sites.clibonlineprogram.pokemonsms.options.Options;
import com.google.sites.clibonlineprogram.pokemonsms.options.Options.OptionStandard;

import com.google.sites.clibonlineprogram.pokemonsms.resources.ResourcePack;
import com.google.sites.clibonlineprogram.pokemonsms.resources.inline.InlineResourcePack;
import com.google.sites.clibonlineprogram.pokemonsms.save.SaveTable;
import com.google.sites.clibonlineprogram.pokemonsms.text.I18N;
import com.google.sites.clibonlineprogram.pokemonsms.text.TextComponent;
import com.google.sites.clibonlineprogram.pokemonsms.util.KeyBinds;
import com.google.sites.clibonlineprogram.sentry.GameBasic;
import com.google.sites.clibonlineprogram.sentry.annotation.Game;

@Game(gameId = "pokemonsms", gameName = "Pokemon SMS", gameVersion = "0.0.0", serialId = PokemonSmsGame.serialVersionUID, uuid = "6a48a936-e0d5-11e7-80c1-9a214cf093ae", displayVersion=true)
public class PokemonSmsGame extends GameBasic {

	/**
	 *
	 */
	static final long serialVersionUID = -2486132005819407344L;

	private boolean paused;

	private Object callLock = new Object();

	public static final long tickTime = 25;
	private long totalTickTime;
	private long lastTickTime;
	private int tickCount = 0;
	private int currTps = 40;
	private int currFps = 60;
	private Random gameRand = new Random();
	private Thread tickThread;

	private static boolean isCrypto256Available = true;

	private Object graphicsLock = new Object();
	private boolean stopGraphicsThread;
	private Thread graphicsLoop;
	private boolean graphicsPaused;
	private Thread graphicsThread;

	private Color background;
	private BufferedImage backgroundImg;
	private boolean run = true;
	private Graphics drawOn;
	private Point cursor;
	private SaveTable saves;


	public static final Gson gson = new GsonBuilder()
			.registerTypeAdapter(TextComponent.class, new TextComponent.TextComponentDeserializer())
			.create();
	public static final JsonParser json = new JsonParser();

	public static I18N lang;
	public static ResourcePack currResourcePack = new InlineResourcePack();


	public PokemonSmsGame() {
		// TODO Auto-generated constructor stub
	}


	public void setLanguage(I18N lang) {
		PokemonSmsGame.lang = lang;
		Locale.setDefault(lang.getLocale());
	}



	/* (non-Javadoc)
	 * @see com.google.sites.clibonlineprogram.sentry.GameBasic#initGame()
	 */
	@Override
	protected void initGame() throws Exception {
		if(Cipher.getMaxAllowedKeyLength("AES")<256)
		{
			JPanel display = new JPanel();
			display.setLayout(new FlowLayout());
			display.add(new JLabel("256-bit AES is not supported by your JVM."));
			display.add(new JLabel("The game is playable but Save file locking and all online options are unusable."));
			display.add(new JLabel("To enable these options please update your crypto.policy property to use the Default Unlimited Strength Encryption Policies"));
			JOptionPane.showMessageDialog(null, display, "Cryptographic Options Limited", JOptionPane.ERROR_MESSAGE);
			isCrypto256Available = false;
		}
		File localSavePath = new File(getDirectory(),"saves/table.pkmdb");
		saves = new SaveTable(JDBC.createConnection("jdbc:sqlite:"+localSavePath.getAbsolutePath(), new Properties()));


	}

	public static boolean canUseCrypto256() {
		return isCrypto256Available;
	}



	/* (non-Javadoc)
	 * @see com.google.sites.clibonlineprogram.sentry.GameBasic#destroyOverride()
	 */
	@Override
	protected void destroyOverride() {
		try {
			Options.INSTANCE.save();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		synchronized(callLock) {
		try {
			if(Options.INSTANCE.getBooleanOption(OptionStandard.DEFOCUS_MODE))
				this.paused = true;
			graphicsPaused = true;
		} catch (Exception e) {
		}
		}
	}

	@Override
	public void start() {
		synchronized(callLock) {
		if(paused)
			this.paused = false;
		callLock.notify();
		graphicsPaused = false;
		this.repaint();
		}

	}

	@Override
	public void run() {
		graphicsLoop = new Thread(this::runGraphics);
		graphicsLoop.start();
		try {
			tickThread = new Thread(this::tick);
			tickThread.setDaemon(true);
			tickThread.start();
			while(!Thread.interrupted()) {
				if(!paused)
					tickThread.interrupt();
				Thread.sleep(1000/currTps);
			}
		}catch(InterruptedException e) {}
		graphicsLoop.interrupt();
	}

	private void runGraphics() {
		try {
			graphicsThread = new Thread(this::render);
			graphicsThread.setDaemon(true);
			graphicsThread.start();
			while(!Thread.interrupted()) {
				if(!graphicsPaused)
					graphicsThread.interrupt();
				Thread.sleep(1000/currFps);
			}
		}catch(InterruptedException e) {}
	}

	private void render() {
		while(run) {
			try {
				Thread.sleep(10000);
			}catch(InterruptedException e) {}
			this.repaint();
		}
	}

	private synchronized void tick() {
		while(run) {
			try {
				Thread.sleep(10000);
			}catch(InterruptedException e) {}
			KeyBinds.update();
		}
	}

	private Color c = Color.white;
	private Font f = new Font(Font.SANS_SERIF,Font.PLAIN,13);

	public void drawText(TextComponent comp) {
		comp.drawOn(this, cursor, f, c);
	}

	public static PokemonSmsGame getInstance() {
		// TODO Auto-generated method stub
		return (PokemonSmsGame)GameBasic.instance;
	}


}
