/*
 * Copyright (C) 2004-2015 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package l2r.gameserver.network.serverpackets;

import gr.sr.network.handler.ServerTypeConfigs;
import l2r.gameserver.model.L2Clan;
import l2r.gameserver.model.skills.L2Skill;

import java.util.List;

/**
 * @author -Wooden-
 */
public class PledgeSkillList extends L2GameServerPacket
{
	private final L2Skill[] _skills;
	private final List<SubPledgeSkill> _subSkills;
	
	public static class SubPledgeSkill
	{
		int _subType;
		int _skillId;
		int _skillLvl;
		
		public SubPledgeSkill(int subType, int skillId, int skillLvl)
		{
			super();
			_subType = subType;
			_skillId = skillId;
			_skillLvl = skillLvl;
		}
	}
	
	public PledgeSkillList(L2Clan clan)
	{
		_skills = clan.getAllSkills();
		_subSkills = clan.getAllSubSkills();
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		
		switch (ServerTypeConfigs.SERVER_TYPE)
		{
			case IL:
				writeH(0x39);
				break;
			case GF:
			case EPILOGUE:
			case FREYA:
			case H5:
			case GC:
			case SL:
				writeH(0x3A);
				break;
		}
		
		switch (ServerTypeConfigs.SERVER_TYPE)
		{
			case IL:
				writeD(_skills.length);
				for (L2Skill sk : _skills)
				{
					writeD(sk.getId());
					writeD(sk.getLevel());
				}
				break;
			case GF:
			case EPILOGUE:
			case FREYA:
			case H5:
				writeD(_skills.length);
				writeD(_subSkills.size()); // squad skill lenght
				for (L2Skill sk : _skills)
				{
					writeD(sk.getId());
					writeD(sk.getLevel());
				}
				for (SubPledgeSkill sk : _subSkills)
				{
					writeD(sk._subType); // Clan Sub-unit types
					writeD(sk._skillId);
					writeD(sk._skillLvl);
				}
				break;
			case GC:
			case SL:
				writeD(_skills.length);
				writeD(_subSkills.size()); // Squad skill length
				for (L2Skill sk : _skills)
				{
					writeD(sk.getDisplayId());
					writeH(sk.getDisplayLevel());
					writeH(0x00); // Sub level
				}
				for (SubPledgeSkill sk : _subSkills)
				{
					writeD(sk._subType); // Clan Sub-unit types
					writeD(sk._skillId);
					writeH(sk._skillLvl);
					writeH(0x00); // Sub level
				}
				break;
		}
	}
}