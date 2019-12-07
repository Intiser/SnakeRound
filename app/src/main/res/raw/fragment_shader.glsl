
varying highp vec2 v_texCoord; // Texture Coordinate

uniform highp float radius;
uniform highp vec2 point;

//float abs(float val){
//    if(val > 0) return val;
//    return (-1.0 * val);
//}

highp float getDistance(){
    highp float dis1 = abs(point[0] - v_texCoord[0]);
    highp float dis2 = abs(point[1] - v_texCoord[1]);
    highp float dis =   dis1 * dis1  + dis2 * dis2 ;
    return dis;
}

void main()
{

    vec4 color = vec4(0.0,1.0,0.0,1.0);
    if(getDistance() < radius*radius  ){
        color = vec4(1.0,0,0,1.0);
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