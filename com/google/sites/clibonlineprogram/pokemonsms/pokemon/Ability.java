package com.google.sites.clibonlineprogram.pokemonsms.pokemon;

import java.util.TreeSet;
import java.util.Collections;
import java.util.Set;

import org.luaj.vm2.LuaTable;

import com.google.sites.clibonlineprogram.pokemonsms.text.TextComponent;
import com.google.sites.clibonlineprogram.pokemonsms.util.registry.ResourceLocation;

public class Ability {


    private LuaTable eventHandler;
    public final ResourceLocation loc;
    private TextComponent name;
    private TextComponent description;
    private Set<ResourceLocation> lifetimeShared;

    public Ability(LuaTable t) {
        String loc = t.get("loc").checkjstring();
        this.loc = new ResourceLocation(loc);
        eventHandler = t.get("eventBus").checktable();
        name = TextComponent.fromLua(t.get("name"));
        description = TextComponent.fromLua(t.get("description"));
        if(t.get("lifetimeSharedAbilities").isnil())
            lifetimeShared = Collections.emptyList();
        else
        {
            Set<ResourceLocation> list = new TreeSet<>();
            LuaTable val = t.get("lifetimeSharedAbilities").checktable();
            for(int i = 0;i<val.length();i++)
                list.add(new ResourceLocation(val.get(i+1).checkjstring()));
            lifetimeShared = Collections.unmodifiableSet(list);
        }
    }

    public EventBus getEventBus(){
        return new EventBus(eventHandler);
    }
    
    public boolean isLifetimeSharedWith(Ability a){
        if(a==this)
           return true;
        else
            return lifetimeShared.contains(a.loc)&&a.lifetimeShared.contains(loc);
    }
    
    public ResourceLocation getLocation(){
        return loc;   
    }
    
    public TextComponent getTranslatedName(){
        return name.getAsFormatted();
    }
    
    public TextComponent getTranslatedDescription(){
        return description.getAsFormatted();   
    }

}
