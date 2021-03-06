package com.example.fred_liu.pulsr.Home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.example.fred_liu.pulsr.R;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

public class DecoviewFragment extends Fragment{

    private DecoView mDecoView,mDecoView2,mDecoView3;
    private int mBackIndex;
    private int mBackIndex2;
    private int mBackIndex3;

    private int mSeries1Index;
    private int mSeries2Index;
    private int mSeries3Index;
    private final float mSeriesMax = 50f;
    TextView textPercentage;
    TextView textRemaining;
    TextView textActivity1;
    TextView textActivity2;
    TextView textActivity3;
    Button demo1, demo2, clear;
    float percentFilled = 0;
    float remainingMins = 0;
    float remainingKm = 0;
    float remainingCals = 0;

    float time = 30;
    float distance = 3.8f;
    float cals = 400;

    public DecoviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_decoview, container, false);

        mDecoView = view.findViewById(R.id.dynamicArcView);
        mDecoView2 = view.findViewById(R.id.dynamicArcView2);
        mDecoView3 = view.findViewById(R.id.dynamicArcView3);

        textPercentage = view.findViewById(R.id.textPercentage);
        textRemaining = view.findViewById(R.id.textRemaining);
        textActivity1 = view.findViewById(R.id.textActivity1);
        textActivity2 = view.findViewById(R.id.textActivity2);
        textActivity3 = view.findViewById(R.id.textActivity3);
        demo1 = view.findViewById(R.id.demo1);
        demo2 = view.findViewById(R.id.demo2);
        clear = view.findViewById(R.id.clear);

        demo1.setVisibility(View.GONE);
        clear.setVisibility(View.GONE);
        demo2.setText("DEMO");

        demo1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                demo1();
            }
        });

        demo2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (demo2.getText() == "DEMO") {
                    demo1.setVisibility(View.VISIBLE);
                    clear.setVisibility(View.VISIBLE);
                    demo2.setText("100%");
                }
                else if(demo2.getText() == "100%") {
                    demo2();
                    demo1.setEnabled(false);
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clear();
                demo1.setVisibility(View.GONE);
                clear.setVisibility(View.GONE);
                demo2.setText("DEMO");
                demo1.setEnabled(true);
            }
        });


        if(getArguments() != null) {
            String strTime = getArguments().getString("time");
            time = Float.parseFloat(strTime);

            String strDistance = getArguments().getString("distance");
            distance = Float.parseFloat(strDistance);

            String strCals = getArguments().getString("cals");
            cals = Float.parseFloat(strCals);
        }



        // Create required data series on the DecoView
        createBackSeries();
        createDataSeries3();
        createDataSeries2();
        createDataSeries1();

        // Setup events to be fired on a schedule
        createEvents();

        return view;
    }


    private void createBackSeries() {
        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFE2E2E2"))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(true)
                .build();

        mBackIndex = mDecoView.addSeries(seriesItem);

        SeriesItem seriesItem2 = new SeriesItem.Builder(Color.parseColor("#FFE2E2E2"))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(true)
                .build();

        mBackIndex2 = mDecoView2.addSeries(seriesItem2);

        SeriesItem seriesItem3 = new SeriesItem.Builder(Color.parseColor("#FFE2E2E2"))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(true)
                .build();

        mBackIndex3 = mDecoView3.addSeries(seriesItem3);
    }

    private void createDataSeries1() {
        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFFF8800"))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(false)
                .build();

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                textActivity1.setText(String.format("%.0f mins", currentPosition));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        mSeries1Index = mDecoView.addSeries(seriesItem);
    }

    private void createDataSeries2() {
        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#78BE20"))
                .setRange(0, 600, 0)
                .setInitialVisibility(false)
                .build();


        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                textActivity2.setText(String.format("%.0f cals", currentPosition));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        mSeries2Index = mDecoView2.addSeries(seriesItem);
    }

    private void createDataSeries3() {
        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FF6699FF"))
                .setRange(0, 5, 0)
                .setInitialVisibility(false)
                .build();


        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                textActivity3.setText(String.format("%.2f km", currentPosition));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });


        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                percentFilled = ((currentPosition - seriesItem.getMinValue()) / (seriesItem.getMaxValue() - seriesItem.getMinValue()));
                textPercentage.setText(String.format("%.0f%%", percentFilled * 100f));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });


        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                remainingKm = 5 - currentPosition;

                textRemaining.setText(String.format("%.1f km to goal", remainingKm));

            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        mSeries3Index = mDecoView3.addSeries(seriesItem);
    }


    private void createEvents() {
        mDecoView.executeReset();

        mDecoView.addEvent(new DecoEvent.Builder(mSeriesMax)
                .setIndex(mBackIndex)
                .setDuration(3000)
                .setDelay(100)
                .build());
        mDecoView2.addEvent(new DecoEvent.Builder(mSeriesMax)
                .setIndex(mBackIndex2)
                .setDuration(3000)
                .setDelay(100)
                .build());
        mDecoView3.addEvent(new DecoEvent.Builder(mSeriesMax)
                .setIndex(mBackIndex3)
                .setDuration(3000)
                .setDelay(100)
                .build());


        mDecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(mSeries3Index)
                .setDuration(2000)
                .setDelay(1250)
                .build());
        mDecoView2.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(mSeries2Index)
                .setDuration(2000)
                .setDelay(1250)
                .build());
        mDecoView3.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(mSeries1Index)
                .setDuration(2000)
                .setDelay(1250)
                .build());
    }

    private void demo1() {

        mDecoView.addEvent(new DecoEvent.Builder(time)
                .setIndex(mSeries1Index)
                .setDelay(100)
                .build());
        mDecoView2.addEvent(new DecoEvent.Builder(cals)
                .setIndex(mSeries2Index)
                .setDelay(100)
                .build());
        mDecoView3.addEvent(new DecoEvent.Builder(distance)
                .setIndex(mSeries3Index)
                .setDelay(100)
                .build());
    }

    private void demo2() {

        mDecoView.addEvent(new DecoEvent.Builder(mSeriesMax).setIndex(mSeries1Index).setDelay(100).setDuration(3000).build());

        mDecoView2.addEvent(new DecoEvent.Builder(600).setIndex(mSeries2Index).setDelay(100).setDuration(3000).build());

        mDecoView3.addEvent(new DecoEvent.Builder(5)
                .setIndex(mSeries3Index)
                .setDelay(100)
                .setDuration(3000)
                .build());

        mDecoView3.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_EXPLODE)
                .setIndex(mSeries1Index)
                .setDelay(3500)
                .setDuration(3000)
                .build());

        mDecoView2.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_EXPLODE)
                .setIndex(mSeries1Index)
                .setDelay(3500)
                .setDuration(3000)
                .build());


        mDecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_EXPLODE)
                .setIndex(mSeries1Index)
                .setDelay(3500)
                .setDuration(3000)
                .setDisplayText("Nice Job!")
                .setListener(new DecoEvent.ExecuteEventListener() {
                    @Override
                    public void onEventStart(DecoEvent decoEvent) {
                        resetText();
                    }

                    @Override
                    public void onEventEnd(DecoEvent decoEvent) {
                        mDecoView.addEvent(new DecoEvent.Builder(mSeriesMax)
                                .setIndex(mBackIndex)
                                .setDuration(3000)
                                .setDelay(100)
                                .build());
                        mDecoView2.addEvent(new DecoEvent.Builder(mSeriesMax)
                                .setIndex(mBackIndex2)
                                .setDuration(3000)
                                .setDelay(100)
                                .build());
                        mDecoView3.addEvent(new DecoEvent.Builder(mSeriesMax)
                                .setIndex(mBackIndex3)
                                .setDuration(3000)
                                .setDelay(100)
                                .build());

                        mDecoView.addEvent(new DecoEvent.Builder(mSeriesMax)
                                .setIndex(mSeries1Index)
                                .setDelay(110)
                                .build());
                        mDecoView2.addEvent(new DecoEvent.Builder(600)
                                .setIndex(mSeries2Index)
                                .setDelay(120)
                                .build());
                        mDecoView3.addEvent(new DecoEvent.Builder(5)
                                .setIndex(mSeries3Index)
                                .setDelay(130)
                                .build());

                    }
                })
                .build());
    }

    private void clear(){
        mDecoView.addEvent(new DecoEvent.Builder(0)
                .setIndex(mSeries1Index)
                .setDelay(0)
                .setDuration(0)
                .build());
        mDecoView2.addEvent(new DecoEvent.Builder(0)
                .setIndex(mSeries2Index)
                .setDelay(0)
                .setDuration(0)
                .build());
        mDecoView3.addEvent(new DecoEvent.Builder(0)
                .setIndex(mSeries3Index)
                .setDelay(0)
                .setDuration(0)
                .build());
    }

    private void resetText() {
//        textActivity1.setText("");
//        textActivity2.setText("");
//        textActivity3.setText("");
        textPercentage.setText("");
        textRemaining.setText("");
    }

    public interface OnFragmentInteractionListener {
    }
}
