package com.google.sites.clibonlineprogram.pokemonsms.text;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.luaj.vm2.LuaValue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.sites.clibonlineprogram.pokemonsms.PokemonSmsGame;
import com.google.sites.clibonlineprogram.pokemonsms.resources.Resource;
import com.google.sites.clibonlineprogram.pokemonsms.resources.ResourceType;
import com.google.sites.clibonlineprogram.pokemonsms.util.JsonUtils;

/**
 * The class for TextComponents. This class describes something that can be stored as raw json text then drawn on screen.
 * TextComponents come in various "flavors" or subtypes. These subtypes describe there behavior when formatted. <br/>
 * For example the following text component:<br/>
 * <code>
 * {<br/>
 * 	"type":"formatted",<br/>
 * 	"text":"Hello World!",<br/>
 * 	"formatting":{<br/>
 * 		"color":"red",<br/>
 * 		"bold":true<br/>
 * 	}<br/>
 * }<br/>
 * </code>
 *  Displays as <font color="red"><b>Hello World!</b></font>
 *  <br/>
 *  Text components can also be chained. Chaining text components will preserve styling until the style is overidden.
 *  A New formatted component will override formatting like bold, italics, etc. And will also override color unless the color is unspecified.<br/>
 *  A new non-formatted text component will not override any formatting and the format will bleed through.
 *  For Example the following text component:<br/>
 *  <code>
 * {<br/>
 * 	"type":"formatted",<br/>
 * 	"text":"Hello World!",<br/>
 * 	"formatting":{<br/>
 * 		"color":"red",<br/>
 * 		"bold":true<br/>
 * 	},<br/>
 * 	"continue":{<br/>
 *   "type":"newline",<br/>
 *    "text":"Program By Connor Horman"<br/>
 *  }<br/>
 * }<br/>
 * </code>
 * Displays as <font color="red"><b>Hello World!<br/>Program By Connor Horman</b></font>
 * <br/><br/>
 * The Lua Side also supports text components, creating them as tables. The first text component can be rewritten as a lua table as so:
 * <br/>
 * <code>
 * {type="formatted",text="Hello World!",formatting={color="red",bold=true}}<br/>
 * </code>
 * <br/>
 * Text components support the I18N structure, which translates components based on current localization into the other types.<br/>
 * When they are translated they can Obtain Arguments from other components. The argument type describes these.<br/>
 * The follow text component:
 * <br/><code>
 * {<br/>
 *  "type":"translateble",<br/>
 *  "text":"misc.nonexistant.group.greating"
 * }<br/>
 * </code>
 * Could be expanded to<br/>
 * {<br/>
 *  "type":"formatted",<br/>
 *  "text":"Hello ",<br/>
 *  "formatting":{<br/>
 *   "color":"gold",<br/>
 *   "italics":true<br/>
 *   },<br/>
 *   "continue":{<br/>
 *   	"type":"argument",<br/>
 *      "continue":{<br/>
 *        "type":"raw",<br/>
 *        "text":"!"<br/>
 *      }<br/>
 *   }<br/>
 *  }<br/>
 *  Which when the first argument is <code>{"type":"raw","text":"World"}</code> produces the text:<br/>
 *  <font color="gold"><b>Hello World!</b></font><br/>
 *  It could also be given <code>{"type":"raw","text":"Java"}</code> which produces:<br/>
 *  <font color="gold"><b>Hello Java!</b></font><br/>
 *  All raw TextComponents can also be described as a simple string, but is stored in object form. (The component "World" and {"type":"raw","text":"Word"} produce the same component).
 *  Any formatting requirements are given by the particular TextComponent and possibly any alternative way they are drawn.
 *	<br/>When a simple string is used as a text component from Lua, it is considered a translateble component, rather than a raw text component.
 *  @see {@link com.google.sites.clibonlineprogram.pokemonsms.text.I18N I18N} which provides the translation system to the game.
 *  @author Connor Horman
 *	@since 0.0
 */
public abstract class TextComponent {
	/**
	 * Class that deserializes text components from Json.<br/>
	 * It uses a registry of types to functions that take the JsonObject and produces a TextComponent.
	 * It can also be passed a Json String to produce a raw text component. (no formatting)
	 * @author Connor Horman
	 *
	 */
	public static class TextComponentDeserializer implements JsonDeserializer<TextComponent>, JsonSerializer<TextComponent> {

