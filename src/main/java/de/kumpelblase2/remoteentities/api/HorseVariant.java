package de.kumpelblase2.remoteentities.api;

public enum HorseVariant
{
	WHITE,
	CREAMY,
	CHESTNUT,
	BROWN,
	BLACK,
	GRAY,
	DARK_BROWN,
	INVISIBLE,
	WHITE_WHITE(256),
	CREAMY_WHITE(257),
	CHESTNUT_WHITE(258),
	BROWN_WHITE(259),
	BLACK_WHITE(260),
	GRAY_WHITE(261),
	DARK_BROWN_WHITE(262),
	WHITE_WHITE_FIELD(512),
	CREAMY_WHITE_FIELD(513),
	CHESTNUT_WHITE_FIELD(514),
	BROWN_WHITE_FIELD(515),
	BLACK_WHITE_FIELD(516),
	GRAY_WHITE_FIELD(517),
	DARK_BROWN_WHITE_FIELD(518),
	WHITE_WHITE_DOTS(768),
	CREAMY_WHITE_DOTS(769),
	CHESTNUT_WHITE_DOTS(770),
	BROWN_WHITE_DOTS(771),
	BLACK_WHITE_DOTS(772),
	GRAY_WHITE_DOTS(773),
	DARK_BROWN_WHITE_DOTS(774),
	WHITE_BLACK_DOTS(1024),
	CREAMY_BLACK_DOTS(1025),
	CHESTNUT_BLACK_DOTS(1026),
	BROWN_BLACK_DOTS(1027),
	BLACK_BLACK_DOTS(1028),
	GRAY_BLACK_DOTS(1029),
	DARK_BROWN_BLACK_DOTS(1030);

	private final int m_id;

	private HorseVariant()
	{
		this.m_id = this.ordinal();
	}

	private HorseVariant(int inID)
	{
		this.m_id = inID;
	}

	public int getID()
	{
		return this.m_id;
	}

	public static HorseVariant getByID(int inID)
	{
		for(HorseVariant variant : values())
		{
			if(variant.getID() == inID)
				return variant;
		}

		return null;
	}
}