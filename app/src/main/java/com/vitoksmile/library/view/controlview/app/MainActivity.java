package com.vitoksmile.library.view.controlview.app;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import com.vitoksmile.library.builder.controlview.ControlViewBuilder;
import com.vitoksmile.library.view.controlview.ControlView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        ControlView.OnControlValueChangeListener {

    private ControlView mControlView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mControlView = (ControlView) findViewById(R.id.controlView);

        mControlView.setOnControlValueChangeListener(this, true);

        findViewById(R.id.btnHumidity).setOnClickListener(this);
        findViewById(R.id.btnTemperature).setOnClickListener(this);
        findViewById(R.id.btnOther).setOnClickListener(this);
    }

    @Override
    public void onControlValueChanged(float value) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnHumidity:
                mControlView.setRange(0, 100);
                mControlView.setValue(60);
                mControlView.setValueNeed(75);
                mControlView.setText("%");
                mControlView.setColor(Color.BLUE);
                mControlView.setColorArc(Color.GRAY);
                mControlView.setColorValueNeed(Color.WHITE);
                break;

            case R.id.btnTemperature:
                mControlView.setRange(20, 29);
                mControlView.setValue(24);
                mControlView.setValueNeed(25);
                mControlView.setText(Html.fromHtml("&#x2103").toString());
                mControlView.setColor(Color.RED);
                mControlView.setColorArc(Color.parseColor("#CC0000"));
                mControlView.setColorValueNeed(Color.WHITE);
                break;

            case R.id.btnOther:
                new ControlViewBuilder()
                        .range(140, 360)
                        .value(179, 179)
                        .text("P")
                        .color(Color.YELLOW, Color.parseColor("#CCCC00"), Color.WHITE)
                        .build(mControlView);
                break;
        }
    }
}
