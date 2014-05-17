package de.kumpelblase2.remoteentities.api.thinking;

public enum DesireType
{
	/**
	 * Desires that happen unconsciously and should be able to get executed along every other.
	 */
	SUBCONSCIOUS,

	/**
	 * Desires which are primal instinct of the entity.
	 */
	PRIMAL_INSTINCT,

	/**
	 * Desires which might fulfill the entity with happiness.
	 */
	HAPPINESS,

	/**
	 * Desires which take up the full concentration of the entity.
	 */
	FULL_CONCENTRATION,

	/**
	 * Addition to movement which should run along most other.
	 */
	MOVEMENT_ADDITION,

	/**
	 * Desires that occasionally pop up and aren't of great importance.
	 */
	OCCASIONAL_URGE,

	/**
	 * Undefined and unused.
	 */
	UNDEFINED,

	/**
	 * A desire which includes the demand of eating something.
	 */
	FOOD;

	/**
	 * Checks if the given type of desire is compatible with this one.
	 *
	 * @param inType Type to check if both are compatible
	 * @return true if they are, false if not
	 */
	public boolean isCompatibleWith(DesireType inType)
	{
		return (this.ordinal() & inType.ordinal()) == 0;
	}
}