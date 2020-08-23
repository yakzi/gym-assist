package com.ziebajakub.gymassist.view.enums;

public enum DayOfWeek {
     MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

     public String getName() {
          return name().charAt(0) + name().substring(1).toLowerCase();
     }
}
