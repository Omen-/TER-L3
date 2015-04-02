package com.saroty.ter.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.saroty.ter.R;
import com.saroty.ter.activities.MainActivity;
import com.saroty.ter.adapters.CoursesViewPagerAdapter;
import com.saroty.ter.schedule.Course;
import com.saroty.ter.schedule.Schedule;
import com.saroty.ter.tasks.AdaptScheduleTask;
import com.saroty.ter.time.DayOfWeek;
import com.saroty.ter.time.LocalTimeInterval;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by Romain on 31/03/2015.
 */
public class DayListFragment extends Fragment
{

    private CoursesViewPagerAdapter myViewPagerAdapter;
    private ViewPager mViewPager;
    private int mDay;
    private int mWeek;
    private Schedule mSchedule;

    public DayListFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.courses_view_pager, container, false);
        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);

        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();

        Log.v("debug romain","appele start");

        if(myViewPagerAdapter == null)
            myViewPagerAdapter =
                    new CoursesViewPagerAdapter(getFragmentManager(),this);
        else
            myViewPagerAdapter =
                    new CoursesViewPagerAdapter(getChildFragmentManager(),this);

        mViewPager.setAdapter(myViewPagerAdapter);

        AdaptScheduleTask a = new AdaptScheduleTask(getActivity().getApplicationContext());

        this.mWeek = 33;
        this.mDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        //TODO : A changer quand arthur aura fait son job, de meme pour le seet current schedule

        try {
            a.execute(new URL("https://celcatfsi.ups-tlse.fr/FSIpargroupes/g558.xml"));
            this.mSchedule = a.get();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ((MainActivity)getActivity()).getSchedules().add(this.mSchedule);
        ((MainActivity)getActivity()).setCurrentSchedule(this.mSchedule);

        //TODO : HARDCODE DU decal pour le jour souhaite pas top

        mViewPager.setCurrentItem(this.mDay+6-1);

    }

    @Override
    public void onStop(){
        super.onStop();
        Log.v("debug romain","appelle stop");
    }

    public int getmWeek()
    {
        return this.mWeek;
    }

    public void setmWeek(int week)
    {
        this.mWeek = week;
    }

    public int getmDay()
    {
        return this.mDay;
    }

    public void setmDay(int day)
    {
        this.mDay = day;
    }


}
