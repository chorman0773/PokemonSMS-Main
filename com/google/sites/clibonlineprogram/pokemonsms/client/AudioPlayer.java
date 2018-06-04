package com.google.sites.clibonlineprogram.pokemonsms.client;

import javax.sound.sampled.*;

public final class AudioPlayer{
  public static AudioPlayer instance = new AudioPlayer();
  private AudioPlayer(){}
  
  public float getModifier(int ubyte){
    ubyte = ubyte&0xff;
    return ubyte/128.0f;
  }
  
  public Clip modifyVolume(Clip c,float modifier){
    float inputVolume = c.getLevel();
    float modifierVal = modifier*inputVolume;
    FloatControl gain = (FloatControl) c.getControl(FloatControl.Type.VOLUME);
    gain.setValue(modifierVal);
    return c;
  }
  public Clip modifyPitch(Clip c,float modifier){
    
  }
};
