package org.nationsatwar.nations.datasource;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.plugin.PluginBase;
import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.NationsObject;
import org.nationsatwar.nations.objects.Rank.RankType;

public class MySQL extends DataSource {
	private static final String SQL_DRIVER = "com.mysql.jdbc.Driver";
	
	
	enum ColumnType {
		STRING, STRING_KEY, STRING_LIST, INT, INT_KEY, INT_LIST, DOUBLE, DOUBLE_LIST, BOOL, DATETIME, OBJECT, HASHMAP_STRING_DOUBLE, HASHMAP_INT_INT, RANKTYPE
	};
	
	private String DB_NAME;
	private String URL;
	private String USER;
	private String PASS;
	private String TABLE_PREFIX;
	
	private HashMap<String, LinkedList<String>> columnNames;
	private HashMap<String, HashMap<String, ColumnType>> columnTypes;
	private HashMap<String, HashMap<String, String>> queries;
	
	public MySQL(PluginBase plugin) {
		super(plugin);
		DB_NAME = plugin.getConfig().getString("datasource.mysql.db");
		URL = "jdbc:mysql://" + plugin.getConfig().getString("datasource.mysql.url") + ":" + plugin.getConfig().getString("datasource.mysql.port") + "/";
		USER = plugin.getConfig().getString("datasource.mysql.user");
		PASS = plugin.getConfig().getString("datasource.mysql.pass");
		TABLE_PREFIX = "nations_";
		
		this.checkTables();
		
		columnNames = new HashMap<String, LinkedList<String>>();
		columnTypes = new HashMap<String, HashMap<String, ColumnType>>();
		queries = new HashMap<String, HashMap<String, String>>();
		queries.put("where", new HashMap<String, String>());
		queries.put("savelist", new HashMap<String, String>());
		queries.put("save", new HashMap<String, String>());
	}
	
	private Connection getConnection() {
		Connection conn;
		try {
			Class.forName(SQL_DRIVER);
			conn = DriverManager.getConnection(URL + DB_NAME, USER, PASS);
		} catch (SQLException e) {
			plugin.getLogger().log(Level.SEVERE, "SQL Database connection failed! " + e.getMessage());
			return null;
		} catch (ClassNotFoundException e) {
			plugin.getLogger().log(Level.SEVERE, "SQL Database connection failed! " + e.getMessage());
			return null;
		}
		
		return conn;
	}
	
