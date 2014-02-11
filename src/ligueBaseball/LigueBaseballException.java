package ligueBaseball;

/**
 * L'exception LigueBaseballException est levee lorsqu'une transaction est inadequate.
 */

public final class LigueBaseballException extends Exception
{
	private static final long serialVersionUID = 1L;

	public LigueBaseballException(String message)
	{
		super(message);
	}
}