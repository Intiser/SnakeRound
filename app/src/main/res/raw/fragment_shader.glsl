
varying highp vec2 v_texCoord; // Texture Coordinate

uniform highp float radius;
uniform highp vec2 control_point;
uniform highp vec2 food_point;
uniform highp float border;

uniform highp float devicewidth;
uniform highp float deviceheight;

//float abs(float val){
//    if(val > 0) return val;
//    return (-1.0 * val);
//}

highp float getDistancePoint(){
    highp float dis1 = abs(control_point[0] - v_texCoord[0]);
    highp float dis2 = abs(control_point[1] - v_texCoord[1]);
    highp float dis =   dis1 * dis1  + dis2 * dis2 ;
    return dis;
}

highp float getDistanceFood(){
    highp float dis1 = abs(food_point[0] - v_texCoord[0]);
    highp float dis2 = abs(food_point[1] - v_texCoord[1]);
    highp float dis =   dis1 * dis1  + dis2 * dis2 ;
    return dis;
}


void main()
{

    vec4 color = vec4(0.0,0.0,0.0,1.0);
    if(v_texCoord[0] < border || v_texCoord[1] < border){
        color = vec4(1.0,1.0,1.0,1.0);
    }
    else if(v_texCoord[0] > devicewidth - border || v_texCoord[1] > deviceheight - border){
        color = vec4(1.0,1.0,1.0,1.0);
    }

    if(getDistancePoint() < radius*radius ){
        color = vec4(1.0,0,0,1.0);
    }
    else if(getDistanceFood() < radius * radius){
        color = vec4(1.0,0,0,1.0);
    }else if(getDistanceFood() < (radius+2.0)*(radius+2.0)){
        color = vec4(1.0,0.76,0.3,1.0);
    }


    gl_FragColor = color;

}



// Shader :  two pixel blending like pixaloop. looping is okay.
// loop is working for three points // of long distance
// direction re-edited
// higher Sigma
// lower difference
// set of points working
// different direction working initially
// array sized increased ( array[700] )
// freeze color added