	private boolean checkTables() {
		Connection conn = this.getConnection();
		if(conn == null) {
			return false;
		}
		
		Statement state;
		DatabaseMetaData dbm;
		try {
			state = conn.createStatement();
			dbm = conn.getMetaData();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			plugin.getLogger().log(Level.SEVERE, "SQL Database connection failed! " + e.getMessage());
			return false;
		}

		
		Nations nations;
		if(plugin instanceof Nations) {
			nations = (Nations) plugin;
		} else {
			return false;
		}
		
		if(nations.inviteManager != null) {
			this.createTable(dbm, state, TABLE_PREFIX+"invite", "(`id` int(11) NOT NULL,`type` varchar(40) NOT NULL,`invitee` int(11) NOT NULL,`inviter` int(11) NOT NULL,PRIMARY KEY (`id`))");
		}
		if(nations.nationManager != null) {
			this.createTable(dbm, state, TABLE_PREFIX+"nation", "(`id` int(11) NOT NULL,  `name` varchar(40) NOT NULL,  PRIMARY KEY (`id`))");
			this.createTable(dbm, state, TABLE_PREFIX+"nation_customranks", "(`id` int(11) NOT NULL COMMENT 'nationid',  `_item` varchar(40) NOT NULL DEFAULT '' COMMENT 'rankid')");
			this.createTable(dbm, state, TABLE_PREFIX+"nation_members", "(`id` int(11) NOT NULL COMMENT 'nationid',  `_idx` int(11) NOT NULL COMMENT 'userid',  `_item` varchar(40) NOT NULL DEFAULT '' COMMENT 'rankid',  UNIQUE KEY `_idx` (`_idx`)");
			this.createTable(dbm, state, TABLE_PREFIX+"nation_towns", "(`id` int(11) NOT NULL COMMENT 'nationid',  `_item` int(11) NOT NULL COMMENT 'townid',  `_idx` int(11) DEFAULT NULL COMMENT 'nationOrder',  UNIQUE KEY `_item` (`_item`))");	
		}
		if(nations.plotManager != null) {
			this.createTable(dbm, state, TABLE_PREFIX+"plot", "(`id` int(11) NOT NULL,  `world` varchar(40) NOT NULL,  `x` int(11) NOT NULL,  `z` int(11) NOT NULL, `locationDescription` varchar(80) NOT NULL DEFAULT '', PRIMARY KEY (`id`)))");
		}
		if(nations.rankManager != null) {
			this.createTable(dbm, state, TABLE_PREFIX+"rank", "(`id` int(11) NOT NULL,  `name` varchar(40) NOT NULL,  `type` varchar(40) NOT NULL,  PRIMARY KEY (`id`)");
		}
		if(nations.townManager != null) {
			this.createTable(dbm, state, TABLE_PREFIX+"town", "(`id` int(11) NOT NULL,  `name` varchar(40) NOT NULL,  PRIMARY KEY (`id`)");
			this.createTable(dbm, state, TABLE_PREFIX+"town_customranks", "(`id` int(11) NOT NULL COMMENT 'townid',  `_item` varchar(40) NOT NULL DEFAULT '' COMMENT 'rankid')");
			this.createTable(dbm, state, TABLE_PREFIX+"town_members", "(`id` int(11) NOT NULL COMMENT 'townid',  `_idx` int(11) NOT NULL COMMENT 'userid',  `_item` varchar(40) NOT NULL DEFAULT '' COMMENT 'rankid',  UNIQUE KEY `_idx` (`_idx`)");
			this.createTable(dbm, state, TABLE_PREFIX+"town_plots", "(`id` int(11) NOT NULL COMMENT 'townid',  `_item` int(11) NOT NULL COMMENT 'plotid',  `_idx` int(11) DEFAULT NULL,  UNIQUE KEY `_item` (`_item`))");	
		}
		if(nations.userManager != null) {
			this.createTable(dbm, state, TABLE_PREFIX+"user", "(`id` int(11) NOT NULL,  `name` varchar(40) NOT NULL,  PRIMARY KEY (`id`),  UNIQUE KEY `name` (`name`))");
		}
		
		try {
			state.close();
			conn.close();
		} catch (SQLException e) {
			plugin.getLogger().log(Level.SEVERE, "SQL Database connection failed! " + e.getMessage());
			return false;
		}
		return true;
	}
	
	private boolean createTable(DatabaseMetaData dbm, Statement state, String table, String query) {
		try {
			if (!dbm.getTables(null, null, table, null).next()) {
				plugin.getLogger().log(Level.INFO, "Creating table " + table + ".");
				state.execute("CREATE TABLE `" + table + "` " + query);
				if (!dbm.getTables(null, null, table, null).next())
					throw new SQLException("Table " + table + " not found and failed to create");
			}
		} catch (SQLException e) {
			plugin.getLogger().log(Level.SEVERE, "SQL Database connection failed! " + e.getMessage());
			return false;
		}
		return true;
	}

