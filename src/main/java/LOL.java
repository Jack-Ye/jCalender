import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.Action;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.UidGenerator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * created 2018/10/8
 * <p>
 * Generate LOL S series game schedule calender.
 * <p>
 *
 * @author yepengwei
 */
public class LOL {
    public static void exportFile() {

        FileOutputStream fout = null;
        try {
            // 创建一个时区（TimeZone）
            TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
            TimeZone timezone = registry.getTimeZone("Asia/Shanghai");
            VTimeZone tz = timezone.getVTimeZone();

            // 创建日历
            Calendar calendar = new Calendar();
            calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
            calendar.getProperties().add(Version.VERSION_2_0);
            calendar.getProperties().add(CalScale.GREGORIAN);

            // 时间主题
            String summary = "S8全球总决赛 小组赛BO1:";

            List<Game> list = castS8Schedules();

            for (Game game : list) {
                // 开始时间
                DateTime start = new DateTime(game.startTime);
                // 开始时间转换为UTC时间（UTC ＋ 时区差 ＝ 本地时间 ）
                start.setUtc(true);
                // 结束时间
                DateTime end = new DateTime(game.endTime);
                // 结束时间设置成UTC时间（UTC ＋ 时区差 ＝ 本地时间 ）
                end.setUtc(true);

                // 新建普通事件
                VEvent event = new VEvent(start, end, summary + game.title);
                //事件坐标
                event.getProperties().add(new Location("釜山"));
                // 生成唯一标示
                event.getProperties().add(new Uid(new UidGenerator("iCal4j").generateUid().getValue()));
                // 添加时区信息
                event.getProperties().add(tz.getTimeZoneId());
                // 提醒,提前10分钟
                VAlarm valarm = new VAlarm(new Dur(0, 0, -10, 0));
                valarm.getProperties().add(new Summary("Event Alarm"));
                valarm.getProperties().add(Action.DISPLAY);
                valarm.getProperties().add(new Description(game.title));
                // 将VAlarm加入VEvent
                event.getAlarms().add(valarm);
                // 添加事件
                calendar.getComponents().add(event);
                System.out.println(game.time + " " + game.title + " done");
            }
            // 验证
            calendar.validate();
            fout = new FileOutputStream("/Users/didi/Didi/gits/jCalender/lols8r1.ics");
            CalendarOutputter outputter = new CalendarOutputter();
            outputter.output(calendar, fout);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static List<Game> castS8Schedules() throws ParseException {
        List<Game> games = new ArrayList<Game>();
        //DAY 1
        games.add(new Game("KT VS TL", "2018-10-10 16:00"));
        games.add(new Game("EDG VS MAD", "2018-10-10 17:00"));
        games.add(new Game("PVB VS FW", "2018-10-10 18:00"));
        games.add(new Game("AFs VS G2", "2018-10-10 19:00"));
        games.add(new Game("RNG VS C9", "2018-10-10 20:00"));
        games.add(new Game("GEN VS VIT", "2018-10-10 21:00"));
        //DAY 2
        games.add(new Game("FW VS AFs", "2018-10-11 16:00"));
        games.add(new Game("PVB VS G2", "2018-10-11 17:00"));
        games.add(new Game("100T VS FNC", "2018-10-11 18:00"));
        games.add(new Game("IG VS GRex", "2018-10-11 19:00"));
        games.add(new Game("VIT VS C9", "2018-10-11 20:00"));
        games.add(new Game("GEN VS RNG", "2018-10-11 21:00"));
        //DAY 3
        games.add(new Game("MAD VS KT", "2018-10-12 16:00"));
        games.add(new Game("TL VS EDG", "2018-10-12 17:00"));
        games.add(new Game("FNC VS IG", "2018-10-12 18:00"));
        games.add(new Game("100T VS GRex", "2018-10-12 19:00"));
        games.add(new Game("RNG VS VIT", "2018-10-12 20:00"));
        games.add(new Game("C9 VS GEN", "2018-10-12 21:00"));
        //DAY 4
        games.add(new Game("AFs VS PVB", "2018-10-13 16:00"));
        games.add(new Game("G2 VS FW", "2018-10-13 17:00"));
        games.add(new Game("IG VS 100T", "2018-10-13 18:00"));
        games.add(new Game("GRex VS FNC", "2018-10-13 19:00"));
        games.add(new Game("TL VS MAD", "2018-10-13 20:00"));
        games.add(new Game("KT VS EDG", "2018-10-13 21:00"));
        //DAY 5
        games.add(new Game("VIT VS RNG", "2018-10-14 16:00"));
        games.add(new Game("GEN VS C9", "2018-10-14 17:00"));
        games.add(new Game("VIT VS GEN", "2018-10-14 18:00"));
        games.add(new Game("C9 VS RNG", "2018-10-14 19:00"));
        games.add(new Game("C9 VS VIT", "2018-10-14 20:00"));
        games.add(new Game("RNG VS GEN", "2018-10-14 21:00"));
        //DAY 6
        games.add(new Game("AFs VS FW", "2018-10-15 16:00"));
        games.add(new Game("G2 VS PVB", "2018-10-15 17:00"));
        games.add(new Game("FW VS G2", "2018-10-15 18:00"));
        games.add(new Game("PVB VS AFs", "2018-10-15 19:00"));
        games.add(new Game("FW VS PVB", "2018-10-15 20:00"));
        games.add(new Game("G2 VS AFs", "2018-10-15 21:00"));
        //DAY 7
        games.add(new Game("TL VS KT", "2018-10-16 16:00"));
        games.add(new Game("MAD VS EDG", "2018-10-16 17:00"));
        games.add(new Game("MAD VS TL", "2018-10-16 18:00"));
        games.add(new Game("EDG VS KT", "2018-10-16 19:00"));
        games.add(new Game("EDG VS TL", "2018-10-16 20:00"));
        games.add(new Game("KT VS MAD", "2018-10-16 21:00"));
        //DAY 8
        games.add(new Game("FNC VS 100T", "2018-10-17 16:00"));
        games.add(new Game("GRex VS IG", "2018-10-17 17:00"));
        games.add(new Game("FNC VS GRex", "2018-10-17 18:00"));
        games.add(new Game("100T VS IG", "2018-10-17 19:00"));
        games.add(new Game("GRex VS 100T", "2018-10-17 20:00"));
        games.add(new Game("IG VS FNC", "2018-10-17 21:00"));

        return games;
    }

    static class Game {
        private String title;
        private Long startTime;
        private Long endTime;
        private String time;

        public Game(String title, String time) throws ParseException {
            this.title = title;
            this.time = time;

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            format.setLenient(false);
            format.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Shanghai"));
            long ts = format.parse(time).getTime();
            this.startTime = ts;
            this.endTime = ts + 3600000;
        }

        @Override
        public String toString() {
            return "Game{" +
                    "title='" + title + '\'' +
                    ", startTime=" + startTime +
                    ", endTime=" + endTime +
                    ", time='" + time + '\'' +
                    '}';
        }
    }

    public static void main(String[] args) throws ParseException {
        exportFile();
    }
}
