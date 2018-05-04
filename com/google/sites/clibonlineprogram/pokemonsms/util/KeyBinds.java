package com.google.sites.clibonlineprogram.pokemonsms.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;
import java.util.TreeMap;

import com.google.sites.clibonlineprogram.pokemonsms.options.Options.Controls;

import net.java.games.input.ControllerEnvironment;

import com.google.sites.clibonlineprogram.pokemonsms.options.Options;

public class KeyBinds {





	private static final Controls[] controls = Controls.values();
	private static final EnumSet<Controls> activeControls = EnumSet.noneOf(Controls.class);
	private static final EnumSet<Controls> controlsToClear = EnumSet.noneOf(Controls.class);
	private static final int[] targetControls;
	static {
		try {
		targetControls = new int[controls.length];
		for(int i =0;i<controls.length;i++)
			targetControls[i] = Options.INSTANCE.getIntOption(controls[i]);
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	public static void updateKeyBind(Controls bind) {
		int i = Arrays.binarySearch(controls, bind);
		try {
			targetControls[i] = Options.INSTANCE.getIntOption(bind);
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void update() {
		activeControls.removeAll(controlsToClear);
		controlsToClear.clear();
	}

	private static class KeyEventHandler implements KeyListener{

		@Override
		public void keyPressed(KeyEvent arg0) {
			int code = arg0.getKeyCode();
			if(Character.isAlphabetic(code))
				code = Character.toLowerCase(code);//Remove Uppercase Letters.
			for(int i = 0;i<controls.length;i++)
				if(targetControls[i]==code){
					activeControls.add(controls[i]);
					return;
					}


		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			int code = arg0.getKeyCode();
			if(Character.isAlphabetic(code))
				code = Character.toLowerCase(code);//Remove Uppercase Letters.
			for(int i = 0;i<controls.length;i++)
				if(targetControls[i]==code){
					controlsToClear.add(controls[i]);
					return;
					}

		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}

	}




}