	private HashMap<String, ColumnType> getColumns(String objtype) {

		// cached?
		//HashMap<String, ColumnType> map = null;
		HashMap<String, ColumnType> map = columnTypes.get(objtype);
		if (map == null) {
			// build the column list
			map = new HashMap<String, ColumnType>();
			if (objtype.equals("invite")) {
				map.put("id", ColumnType.INT_KEY);
				map.put("type", ColumnType.STRING);
				map.put("invitee", ColumnType.INT);
				map.put("inviter", ColumnType.INT);
			} else if (objtype.equals("nation")) {
				map.put("id", ColumnType.INT_KEY);
				map.put("name", ColumnType.STRING);
				map.put("customRanks", ColumnType.INT_LIST);
				map.put("members", ColumnType.HASHMAP_INT_INT);
				map.put("towns", ColumnType.INT_LIST);
			} else if (objtype.equals("plot")) {
				map.put("id", ColumnType.INT_KEY);
				map.put("world", ColumnType.STRING);
				map.put("x", ColumnType.INT);
				map.put("z", ColumnType.INT);
				map.put("locationDescription", ColumnType.STRING);
			} else if (objtype.equals("rank")) {
				map.put("id", ColumnType.INT_KEY);
				map.put("name", ColumnType.STRING);
				map.put("type", ColumnType.RANKTYPE);
			} else if (objtype.equals("town")) {
				map.put("id", ColumnType.INT_KEY);
				map.put("name", ColumnType.STRING);
				map.put("customRanks", ColumnType.INT_LIST);
				map.put("members", ColumnType.HASHMAP_INT_INT);
				map.put("plots", ColumnType.INT_LIST);
			} else if (objtype.equals("user")) {
				map.put("id", ColumnType.INT_KEY);
				map.put("name", ColumnType.STRING);
			} else if (objtype.equals("alliance")) {
				map.put("allyID", ColumnType.INT_KEY);
				map.put("name", ColumnType.STRING);
				map.put("militaryNations", ColumnType.STRING_LIST);
				map.put("protectedNations", ColumnType.STRING_LIST);
				map.put("militaryInvites", ColumnType.STRING_LIST);
				map.put("protectedInvites", ColumnType.STRING_LIST);
			}
			// use an ordered collection just so that we don't assume that the hashset keys will always be the same order
			columnNames.put(objtype, new LinkedList<String>(map.keySet()));
			// add to cache
			columnTypes.put(objtype, map);
		}
		return map;
	}

	private String getQuery(String query, String objtype) {

		String sql = "";
		try {
			// cached?
			sql = null;
			sql = queries.get(query).get(objtype);
			if (sql == null) {
				if (query.equals("where")) {
					HashMap<String, ColumnType> columns = getColumns(objtype);
					StringBuilder key = new StringBuilder();
					for (String columnName : columnNames.get(objtype)) {
						String quotedName = '`' + columnName + '`';
						ColumnType columnType = columns.get(columnName);
						if (columnType == ColumnType.STRING_KEY || columnType == ColumnType.INT_KEY) {
							if (key.length() > 0) {
								key.append(" AND ");
							}
							key.append(quotedName);
							key.append("=?");
						}
					}
					sql = key.toString();
				} else if (query.equals("savelist")) {
					HashMap<String, ColumnType> columns = getColumns(objtype);
					StringBuilder names = new StringBuilder(), values = new StringBuilder();
					for (String columnName : columnNames.get(objtype)) {
						String quotedName = '`' + columnName + '`';
						ColumnType columnType = columns.get(columnName);
						if (columnType == ColumnType.STRING_KEY || columnType == ColumnType.INT_KEY) {
							if (names.length() > 0) {
								names.append(",");
								values.append(",");
							}
							names.append(quotedName);
							values.append("?");
						}
						/*if (columnType != ColumnType.STRING_KEY && columnType != ColumnType.INT_KEY) {
							if (update.length() > 0) update.append(",");
							update.append(quotedName).append("=VALUES(").append(quotedName).append(")");
						}*/
					}
					sql = "(" + names + ",_idx,_item) VALUES (" + values + ",?,?)";
					//sql = "(" + names + ") VALUES (" + values + ")";
				} else if (query.equals("save")) {
					String table = TABLE_PREFIX + objtype;
					StringBuilder names = new StringBuilder(), values = new StringBuilder(), update = new StringBuilder();
					HashMap<String, ColumnType> columns = getColumns(objtype);
					for (String columnName : columnNames.get(objtype)) {
						String quotedName = '`' + columnName + '`';
						ColumnType columnType = columns.get(columnName);
						if (columnType != ColumnType.STRING_LIST && columnType != ColumnType.INT_LIST
								&& columnType != ColumnType.DOUBLE_LIST && columnType != ColumnType.HASHMAP_STRING_DOUBLE && columnType != ColumnType.HASHMAP_INT_INT) {
							if (names.length() > 0) {
								names.append(",");
								values.append(",");
							}
							names.append(quotedName);
							values.append("?");
							if (columnType != ColumnType.STRING_KEY && columnType != ColumnType.INT_KEY) {
								if (update.length() > 0) update.append(",");
								update.append(quotedName).append("=VALUES(").append(quotedName).append(")");
							}
						}
					}
					if(update.length() < 1) {
						sql = "INSERT INTO " + table + " (" + names + ") VALUES (" + values + ")";
					} else {
						sql = "INSERT INTO " + table + " (" + names + ") VALUES (" + values + ") ON DUPLICATE KEY UPDATE " + update;
					}
				}
				queries.get(query).put(objtype, sql);
				plugin.getLogger().log(Level.FINE, sql);
			}
		} catch (Exception e) {
			plugin.getLogger().log(Level.SEVERE,"getQuery failed (" + query + ":" + objtype + "): " +e.getMessage() + " - "+sql);
			e.printStackTrace();
			sql = "";
		}
		return sql;
	}

