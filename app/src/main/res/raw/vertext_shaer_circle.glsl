attribute vec4 a_Position;//顶点位置

uniform mat4 u_Matrix;

void main() {
    gl_Position  =  u_Matrix * a_Position;;

}