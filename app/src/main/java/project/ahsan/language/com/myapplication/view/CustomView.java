package project.ahsan.language.com.myapplication.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import project.ahsan.language.com.myapplication.model.Point;
import project.ahsan.language.com.myapplication.renderer.CustomRenderer;
import project.ahsan.language.com.myapplication.utility.ViewUtils;

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


    private void iniRenderer(){
        setEGLContextClientVersion(2);
        float width =  mContext.getResources().getDisplayMetrics().widthPixels;
        float height = (float) (mContext.getResources().getDisplayMetrics().heightPixels - ViewUtils.getPixelsFromDP(mContext, 10));
        customRenderer = new CustomRenderer(mContext, width, height);
        setRenderer(customRenderer);

    }

    public void setPointToRenderer(Point point){
        customRenderer.setPoint(point);
    }

}
