package com.naddiaz.workshift.ui.decorators;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by NESTOR on 21/06/2015.
 */
public class Decorator implements DayViewDecorator {

    private Date date;

    private ShapeDrawable drawable;
    private DotSpan dotSpan;
    private ArrayList<Date> dateArrayList;

    public Decorator(String date) {
        DateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        try {
            this.date = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Decorator(ArrayList<Date> dateArrayList) {
        this.dateArrayList = dateArrayList;
    }

    @Override
    public boolean shouldDecorate(CalendarDay calendarDay) {
        return this.dateArrayList.contains(calendarDay.getDate()) ? true : false;
    }

    @Override
    public void decorate(DayViewFacade dayViewFacade) {
        if(this.drawable != null)
            dayViewFacade.setBackgroundDrawable(this.drawable);
        if(this.dotSpan != null)
            dayViewFacade.addSpan(this.dotSpan);
    }

    public Decorator generateBackgroundDrawable(String hexColorStart, String hexColorEnd) {
        final int r = 5;
        final float[] outerR = new float[] {r, r, r, r, r, r, r, r};
        final int color = Color.parseColor("#B3" + hexColorStart);
        final int colorEnd = (hexColorEnd == null) ? color : Color.parseColor("#B3" + hexColorEnd);

        RoundRectShape rr = new RoundRectShape(outerR, null, null);

        this.drawable = new ShapeDrawable(rr);
        this.drawable.setShaderFactory(new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {
                return new LinearGradient(0, 0, width, height, color, colorEnd, Shader.TileMode.CLAMP);
            }
        });
        return this;
    }

    public Decorator generateDotSpan(String hexColor) {
        final int color = Color.parseColor("#" + hexColor);
        this.dotSpan = new DotSpan(4,color);
        return this;
    }
}