package project.ahsan.language.com.myapplication.ui.gameplay.glview;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import project.ahsan.language.com.myapplication.ui.gameplay.glview.model.Point;
import project.ahsan.language.com.myapplication.ui.gameplay.glview.renderer.CustomRenderer;
import project.ahsan.language.com.myapplication.ui.gameplay.glview.utility.ViewUtils;

public class CustomView extends GLSurfaceView {

    CustomRenderer customRenderer;
    Context mContext;


    public CustomView(Context context) {
        super(context);
        mContext = context;
        iniRenderer();
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        iniRenderer();
    }


    private void iniRenderer() {
        setEGLContextClientVersion(2);
        //setEGLConfigChooser(8 , 8, 8, 8, 16, 0);
        float width = mContext.getResources().getDisplayMetrics().widthPixels;
        float height = (float) (mContext.getResources().getDisplayMetrics().heightPixels - ViewUtils.getPixelsFromDP(mContext,16));
        customRenderer = new CustomRenderer(mContext, width, height);
        setRenderer(customRenderer);

    }

    public void setPointsToRenderer(Point point) {
        customRenderer.setPoint(point);

    }

    public void setFoodPointsToRenderer(Point point) {
        customRenderer.setFoodPoint(point);

    }

    public void setRadiusOfFood(double radius){
        customRenderer.setRadius(radius);
    }

    public void setBorder(double border){

    }

}
