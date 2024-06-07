 attribute vec4 a_Position;
 //具有2分量的s t 纹理坐标
 attribute vec2 a_TextureCoordinates;

 varying vec2 v_TextureCoordinates;

 uniform mat4 u_Matrix;

  void main() {
      gl_Position =  u_Matrix * a_Position;
      v_TextureCoordinates = a_TextureCoordinates;
   }
