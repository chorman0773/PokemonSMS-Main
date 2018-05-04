package com.google.sites.clibonlineprogram.pokemonsms.save;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.NoSuchPaddingException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.google.sites.clibonlineprogram.pokemonsms.PokemonSmsGame;
import com.google.sites.clibonlineprogram.pokemonsms.save.nbt.NBTTagCompound;
import com.google.sites.clibonlineprogram.pokemonsms.text.TextComponent;
import com.google.sites.clibonlineprogram.pokemonsms.text.TextComponent.TextComponentString;
import com.google.sites.clibonlineprogram.pokemonsms.text.TextComponent.TextComponentFormatted;
import com.google.sites.clibonlineprogram.pokemonsms.util.Base64Images;

public class SaveTable {
	private static final File saveDir = new File(PokemonSmsGame.getInstance().getDirectory(),"saves");
	private Connection con;
	private int cid;
	private List<SaveRow> saves = new ArrayList<>();
	private class SaveRow{
		private TextComponent name;
		private File path;
		private boolean isEncrypted;
		private int playhours;
		private int playminutes;
		private BufferedImage img;
		private int id;
		SaveRow(int id,TextComponent name,File path,boolean isEncrypted,int playhours,int playminutes,BufferedImage img){
			this.name = name;
			this.path = path;
			this.isEncrypted = isEncrypted;
			this.playhours = playhours;
			this.playminutes = playminutes;
			this.img = img;
		}
	}
	private static TextComponent lockedIcon = new TextComponent.GraphicTextComponent("misc.menus.savelocked");
	public enum VerificationCode{
		OK, FILE_NOT_FOUND, FILE_MALFORMED, IOERROR
	}
	private static final String deleteStat = "DELETE * FROM SaveData WHERE Id=?";
	private final PreparedStatement delete;
	public SaveTable(Connection con) throws SQLException {
		con.setAutoCommit(true);
		ResultSet rs = con.getMetaData().getTables(null, null, null, null);
		boolean foundSaveData = false;
		if(rs.first())
			do {
				if(rs.getString(2).equals("SaveData"))
					foundSaveData = true;
			}while(rs.next());

		if(!foundSaveData) {
			String stat = "CREATE TABLE SaveData"+
							"('Id' Integer Primary Key Autoincrement NON NULL,"+
							"'NameComponent' Text NON NULL,"+
							"'Path' Text NON NULL," +
							"'PlayHours' Integer NON NULL" +
							"'PlayMinutes' Integer NON NULL" +
							"'IsEncrypted' Text NON NULL" +
							"'IconData' Text NON NULL)";
			con.createStatement().executeUpdate(stat);
		}
		this.con = con;
		this.loadRows();
		delete = this.con.prepareStatement(deleteStat);

	}
	private void loadRows() throws SQLException {
		Statement s = con.createStatement();
		ResultSet rs = s.executeQuery("SELECT * FROM SaveData");
		rs.beforeFirst();
		while(rs.next()) {
			int id = rs.getInt("Id");
			String jsonName = rs.getString("NameComponent");
			String fileName = rs.getString("Path");
			boolean isEncrypted = Boolean.parseBoolean(rs.getString("IsEncrypted"));
			int playHours = rs.getInt("PlayHours");
			int playMinutes = rs.getInt("PlayMinutes");
			String imgData = rs.getString("IconData");
			TextComponent name = PokemonSmsGame.gson.fromJson(jsonName, TextComponent.class);
			File path = new File(saveDir,fileName);
			saves.add(new SaveRow(id,name,path,isEncrypted,playHours,playMinutes,Base64Images.b642img(imgData)));
			cid = id;
		}
		cid++;

	}
	public VerificationCode verify(int id){
		try {
			SaveRow row = saves.get(id);
			if(!row.path.exists())
				return VerificationCode.FILE_NOT_FOUND;
			if(row.isEncrypted) {
				if(!SaveFileLocker.verifyEncrypted(row.path))
					return VerificationCode.FILE_MALFORMED;
			}else if(!ShadeResources.verify(row.path))
				return VerificationCode.FILE_MALFORMED;
			return VerificationCode.OK;
		}catch(IOException e) {
			return VerificationCode.IOERROR;
		}
	}
	public void deleteFile(int id) throws SQLException {
		SaveRow row = saves.get(id);
		row.path.delete();
		this.delete.setInt(0, id);
		this.delete.executeUpdate();
	}
	public void save(int id,LoadedSaveData data) throws IOException {
		SaveRow row = saves.get(id);
		row.isEncrypted = false;
		writeFileInfo(false,row);
		ShadeResources.save(data.getData(),row.path);
	}
	public void savePreEncrypted(int id,LoadedSaveData data)throws IOException, GeneralSecurityException{
		NBTTagCompound comp = data.getData();
		String s = comp.getTagCompound("Header").getString("Password");
		byte[] password = s.getBytes(StandardCharsets.UTF_8);
		saveEncrypted(id,data,password);
	}
	public void saveEncrypted(int id,LoadedSaveData data,byte[] password) throws IOException, GeneralSecurityException {
		SaveRow row = saves.get(id);
		row.isEncrypted = true;
		writeFileInfo(false,row);
		SaveFileLocker locker = SaveFileLocker.newInstance(password, row.path);
		locker.writeSaveData(data.getData());
		locker.close();
	}
	public LoadedSaveData readUnencrypted(int id) throws IOException {
		SaveRow row = saves.get(id);
		return ShadeResources.load(row.path);
	}
	public LoadedSaveData readEncrypted(byte[] password,int id) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IOException {
		SaveRow row = saves.get(id);
		return SaveFileLocker.SaveFileUnlocker.read(password, row.path);
	}

