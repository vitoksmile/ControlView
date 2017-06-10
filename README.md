# ControlView 
<h1 align="center">
<img src="/.github/screenshot1.jpg" width="270" height="480" alt="Screenshot 1"/>
<img src="/.github/screenshot2.jpg" width="270" height="480" alt="Screenshot 2"/><br>
<img src="/.github/screenshot3.jpg" width="270" height="480" alt="Screenshot 3"/>
<img src="/.github/screenshot4.jpg" width="270" height="480" alt="Screenshot 4"/>
</h1>

## Requirements
* SDK 10 and and higher

# Usage
## Easy to Use
Add the xml code:  
```xml
<com.vitoksmile.library.view.controlview.ControlView
  xmlns:cv="http://schemas.android.com/apk/res-auto"
  android:id="@+id/controlView"
  android:layout_width="match_parent"
  android:layout_height="match_parent"
  cv:colorArc="@color/colorAccent"
  cv:colorBase="@color/colorPrimaryDark"
  cv:colorValueNeed="@color/colorAccent"
  cv:rangeMax="80"
  cv:rangeMin="20"
  cv:text="%"
  cv:value="34"
  cv:valueNeed="44" />
```

Add this to initialize the ControlView:
```java
mControlView = (ControlView) findViewById(R.id.controlView);
```

Setters:
```java
mControlView.setRange(0, 100);
mControlView.setValue(60);
mControlView.setValueNeed(75);
mControlView.setText("%");
mControlView.setColor(Color.BLUE);
mControlView.setColorArc(Color.GRAY);
mControlView.setColorValueNeed(Color.WHITE);
```

or use ControlViewBuilder:
```java
new ControlViewBuilder()
  .range(140, 360)
  .value(179, 179)
  .text("P")
  .color(Color.YELLOW, Color.parseColor("#CCCC00"), Color.WHITE)
  .build(mControlView);
```

## Listeners
ControlView.OnControlValueChangeListener  
```java
@Override
public void onControlValueChanged(float value) {
}
```