	private int prepareKey(PreparedStatement stmt, NationsObject obj) {

		try {
			String objtype = obj.getClass().getSimpleName().toLowerCase();
			HashMap<String, ColumnType> types = getColumns(objtype);
			int i = 1;
			for (String key : columnNames.get(objtype)) {
				Field field = this.getField(obj.getClass(), key);
				//Field field = obj.getClass().getDeclaredField(key);
				field.setAccessible(true);
				switch (types.get(key)) {
				case STRING_KEY:
					stmt.setString(i++, (String) field.get(obj));
					break;
				case INT_KEY:
					stmt.setInt(i++, field.getInt(obj));
					break;
				}
			}
			return i - 1;
		} catch (Exception e) {
			plugin.getLogger().log(Level.WARNING,"Error preparing key (" + obj.toString() + "): " + e + " - " + e.getMessage());
		}
		return 0;
	}

	@Override
	public boolean save(NationsObject obj) {

		Connection conn = this.getConnection();
		if(conn == null) {
			return false;
		}
		
		
		if (obj == null) return false;
		String objtype = obj.getClass().getSimpleName().toLowerCase();
		try {
			conn.setAutoCommit(false);
			PreparedStatement stmt = conn.prepareStatement(getQuery("save", objtype));
			HashMap<String, ColumnType> types = getColumns(objtype);
			// populate the parameters
			int i = 1;
			boolean hasLists = false;
			for (String name : columnNames.get(objtype)) {
				Field field = this.getField(obj.getClass(), name);
				//Field field = obj.getClass().getDeclaredField(name);
				field.setAccessible(true);
				switch (types.get(name)) {
				case STRING:
				case STRING_KEY:
					stmt.setString(i++, (String) field.get(obj));
					break;
				case RANKTYPE:
					stmt.setString(i++, field.get(obj).toString());
					break;
				case INT:
				case INT_KEY:
					stmt.setInt(i++, field.getInt(obj));
					break;
				case DOUBLE:
					stmt.setDouble(i++, field.getDouble(obj));
					break;
				case BOOL:
					stmt.setInt(i++, field.getBoolean(obj) ? 1 : 0);
					break;
				case DATETIME:
					java.sql.Timestamp ts = new java.sql.Timestamp(0);
					Object fieldobj = field.get(obj);
					if (fieldobj instanceof Date)
						ts.setTime(((Date)fieldobj).getTime());
					else if (fieldobj instanceof Calendar)
						ts.setTime(((Calendar)fieldobj).getTimeInMillis());
					else
						throw new Exception("Invalid class for DATETIME column.");
					stmt.setTimestamp(i++, ts);
					break;
				case STRING_LIST:
				case INT_LIST:
				case DOUBLE_LIST:
				case HASHMAP_INT_INT:
				case HASHMAP_STRING_DOUBLE:
					hasLists = true;
					break;
				}
			}
			plugin.getLogger().log(Level.INFO, stmt.toString());
			stmt.executeUpdate();
			stmt.close();
			// save any lists
			if (hasLists) {
				for (String name : columnNames.get(objtype)) {
					switch (types.get(name)) {
					case STRING_LIST:
					case INT_LIST:
					case DOUBLE_LIST:
					case HASHMAP_STRING_DOUBLE:
					case HASHMAP_INT_INT:
						Field fieldlist = this.getField(obj.getClass(), name);
						//Field fieldlist = obj.getClass().getDeclaredField(name);
						fieldlist.setAccessible(true);
						String table = TABLE_PREFIX + objtype + "_" + name;
						//PreparedStatement clear = conn.prepareStatement("DELETE FROM " + table + " WHERE " + getQuery("where", objtype));
						//prepareKey(clear, obj);
						//clear.executeUpdate();
						//clear.close();
						PreparedStatement add = conn.prepareStatement("REPLACE INTO " + table + " " + getQuery("savelist", objtype));
						int numkeys = prepareKey(add, obj);
						int idx = 0;
						if(fieldlist.get(obj) instanceof ArrayList) {
							for (Object listitem : (ArrayList<?>) fieldlist.get(obj)) {
								add.setInt(numkeys + 1, idx++);
								switch (types.get(name)) {
								case STRING_LIST:
									add.setString(numkeys + 2, (String) listitem);
									break;
								case INT_LIST:
									add.setInt(numkeys + 2, (Integer) listitem);
									break;
								case DOUBLE_LIST:
									add.setDouble(numkeys + 2, (Double) listitem);
								}
								add.executeUpdate();
							}						
						}
						if(fieldlist.get(obj) instanceof HashMap<?, ?>) {
							Map<?, ?> map = (HashMap<?,?>) fieldlist.get(obj);
							for(Map.Entry<?, ?> entry : map.entrySet()) {
								//add.setInt(numkeys + 1, idx++);
								switch (types.get(name)) {
								case HASHMAP_STRING_DOUBLE:
									add.setString(numkeys + 1, (String) entry.getKey());
									add.setDouble(numkeys + 2, (Double) entry.getValue());
									break;
								case HASHMAP_INT_INT:
									add.setInt(numkeys + 1, (Integer) entry.getKey());
									add.setInt(numkeys + 2, (Integer) entry.getValue());
									break;
								}
								plugin.getLogger().log(Level.FINE, add.toString());
								add.executeUpdate();
							}				
						}
						add.close();
					}
				}
			}
			conn.commit();
			conn.setAutoCommit(true);
			conn.close();
			return true;
		} catch (Exception e) {
			plugin.getLogger().log(Level.SEVERE, "Failed to save (" + obj.toString() + "): " + e + " - " + e.getMessage());
			e.printStackTrace();
			try {
				conn.rollback();
				conn.setAutoCommit(true);
				conn.close();
			} catch (SQLException sql_e) {
				plugin.getLogger().log(Level.SEVERE, "Error rolling back: " + sql_e);
			}
		}
		return false;
	}

