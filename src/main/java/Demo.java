import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.Recur;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.WeekDay;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.property.Action;
import net.fortuna.ical4j.model.property.Attendee;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.RRule;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.UidGenerator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

/**
 * created 2018/10/8
 * <p>
 * Demo for how to use ical to generate calender ics file.
 * <p>
 *
 *
 * @author yepengwei
 */
public class Demo {
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
            String summary = "S8全球总决赛赛程";
            // 开始时间
            DateTime start = new DateTime(1539158400000l);
            // 开始时间转换为UTC时间（UTC ＋ 时区差 ＝ 本地时间 ）
            start.setUtc(true);
            // 结束时间
            DateTime end = new DateTime(1541260800000l);
            // 结束时间设置成UTC时间（UTC ＋ 时区差 ＝ 本地时间 ）
            end.setUtc(true);
            // 新建普通事件
            // VEvent event = new VEvent(start, end, summary);
            // 定义全天事件（注意默认是UTC时间）
            VEvent event = new VEvent(new Date(1539158400000l), new Date(1539158400000l), summary);
            //事件坐标
            event.getProperties().add(new Location("釜山"));
            // 生成唯一标示
            event.getProperties().add(new Uid(new UidGenerator("iCal4j").generateUid().getValue()));
            // 添加时区信息
            event.getProperties().add(tz.getTimeZoneId());
            // 添加邀请者
            Attendee dev1 = new
                    Attendee(URI.create("mailto:45483388@qq.com"));
            dev1.getParameters().add(Role.REQ_PARTICIPANT);
            dev1.getParameters().add(new Cn("接口"));
            event.getProperties().add(dev1);
            // 重复事件
            Recur recur = new Recur(Recur.WEEKLY, Integer.MAX_VALUE);
            recur.getDayList().add(WeekDay.MO);
            recur.getDayList().add(WeekDay.TU);
            recur.getDayList().add(WeekDay.WE);
            recur.getDayList().add(WeekDay.TH);
            recur.getDayList().add(WeekDay.FR);
            RRule rule = new RRule(recur);
            event.getProperties().add(rule);
            // 提醒,提前10分钟
            VAlarm valarm = new VAlarm(new Dur(0, 0, -10, 0));
            valarm.getProperties().add(new Summary("Event Alarm"));
            valarm.getProperties().add(Action.DISPLAY);
            valarm.getProperties().add(new Description("RNG VS C9 at 20:00 pm"));
            // 将VAlarm加入VEvent
            event.getAlarms().add(valarm);
            // 添加事件
            calendar.getComponents().add(event);
            // 验证
            calendar.validate();
            fout = new FileOutputStream("/Users/didi/Didi/gits/jCalender/2.ics");
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
}