	private void writeFileInfo(boolean create,SaveRow row) {
		try {
			String stat;
			if(create)
				stat = "INSERT INTO SaveData ("+
						row.id+",'"+
						PokemonSmsGame.gson.toJson(row.name.toJson())+"',"+
						row.path.getName()+","+
						row.playhours+","+
						row.playminutes+","+
						row.isEncrypted+"',"+
						Base64Images.img2b64(row.img)+"')";
			else
				stat = "UPDATE SaveData SET Id="+row.id+","+
						"NameComponent='"+PokemonSmsGame.gson.toJson(row.name.toJson())+"',"+
						"Path="+row.path.getName()+","+
						"PlayHours="+row.playhours+","+
						"PlayMinutes="+row.playminutes+","+
						"IsEncrypted="+row.isEncrypted+","+
						"ImageData='"+Base64Images.img2b64(row.img)+"' WHERE Id="+row.id;
				con.createStatement().executeUpdate(stat);
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Creates a new Save file by adding a row to the save table.
	 * @param name
	 * @param img the Image for the player
	 * @return the id of the new file
	 */
	public int newFile(String name,BufferedImage img) {
		int id = cid++;
		SaveRow row = new SaveRow(id,new TextComponentString(name),new File(saveDir,""),false,0,0,img);
		saves.add(row);
		writeFileInfo(true,row);
		return id;
	}

	public Component getFileComponent(int id,Container owner) {
		VerificationCode code = verify(id);
		SaveRow row = saves.get(id);

		Container ret = new Panel();
		ret.setLayout(new GridBagLayout());
		owner.add(ret);
		JLabel name = new JLabel();
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.fill = GridBagConstraints.BOTH;
		ret.add(name,constraints);
		row.name.drawOn(name, new Point(), null, null);
		JLabel image = new JLabel();
		Icon i = new ImageIcon(row.img);
		image.setIcon(i);
		constraints.anchor = GridBagConstraints.EAST;
		ret.add(name,constraints);
		JLabel locked = new JLabel();
		constraints.anchor = GridBagConstraints.WEST;
		ret.add(locked,constraints);
		if(row.isEncrypted)
			lockedIcon.drawOn(locked, new Point(), null, null);

		return ret;
	}


}
