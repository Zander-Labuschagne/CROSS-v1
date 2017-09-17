package cryogen;

public class InvalidProjectTitleException extends RuntimeException
{
	public InvalidProjectTitleException()
	{
		super();
	}

	public InvalidProjectTitleException(String message)
	{
		super(message);
	}
}
