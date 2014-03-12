package ligueBaseball;
import java.sql.Time;
import java.text.*;
import java.util.Date;

/**
 * Permet de valider le format d'une date en YYYY-MM-DD et de la convertir en un
 * objet Date.
 * 
 * <pre>
 * 
 *  Marc Frappier - 83 427 378
 *  Universit� de Sherbrooke
 *  version 2.0 - 13 novembre 2004
 *  ift287 - exploitation de bases de donn�es
 * 
 * 
 * </pre>
 */
public class FormatDate
{
private static SimpleDateFormat formatAMJ;
private static SimpleDateFormat formatHHMM;

static
    {
    formatAMJ = new SimpleDateFormat("yyyy-MM-dd");
    formatAMJ.setLenient(false);
    formatHHMM = new SimpleDateFormat("hh:mm");
    formatHHMM.setLenient(false);
    }

public static void main(String[] args) throws ParseException
{
	for (int i = 0; i < args.length; i++) {
		System.out.println(convertirDate(args[i]));
	}
}

/**
 * Convertit une String du format YYYY-MM-DD en un objet de la classe Date.
 */
public static Date convertirDate(String dateString)
  throws ParseException
{
return formatAMJ.parse(dateString);
}

public static Time convertirTime(String timeString) throws ParseException{
	return new Time(new SimpleDateFormat("HH:mm").parse(timeString).getTime());
}

public static String toString(Date date)
{
return formatAMJ.format(date);
}
}
