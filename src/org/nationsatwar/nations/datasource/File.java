package org.nationsatwar.nations.datasource;

import java.util.ArrayList;

import org.nationsatwar.nations.Nations;
import org.nationsatwar.nations.objects.NationsObject;

public class File extends DataSource {

	public File(Nations instance) {
		super(instance);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean save(NationsObject obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean load(NationsObject obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(NationsObject obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<NationsObject> gatherDataset(NationsObject obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean reloadDatabase() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveDatabase() {
		// TODO Auto-generated method stub
		return false;
	}

}
