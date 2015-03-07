package com.surv.ui123;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

public class PieActivity {
 
public Intent execute(Context context) {
int[] c = new int[] { Color.RED, Color.YELLOW, Color.BLUE,Color.GREEN,Color.GRAY};

String op[]=FragPolls.ops1.split("~#%&");
String rp[]=FragPolls.res1.split(",");
int colors[]=new int[op.length];
for(int j=0;j<op.length;j++)
{
	colors[j]=c[j];
}
DefaultRenderer renderer = buildCategoryRenderer(colors);
CategorySeries categorySeries = new CategorySeries("Poll Results");


for(int i=0;i<op.length;i++)
{
double per=	(Integer.parseInt(rp[i])/FragPolls.nrpl)*100;
categorySeries.add(op[i]+" ("+per+")",per);
}
return ChartFactory.getPieChartIntent(context, categorySeries, renderer, null);
}
 
protected DefaultRenderer buildCategoryRenderer(int[] colors) {
DefaultRenderer renderer = new DefaultRenderer();
for (int color : colors) {
SimpleSeriesRenderer r = new SimpleSeriesRenderer();
r.setColor(color);
renderer.addSeriesRenderer(r);
}
return renderer;
}
}