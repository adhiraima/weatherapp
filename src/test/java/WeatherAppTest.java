import java.time.Instant;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class WeatherAppTest {
    public static void main(String[] args) {
        Calendar cal = Calendar.getInstance();
        //cal.setTimeInMillis((long)1485789600 * 1000);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        System.out.println(cal.getTime());
        System.out.println(new Date((long)1504848000 * 1000));

        System.out.println(Instant.ofEpochSecond(1485789600)
                .atZone(ZoneId.of("GMT")).equals(Instant.ofEpochMilli(cal.getTimeInMillis())));

    }
}