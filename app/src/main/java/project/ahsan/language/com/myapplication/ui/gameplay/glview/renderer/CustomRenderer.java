package project.ahsan.language.com.myapplication.ui.gameplay.glview.renderer;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


import project.ahsan.language.com.myapplication.R;
import project.ahsan.language.com.myapplication.ui.gameplay.glview.model.Point;
import project.ahsan.language.com.myapplication.ui.gameplay.glview.utility.RawResourceReader;

public class CustomRenderer implements GLSurfaceView.Renderer {

    private final Context mActivityContext;

    // uniform variable of shader files
    private Point point;
    private Point foodPoint;
    private float radius;
    private float border;

    // all handles
    int programHandle;
    private int mPositionHandle;
    private int mTextureCordinateHandle;
    private int mPointHandle;
    private int mFoodPointHandle;
    private int mRadiusHandle;
    private int mBorderHandle;
    private int mWidthHandle;
    private int mHeightHandle;

    // data sizes
    private final int mPositionDataSize = 3;
    private final int mTextureCoordinateDataSize = 2;
    private final int mBytesPerFloat = 4;

    // matrixes
    private float[] mViewMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];

    // data buffers
    private final FloatBuffer mSquareTextures;
    private final FloatBuffer mSquarePositions;

    // point datas
    float[] squarePositionData = {
            -1.0f, 1.0f, 0.0f,
            -1.0f, -1.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            1.0f, -1.0f, 0.0f,
    };

    float[] sqaureTextureData = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
    };

    // internal variables
    private float screenWidth;
    private float screenHeight;
    private float vibrateCount = 0;
    private float vibratePixel = 3;



    public CustomRenderer(Context context, float width, float height) {
        mActivityContext = context;
        point = new Point(width / 2, height / 2);
        foodPoint = new Point(width / 4, height / 4);
        radius = (float) 50;
        border = (float) 10.0;
        this.screenHeight = height;
        this.screenWidth = width;

        sqaureTextureData = new float[]{
                0.0f, 0.0f,
                0.0f, height,
                width, 0.0f,
                width, height,
        };

        mSquarePositions = ByteBuffer.allocateDirect(squarePositionData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mSquarePositions.put(squarePositionData).position(0);

        mSquareTextures = ByteBuffer.allocateDirect(sqaureTextureData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mSquareTextures.put(sqaureTextureData).position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 1.0f, 0.5f);

        // Use culling to remove back faces.
//        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glDisable(GLES20.GL_CULL_FACE);

        // Enable depth testing
//        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);

        GLES20.glEnable(GLES20.GL_BLEND);

        //GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE);
        //GLES20.glBlendFunc(GLES20.GL_DST_COLOR, GLES20.GL_ZERO);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        // Position the eye in front of the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 1.6f;

        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

        final String vertexShader = getVertexShader();

        final String fragmentShader = getFragmentShader();

        // Load in the vertex shader.
        int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);

        if (vertexShaderHandle != 0) {
            // Pass in the shader source.
            GLES20.glShaderSource(vertexShaderHandle, vertexShader);

            // Compile the shader.
            GLES20.glCompileShader(vertexShaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0) {
                GLES20.glDeleteShader(vertexShaderHandle);
                vertexShaderHandle = 0;
            }
        }

        if (vertexShaderHandle == 0) {
            throw new RuntimeException("Error creating vertex shader.");
        }

        // Load in the fragment shader shader.
        int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

        if (fragmentShaderHandle != 0) {
            // Pass in the shader source.
            GLES20.glShaderSource(fragmentShaderHandle, fragmentShader);

            // Compile the shader.
            GLES20.glCompileShader(fragmentShaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(fragmentShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0) {
                GLES20.glDeleteShader(fragmentShaderHandle);
                fragmentShaderHandle = 0;
            }
        }

        if (fragmentShaderHandle == 0) {
            throw new RuntimeException("Error creating fragment shader.");
        }

        // Create a program object and store the handle to it.
        programHandle = GLES20.glCreateProgram();

        if (programHandle != 0) {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(programHandle, vertexShaderHandle);

            // Bind the fragment shader to the program.
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);

            // Bind attributes
            GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
            //GLES20.glBindAttribLocation(programHandle, 1, "a_Color");
            GLES20.glBindAttribLocation(programHandle, 2, "a_TexCoordinate");
            //GLES20.glBindAttribLocation(programHandle, 3, "a_TexCoordinate2");


            // Link the two shaders together into a program.
            GLES20.glLinkProgram(programHandle);

            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            // If the link failed, delete the program.
            if (linkStatus[0] == 0) {
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }

        checkGlError(" program ");

        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        mTextureCordinateHandle = GLES20.glGetAttribLocation(programHandle, "a_TexCoordinate");
        mPointHandle = GLES20.glGetUniformLocation(programHandle, "control_point");
        mFoodPointHandle = GLES20.glGetUniformLocation(programHandle, "food_point");
        mRadiusHandle = GLES20.glGetUniformLocation(programHandle, "radius");
        mBorderHandle = GLES20.glGetUniformLocation(programHandle, "border");
        mWidthHandle = GLES20.glGetUniformLocation(programHandle, "devicewidth");
        mHeightHandle = GLES20.glGetUniformLocation(programHandle, "deviceheight");

        GLES20.glUseProgram(programHandle);

    }

    private void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("TAG", op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height);

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 10.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glUniform1f(mRadiusHandle, radius);
        GLES20.glUniform1f(mBorderHandle, border);
        GLES20.glUniform1f(mWidthHandle, screenWidth);
        GLES20.glUniform1f(mHeightHandle, screenHeight);

        float[] pos1 = {(float) point.getX() + vibratePixel, (float) point.getY()};
        GLES20.glUniform2fv(mPointHandle, 0, pos1, 0);
        float[] pos2 = {(float) foodPoint.getX(), (float) foodPoint.getY()};
        GLES20.glUniform2fv(mFoodPointHandle, 0, pos2, 0);

        drawSquare();
        GLES20.glFinish();
        //vibrateCount++;
        //if(vibrateCount == 2){
            vibratePixel = vibratePixel * (-1);
       // }
    }

    private void drawSquare() {

        mSquarePositions.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
                0, mSquarePositions);

        GLES20.glEnableVertexAttribArray(mPositionHandle);


        mSquareTextures.position(0);
        GLES20.glVertexAttribPointer(mTextureCordinateHandle, mTextureCoordinateDataSize, GLES20.GL_FLOAT, false,
                0, mSquareTextures);

        GLES20.glEnableVertexAttribArray(mTextureCordinateHandle);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }

    protected String getVertexShader() {
        return RawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.vertex_shader);
    }

    protected String getFragmentShader() {
        return RawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.fragment_shader);
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public void setFoodPoint(Point point) {
        this.foodPoint = point;
    }

    public void setRadius(double radius) {
        this.radius = (float) radius;
    }

    public void setBorder(double border){
        this.border = (float) border;
    }

}