		public TextComponentDeserializer() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public TextComponent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			if(json.isJsonPrimitive()) {
				JsonPrimitive primitive = json.getAsJsonPrimitive();
				if(primitive.isString())
					return new TextComponentString(primitive.getAsString());
				else
					throw new JsonParseException("Element must be an object or a string");
			}
			if(!json.isJsonObject())
				throw new JsonParseException("Element must be an object");
			JsonObject obj = json.getAsJsonObject();
			String type = obj.get("type").getAsString();
			if(!producers.containsKey(type))
				throw new JsonParseException("This does not represent a text component");
			Function<JsonObject,TextComponent> producer = producers.get(type);
			return producer.apply(obj);
		}

		@Override
		public JsonElement serialize(TextComponent src, Type typeOfSrc, JsonSerializationContext context) {
			// TODO Auto-generated method stub
			return src.toJson();
		}

	}
	public static final Gson gson;

	static {
		producers = new TreeMap<>();
		gson = new GsonBuilder()
				.registerTypeAdapter(TextComponent.class, new TextComponentDeserializer())
				.registerTypeAdapter(I18NNode.class, new I18NNode.I18NNodeAdapter())
				.create();
		registerTextComponentSubclass("raw",o->new TextComponentString(o.get("text").getAsString()));
		registerTextComponentSubclass("endl",o->new NewlineTextComponent(o.get("text").getAsString()));
		registerTextComponentSubclass("translateable",new Function<JsonObject,TextComponent>(){

			@Override
			public TextComponent apply(JsonObject t) {
				return new TranslatebleTextComponent(t.get("text").getAsString());
			}

		});
		registerTextComponentSubclass("formatted",new Function<JsonObject,TextComponent>(){

			@Override
			public TextComponent apply(JsonObject t) {

					Style s = new Style(t.getAsJsonObject("formatting"));
					return new TextComponentFormatted(t.get("text").getAsString(),s);
			}

		});
		registerTextComponentSubclass("argument",new Function<JsonObject,TextComponent>(){

			@Override
			public TextComponent apply(JsonObject arg0) {
				if(arg0.has("target"))
					return new ArgumentTextComponent(arg0.get("target").getAsInt());
				return new ArgumentTextComponent(-1);
			}

		});

	}
	public static final TextComponent fromJson(JsonObject o) {
		return gson.fromJson(o, TextComponent.class);
	}
	public static final TextComponent fromJson(String s) {
		return gson.fromJson(s, TextComponent.class);
	}
	public static final TextComponent fromLua(LuaValue v) {
		if(v.isstring())
			return new TranslatebleTextComponent(v.checkjstring());
		return gson.fromJson(JsonUtils.luaToJson(v), TextComponent.class);
	}

	private static final Map<String,Function<JsonObject,TextComponent>> producers;
	public static class NewlineTextComponent extends TextComponent{

		public NewlineTextComponent(String txt) {
			super("endl", txt);
			// TODO Auto-generated constructor stub
		}

		/* (non-Javadoc)
		 * @see com.google.sites.clibonlineprogram.pokemonsms.text.TextComponent#prependToken()
		 */
		@Override
		protected String prependToken() {
			// TODO Auto-generated method stub
			return System.lineSeparator();
		}

	}
	public static class TextComponentString extends TextComponent{
		public TextComponentString(String txt) {
			super("raw",txt);
		}

	}

	public static class TextComponentFormatted extends TextComponent{
		private Style s;
		public TextComponentFormatted(String txt,Style s) {
			super("formatted",txt);
			this.s = s;

		}
		public TextComponentFormatted clone() {
			TextComponentFormatted clone = (TextComponentFormatted)super.clone();
			clone.s = s.clone();
			return clone;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((s == null) ? 0 : s.hashCode());
			return result;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			TextComponentFormatted other = (TextComponentFormatted) obj;
			if (s == null) {
				if (other.s != null)
					return false;
			} else if (!s.equals(other.s))
				return false;
			return true;
		}
		/* (non-Javadoc)
		 * @see com.google.sites.clibonlineprogram.pokemonsms.text.TextComponent#toJson()
		 */
		@Override
		public JsonObject toJson() {
			JsonObject ret = super.toJson();
			ret.add("formatting", s.asJson());
			return ret;
		}
		public Style getStyle() {
			// TODO Auto-generated method stub
			return s;
		}
		@Override
		public Point drawOn(Component comp, Point start, Font f, Color c) {
			f = s.getFont();
			if(s.getColor()!=null)
				c = s.getColor().getAwt();
			return super.drawOn(comp, start, f, c);
		}


	}
	/**
	 *  Registers a new text component type. <br/>
	 * The given function must be stateless and non-interfering. The resultant text component must be distinct from any other text component.
	 */
	public static final void registerTextComponentSubclass(String name,Function<JsonObject,TextComponent> producer) {
		producers.put(name, producer);
	}

	public static final Color DEFAULT_COLOR = Color.white;
	public static final String DEFAULT_FAMILY = "SansSerif";
	public static final int DEFAULT_SIZE = 12;

	/**
	 * The class which describes the color of formatting.<br/>
	 * This enumeration provides all the colors that the text system uses. Each Color also has a awt color in sRGB color space assigned to it.
	 * The assigned color provides a bridge from the TextComponent system to the AWT system.
	 * @author Connor Horman
	 *
	 */
	public static enum EnumTextColor {
		black(Color.black),dark_blue(new Color(00,00,128)),
		dark_green(new Color(0,100,0)),dark_aqua(new Color(0,139,139))
		,dark_red(new Color(139,0,0)),dark_purple(new Color(75,0,130)),
		gold(new Color(218,165,32)), gray(Color.gray), dark_gray(Color.darkGray), blue(Color.blue),
		green(new Color(0,255,0)), aqua(new Color(0,255,255)), red(Color.red),
		purple(new Color(128,0,128)), yellow(new Color	(255,255,0)), white(Color.white);
		private final Color awt;
		EnumTextColor(Color awt){
			this.awt = awt;
		}
		public Color getAwt() {
			return awt;
		}
		/**
		 * Obtains the static hashcode of the enum. This is guarenteed to be consistent between runs. (Unlike Enum.hashCode())
		 * @return a static hashcode, computed by the hashcode of the name, the color, and the class name
		 */
		public int staticHashcode() {
			final int prime = 31;
			int hashcode = name().hashCode();
			hashcode = prime*hashcode + awt.hashCode();
			hashcode = prime*hashcode + this.getDeclaringClass().getName().hashCode();
			return hashcode;
		}
	}
	/**
	 * The Style class describes the formatting of a Formatted text component.<br/>
	 * This contains a color, then 4 formatting options being <code>bold, italics, underscore, and strikethrough</code>.<br/>
	 * This class can be cloned to produce a distinct style from the original that is initially equal to the other.
	 * @author Connor Horman
	 *
	 */
	public static final class Style implements Cloneable{
		private EnumTextColor color;
		private boolean bold;
		private boolean italics;
		private boolean underscore;
		private boolean strikethrough;
		/**
		 * Constructs a new Style with a single color option.<br/> If null, then the style will not be associated with a color.
		 * @param color The initial color to use.
		 */
		public Style(EnumTextColor color) {
			this.color = color;
		}
		/**
		 * Constructs a new style based on a json object to use.<br/>
		 * @param formatting The JsonObject which contains the information about the style. May not be null.
		 */
		public Style(JsonObject formatting) {
			this.color = formatting.has("color")?EnumTextColor.valueOf(formatting.get("color").getAsString()):null;
			this.bold = formatting.has("bold")?formatting.get("bold").getAsBoolean():false;
			this.italics = formatting.has("italics")?formatting.get("italics").getAsBoolean():false;
			this.underscore = formatting.has("underscore")?formatting.get("underscore").getAsBoolean():false;
			this.strikethrough = formatting.has("strikethrough")?formatting.get("strikethrough").getAsBoolean():false;
		}
		/**
		 * Copy constructor. private to restrict use.<br/>
		 * Note: For any style s, s==new Style(s) must equal false, but s.equals(new Style(s)) must be true. The same applies to the clone() method
		 * @param base The initial style to copy.
		 */
		private Style(Style base) {
			this.color = base.color;
			this.bold = base.bold;
			this.italics = base.italics;
			this.underscore = base.underscore;
			this.strikethrough = base.strikethrough;
		}
		/**
		 * Sets the bold property.<br/>
		 * If the initial text looks like "Hello World!" the resultant text will look like "<b>Hello World!</b>"
		 * @return this, for chaining.
		 */
		public Style setBold(boolean bold) {
			this.bold = bold;
			return this;
		}
		/**
		 * Sets the italics property.<br/>
		 * If the initial text looks like "Hello World!" the resultant text will look like "<i>Hello World</i>"
		 * @return this, for chaining.
		 */
		public Style setItalics(boolean italics) {
			this.italics = italics;
			return this;
		}
		/**
		 * Sets the strikethrough property. <br/>
		 * If the initial text looks like "Hello World!", the resultant text will look like "<s>Hello World</s>"
		 * @return this, for chaining
		 */
		public Style setStrikethrough(boolean strikethrough) {
			this.strikethrough = strikethrough;
			return this;
		}
		/**
		 * Sets the underscore property. <br/>
		 * If the initial text looks like "Hello World!", the resultant text will look like "<u>Hello World</u>"
		 * @return this, for chaining
		 */
		public Style setUnderscore(boolean underscore) {
			this.underscore = underscore;
			return this;
		}
		/**
		 * Sets the color property. <br/>
		 * The resultant text will change its color based on the given color. If null, then the text will take the color of its immediate parent.<br/>
		 * Otherwise the text's color will be updated and as will all its children until the next Formatted Component with a non-null color property.<br/>
		 * If the given color is {@link EnumTextColor#gold} and the initial text is "Hello World!" the the resultant text will be "<font color="gold">Hello World!</font>"<br/>
		 * @return this, for chaining
		 */
		public Style setColor(EnumTextColor color) {
			this.color = color;
			return this;
		}
		/**
		 * Converts this Style into a JsonObject.<br/>
		 * It is guarenteed that for any non-null style s, new Style(s.asJson()).equals(s) will be true.<br/>
		 * The color property is only saved if it is not null. Formatting properties can be saved reguardless of there value, but are only saved if set.<br/>
		 * @return A distinct JsonObject which represents this style in json form.
		 */
		public JsonObject asJson() {
			JsonObject obj = new JsonObject();
			if(color!=null)
				obj.addProperty("color", color.name());
			if(bold)
				obj.addProperty("bold", bold);
			if(italics)
				obj.addProperty("italics", italics);
			if(underscore)
				obj.addProperty("underscore", underscore);
			if(strikethrough)
				obj.addProperty("strikethrough", strikethrough);
			return obj;
		}
		/**
		 * Copies this style.
		 */
		public Style clone() {
			return new Style(this);
		}
		/**
		 * Computes the Hashcode of this color. <br/>
		 * This hashcode is guarenteed to be equivalent between equal Styles between instances of the game.
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (bold ? 1231 : 1237);
			result = prime * result + ((color == null) ? 0 : color.staticHashcode());
			result = prime * result + (italics ? 1231 : 1237);
			result = prime * result + (strikethrough ? 1231 : 1237);
			result = prime * result + (underscore ? 1231 : 1237);
			return result;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof Style)) {
				return false;
			}
			Style other = (Style) obj;
			if (bold != other.bold) {
				return false;
			}
			if (color != other.color) {
				return false;
			}
			if (italics != other.italics) {
				return false;
			}
			if (strikethrough != other.strikethrough) {
				return false;
			}
			if (underscore != other.underscore) {
				return false;
			}
			return true;
		}
		/**
		 * Obtains the active text color.
		 * @return The color property
		 */
		public EnumTextColor getColor() {
			// TODO Auto-generated method stub
			return color;
		}

		/**
		 * Checks if the Style represents bold text.
		 * @return the bold property
		 */
		public boolean isBold() {
			return bold;
		}

		/**
		 * Checks if the style represents italic text.
		 * @return the italics property
		 */
		public boolean isItalics() {
			return italics;
		}

		/**
		 * Checks if the style represents strikethrough text.
		 * @return the strikethrough property
		 */
		public boolean isStrikethrough() {
			return strikethrough;
		}

		/**
		 * Checks if the style represents underscored text.
		 * @return the underscore property
		 */
		public boolean isUnderscore() {
			return underscore;
		}

		/**
		 * Merges this style into the next.
		 * This is given by cloning the next style, then setting the resultants' color to the current if the next style does not have an associated style.
		 * @param next The next style to be used
		 * @return a copy of the next style, with this color merged in if the next style has not associated color.
		 */
		public Style merge(Style next) {
			Style ret = next.clone();
			ret.color = next.color!=null?next.color:color;
			return ret;
		}

		public Font getFont() {
			Font base = new Font(DEFAULT_FAMILY,(bold?Font.BOLD:0)|(italics?Font.ITALIC:0),DEFAULT_SIZE);
			Map<TextAttribute,Object> attributes = (Map<TextAttribute,Object>) base.getAttributes();
			attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
			attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
			return Font.getFont(attributes);
		}

	}

	/**
	 * Represents a text component that can be translated. The "text" is a key for the i18n tree.<br/>
	 * This text component cannot be directly printed and must be translated before it can be displayed.
	 * @author Connor Horman
	 *
	 */
	public static class TranslatebleTextComponent extends TextComponent{

		public TranslatebleTextComponent(String l10nkey) {
			super("translateable",l10nkey);
		}
		public TextComponent getAsFormatted() {
			return PokemonSmsGame.lang.translateAndFormat(this, new TextComponent[0]);
		}
		public TextComponent format(TextComponent[] args,int depth) {
			TextComponent[] nargs = new TextComponent[args.length-depth];
			System.arraycopy(args, depth, nargs, 0, nargs.length);
			return PokemonSmsGame.lang.translateAndFormat(this, nargs);
		}
		@Override
		protected Point drawOnComponent(Component comp, Point start, Font f, Color c) {
			// TODO Auto-generated method stub
			return getAsFormatted().drawOn(comp, start, f, c);
		}


	}

	/**
	 * A placeholder for an argument in a text component tree. This is primarily used within translations.<br/>
	 * This can never be used for printing or basic formatting and requires a string of text components to substitute in.<br/>
	 * Rather then the standard "text" property, arguments have an optional "target" property. If it exists, it represents the index in the formatting arguments (which themselves are TextComponents).<br/>
	 * @author Connor Horman
	 *
	 */
	public static class ArgumentTextComponent extends TextComponent{
		private int target;
		public ArgumentTextComponent(int target) {
			super("arg", null);
			this.target = target;
		}
		/* (non-Javadoc)
		 * @see com.google.sites.clibonlineprogram.pokemonsms.text.TextComponent#toJson()
		 */
		@Override
		public JsonObject toJson() {
			JsonObject ret = new JsonObject();
			ret.addProperty("type","argument");
			if(target>=0)
				ret.addProperty("target", target);
			return ret;
		}
		/* (non-Javadoc)
		 * @see com.google.sites.clibonlineprogram.pokemonsms.text.TextComponent#format(com.google.sites.clibonlineprogram.pokemonsms.text.TextComponent[], int)
		 */
		@Override
		public TextComponent format(TextComponent[] args, AtomicInteger depth) {
			TextComponent base = null;
			if(target>=0)
				base = args[target].format(args, depth);
			else
			{
				AtomicInteger ndepth = new AtomicInteger();
				TextComponent[] nargs = new TextComponent[args.length-depth.get()];
				System.arraycopy(args, depth.get(), nargs, 0, args.length-depth.get());
				base = args[depth.get()].format(nargs, ndepth);
				depth.addAndGet(ndepth.get());
			}
			base.appendComponent(this.getNext().format(args, depth));
			return base;
		}
		/* (non-Javadoc)
		 * @see com.google.sites.clibonlineprogram.pokemonsms.text.TextComponent#getAsFormatted()
		 */
		@Override
		public TextComponent getAsFormatted() {
			// TODO Auto-generated method stub
			throw new IllegalStateException("This Text component cannot be formatted without arguments");
		}
		/* (non-Javadoc)
		 * @see com.google.sites.clibonlineprogram.pokemonsms.text.TextComponent#prependToken()
		 */
		@Override
		protected String prependToken() {
			throw new IllegalStateException("This text component cannot be displayed until formatted.");
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + target;
			return result;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!super.equals(obj)) {
				return false;
			}
			ArgumentTextComponent other = (ArgumentTextComponent) obj;
			if (target != other.target) {
				return false;
			}
			return true;
		}
		@Override
		protected Point drawOnComponent(Component comp, Point start, Font f, Color c) {
			throw new UnsupportedOperationException("Can't draw an argument to the screen");
		}

	}

	/**
	 * Represents a "TextComponent" which is actually an image. This can be a full image or a subportion of a tilemap.
	 * @author Connor Horman
	 *
	 */
	public static class GraphicTextComponent extends TextComponent{

		private Rectangle subPosition;
		public GraphicTextComponent(String resourceKey) {
			super("graphic", resourceKey);
			// TODO Auto-generated constructor stub
		}
		public GraphicTextComponent(String resourceKey,Rectangle subPosition) {
			super("graphic",resourceKey);
			this.subPosition = (Rectangle) subPosition.clone();
		}
		@Override
		protected Point drawOnComponent(Component comp, Point start, Font f, Color c) {
			try {
			Graphics g = comp.getGraphics();
			Resource r = PokemonSmsGame.currResourcePack.getResource(this.toUnformattedString(), ResourceType.GRAPHIC);
			BufferedImage img = r.getAsImage();
			if(subPosition==null) {
				int length = img.getWidth();
				int height = img.getHeight();
				g.drawImage(img, start.x, start.y, null);
				start.x += length;
			}
			return null;
			}catch(IOException e) {
				throw new IllegalStateException("Loading image provoked IOError",e);
			}
		}



	}

	private String text;
	private String mode;
	private TextComponent next;

	public TextComponent(String mode,String txt) {
		// TODO Auto-generated constructor stub
	}
	public TextComponent appendComponent(TextComponent c) {
		if(c==this)
			return this;
		else if(next!=null)
		{
			next.appendComponent(c);
			return this;
		}else
		{
			next = c;
			return this;
		}
	}
	public JsonObject toJson() {
		JsonObject ret = new JsonObject();
		ret.addProperty("type", mode);
		ret.addProperty("text", text);
		if(next!=null)
			ret.add("continue", next.toJson());
		return ret;
	}

	public TextComponent clone() {
		try {
			TextComponent clone = (TextComponent)super.clone();
			if(this.next!=null)
				clone.next = next.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException("Bad State found");
		}
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mode == null) ? 0 : mode.hashCode());
		result = prime * result + ((next == null) ? 0 : next.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj.getClass()!=this.getClass())
			return false;

		TextComponent other = (TextComponent) obj;
		if (mode == null) {
			if (other.mode != null) {
				return false;
			}
		} else if (!mode.equals(other.mode)) {
			return false;
		}
		if (next == null) {
			if (other.next != null) {
				return false;
			}
		} else if (!next.equals(other.next)) {
			return false;
		}
		if (text == null) {
			if (other.text != null) {
				return false;
			}
		} else if (!text.equals(other.text)) {
			return false;
		}
		return true;
	}

	public TextComponent getAsFormatted() {
		return this;
	}

	public TextComponent format(TextComponent[] args,AtomicInteger deapth) {
		TextComponent base = this.clone();
		base.next = null;
		if(this.next!=null)
			base.appendComponent(next.format(args,deapth));

		return base;
	}
	protected String prependToken() {
		return " ";
	}

	public final String toUnformattedString() {
		return prependToken()+text;
	}
	public String getType() {
		// TODO Auto-generated method stub
		return mode;
	}
	public TextComponent getNext() {
		// TODO Auto-generated method stub
		return next;
	}
	public Point drawOn(Component c,Point start,Font f,Color color) {
		if(f==null)
			f = new Font(DEFAULT_FAMILY,Font.PLAIN,DEFAULT_SIZE);
		this.drawOnComponent(c, start, f, color);
		this.next.drawOn(c, start, f, color);
		return start;
	}
	protected Point drawOnComponent(Component comp,Point start,Font f,Color c) {
		Graphics2D graphics = (Graphics2D) comp.getGraphics();
		graphics.setFont(f);
		graphics.setColor(c);
		Rectangle rect = f.getStringBounds(prependToken()+text, graphics.getFontRenderContext()).getBounds();
		graphics.drawString(text, start.x,start.y);
		start.x += rect.getWidth();
		return start;
	}



}
