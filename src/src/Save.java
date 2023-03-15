import java.nio.file.Path;

public class Save {
    int year, month, day, hour, minute, second;
    Path path;
    Save(int year,int month,int day,int hour,int minute,int second, Path path) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.path = path;
    }


    public int getYear() {
        return this.year;
    }

    public int getMonth() {
        return this.month;
    }

    public int getDay() {
        return this.day;
    }

    public int getHour() {
        return this.hour;
    }

    public int getMinute() {
        return this.minute;
    }

    public int getSecond() {
        return this.second;
    }
    public Path getPath() {
        return this.path;
    }
    
}