	@Override
	public boolean load(NationsObject obj) {

		Connection conn = this.getConnection();
		if(conn == null) {
			return false;
		}
		
		
		if (obj == null) return false;
		String objtype = obj.getClass().getSimpleName().toLowerCase();
		try {
			String table = TABLE_PREFIX + objtype;
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + table + " WHERE " + getQuery("where", objtype));
			prepareKey(stmt, obj);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				HashMap<String, ColumnType> types = getColumns(objtype);
				boolean hasLists = false;
				for (String name : columnNames.get(objtype)) {
					Field field = this.getField(obj.getClass(), name);
					//Field field = obj.getClass().getDeclaredField(name);
					field.setAccessible(true);
					switch (types.get(name)) {
					case STRING:
						field.set(obj, rs.getString(name));
						break;
					case RANKTYPE:
						field.set(obj, RankType.valueOf(rs.getString(name)));
						break;
					case INT:
						field.setInt(obj, rs.getInt(name));
						break;
					case DOUBLE:
						field.setDouble(obj, rs.getDouble(name));
						break;
					case BOOL:
						field.setBoolean(obj, rs.getInt(name) != 0);
						break;
					case DATETIME:
						java.sql.Timestamp ts = rs.getTimestamp(name);
						Object fieldobj = field.get(obj);
						if (fieldobj instanceof Date)
							((Date)fieldobj).setTime(ts.getTime());
						else if (fieldobj instanceof Calendar)
							((Calendar)fieldobj).setTimeInMillis(ts.getTime());
						else
							throw new Exception("Invalid class for DATETIME column.");
						break;
					case STRING_LIST:
					case INT_LIST:
					case DOUBLE_LIST:
					case HASHMAP_STRING_DOUBLE:
					case HASHMAP_INT_INT:
						hasLists = true;
					}
					
				}
				if (hasLists) {
					for (String name : columnNames.get(objtype)) {
						Field field = this.getField(obj.getClass(), name);
						//Field field = obj.getClass().getDeclaredField(name);
						field.setAccessible(true);
						
						switch (types.get(name)) {
						case STRING_LIST:
						case INT_LIST:
						case DOUBLE_LIST:
							String listtable = TABLE_PREFIX + objtype + "_" + name;
							PreparedStatement getlist = conn.prepareStatement("SELECT _item FROM " + listtable + " WHERE " + getQuery("where", objtype) + " ORDER BY _idx");
							prepareKey(getlist, obj);
							ResultSet listrs = getlist.executeQuery();
							if (types.get(name) == ColumnType.STRING_LIST) {
								ArrayList<String> list = new ArrayList<String>();
								while (listrs.next())
									list.add(listrs.getString(1));
								field.set(obj, list);
							} else if (types.get(name) == ColumnType.INT_LIST) {
								ArrayList<Integer> list = new ArrayList<Integer>();
								while (listrs.next())
									list.add(listrs.getInt(1));
								field.set(obj, list);
							} else if (types.get(name) == ColumnType.DOUBLE_LIST) {
								ArrayList<Double> list = new ArrayList<Double>();
								while(listrs.next())
									list.add(listrs.getDouble(1));
								field.set(obj, list);
							}
							listrs.close();
							getlist.close();
							break;
						case HASHMAP_STRING_DOUBLE:
							listtable = TABLE_PREFIX + objtype + "_" + name;
							getlist = conn.prepareStatement("SELECT _idx,_item FROM " + listtable + " WHERE " + getQuery("where", objtype));
							prepareKey(getlist, obj);
							listrs = getlist.executeQuery();
							if (types.get(name) == ColumnType.HASHMAP_STRING_DOUBLE) {
								HashMap<String, Double> list = new HashMap<String, Double>();
								while (listrs.next())
									list.put(listrs.getString(1), listrs.getDouble(2));
								field.set(obj, list);
							}
							listrs.close();
							getlist.close();
							break;			
						case HASHMAP_INT_INT:
							listtable = TABLE_PREFIX + objtype + "_" + name;
							getlist = conn.prepareStatement("SELECT _idx,_item FROM " + listtable + " WHERE " + getQuery("where", objtype));
							prepareKey(getlist, obj);
							listrs = getlist.executeQuery();
							if (types.get(name) == ColumnType.HASHMAP_INT_INT) {
								HashMap<Integer, Integer> list = new HashMap<Integer, Integer>();
								while (listrs.next())
									list.put(listrs.getInt(1), listrs.getInt(2));
								field.set(obj, list);
							}
							listrs.close();
							getlist.close();
							break;
						}
						
					}
				}
			}
			rs.close();
			plugin.getLogger().log(Level.INFO, stmt.toString());
			stmt.close();
			conn.close();
			return true;
		} catch (Exception e) {
			plugin.getLogger().log(Level.SEVERE, "Failed to load (" + obj.toString() + "): " + e + " - " + e.getMessage());
		}
		return false;
	}

	@Override
	public boolean delete(NationsObject obj) {
		Connection conn = this.getConnection();
		if(conn == null) {
			return false;
		}
		
		
		if (obj == null) return false;
		String objtype = obj.getClass().getSimpleName().toLowerCase();
		try {
			conn.setAutoCommit(false);
			String table = TABLE_PREFIX + objtype;
			PreparedStatement stmt = conn.prepareStatement("DELETE FROM " + table + " WHERE " + getQuery("where", objtype));
			prepareKey(stmt, obj);
			stmt.executeUpdate();
			stmt.close();
			HashMap<String, ColumnType> types = getColumns(objtype);
			for (String name : columnNames.get(objtype)) {
				switch (types.get(name)) {
				case STRING_LIST:
				case INT_LIST:
				case DOUBLE_LIST:
				case HASHMAP_STRING_DOUBLE:
				case HASHMAP_INT_INT:
					String listtable = TABLE_PREFIX + objtype + "_" + name;
					stmt = conn.prepareStatement("DELETE FROM " + listtable + " WHERE " + getQuery("where", objtype));
					prepareKey(stmt, obj);
					stmt.executeUpdate();
					stmt.close();
					break;
				}
			}
			conn.commit();
			conn.setAutoCommit(true);
			conn.close();
			return true;
		} catch (Exception e) {
			plugin.getLogger().log(Level.SEVERE, "Failed to delete (" + obj.toString() + "): " + e + " - " + e.getMessage());
			try {
				conn.rollback();
				conn.setAutoCommit(true);
				conn.close();
			} catch (SQLException sql_e) {
				plugin.getLogger().log(Level.SEVERE, "Error rolling back: " + sql_e);
			}
		}
		return false;
	}

	@Override
	public ArrayList<NationsObject> gatherDataset(NationsObject obj) {
		Connection conn = this.getConnection();
		if(conn == null) {
			return null;
		}
		
		
		if (obj == null) return null;
		String objtype = obj.getClass().getSimpleName().toLowerCase();
		ArrayList<NationsObject> dataset = new ArrayList<NationsObject>();
		try {
			String table = TABLE_PREFIX + objtype;
			HashMap<String, ColumnType> types = getColumns(objtype);
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + table);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				obj = obj.getClass().newInstance();
				boolean hasLists = false;
				for (String name : columnNames.get(objtype)) {
					Field field = this.getField(obj.getClass(), name);
					//Field field = obj.getClass().Field(name);
					field.setAccessible(true);
					switch (types.get(name)) {
					case STRING:
					case STRING_KEY:
						field.set(obj, rs.getString(name));
						break;
					case RANKTYPE:
						field.set(obj, RankType.valueOf(rs.getString(name)));
						break;
					case INT:
					case INT_KEY:
						field.setInt(obj, rs.getInt(name));
						break;
					case DOUBLE:
						field.setDouble(obj, rs.getDouble(name));
						break;
					case BOOL:
						field.setBoolean(obj, rs.getInt(name) != 0);
						break;
					case DATETIME:
						java.sql.Timestamp ts = rs.getTimestamp(name);
						Object fieldobj = field.get(obj);
						if (fieldobj instanceof Date)
							((Date)fieldobj).setTime(ts.getTime());
						else if (fieldobj instanceof Calendar)
							((Calendar)fieldobj).setTimeInMillis(ts.getTime());
						else
							throw new Exception("Invalid class for DATETIME column.");
						break;
					case STRING_LIST:
					case INT_LIST:
					case DOUBLE_LIST:
					case HASHMAP_STRING_DOUBLE:
					case HASHMAP_INT_INT:
						hasLists = true;
					}
				}
				if (hasLists) {
					for (String name : columnNames.get(objtype)) {
						Field field = this.getField(obj.getClass(), name);
						//Field field = obj.getClass().getDeclaredField(name);
						field.setAccessible(true);
						
						switch (types.get(name)) {
						case STRING_LIST:
						case INT_LIST:
						case DOUBLE_LIST:
							String listtable = TABLE_PREFIX + objtype + "_" + name;
							PreparedStatement getlist = conn.prepareStatement("SELECT _item FROM " + listtable + " WHERE " + getQuery("where", objtype));// + " ORDER BY _idx");
							prepareKey(getlist, obj);
							plugin.getLogger().log(Level.FINE, getlist.toString());
							ResultSet listrs = getlist.executeQuery();
							if (types.get(name) == ColumnType.STRING_LIST) {
								ArrayList<String> list = new ArrayList<String>();
								while (listrs.next())
									list.add(listrs.getString(1));
								field.set(obj, list);
							} else if (types.get(name) == ColumnType.INT_LIST) {
								ArrayList<Integer> list = new ArrayList<Integer>();
								while (listrs.next())
									list.add(listrs.getInt(1));
								field.set(obj, list);
							} else if (types.get(name) == ColumnType.DOUBLE_LIST) {
								ArrayList<Double> list = new ArrayList<Double>();
								while (listrs.next())
									list.add(listrs.getDouble(1));
								field.set(obj, list);
							}
							listrs.close();
							getlist.close();
							break;
						case HASHMAP_STRING_DOUBLE:
							listtable = TABLE_PREFIX + objtype + "_" + name;
							getlist = conn.prepareStatement("SELECT _idx,_item FROM " + listtable + " WHERE " + getQuery("where", objtype));
							prepareKey(getlist, obj);
							listrs = getlist.executeQuery();
							if (types.get(name) == ColumnType.HASHMAP_STRING_DOUBLE) {
								HashMap<String, Double> list = new HashMap<String, Double>();
								while (listrs.next())
									list.put(listrs.getString(1), listrs.getDouble(2));
								field.set(obj, list);
							}
							listrs.close();
							getlist.close();
							break;			
						case HASHMAP_INT_INT:
							listtable = TABLE_PREFIX + objtype + "_" + name;
							getlist = conn.prepareStatement("SELECT _idx,_item FROM " + listtable + " WHERE " + getQuery("where", objtype));
							prepareKey(getlist, obj);
							listrs = getlist.executeQuery();
							if (types.get(name) == ColumnType.HASHMAP_INT_INT) {
								HashMap<Integer, Integer> list = new HashMap<Integer, Integer>();
								while (listrs.next())
									list.put(listrs.getInt(1), listrs.getInt(2));
								field.set(obj, list);
							}
							listrs.close();
							getlist.close();
							break;	
						}
					}
				}
				dataset.add(obj);
			}
			rs.close();
			plugin.getLogger().log(Level.INFO, stmt.toString());
			stmt.close();
			conn.close();
		} catch (Exception e) {
			plugin.getLogger().log(Level.SEVERE, "Failed to gather (" + objtype + "): " + e + " - " + e.getMessage());
			e.printStackTrace();
		}
		return dataset;
	}
	
	 private Field getField(Class<?> theClass, String fieldName) throws NoSuchFieldException {
		try {
			return theClass.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			Class<?> superClass = theClass.getSuperclass();
			if (superClass == null) {
				throw e;
			} else {
				return getField(superClass, fieldName);
			}
		}
	}
}
