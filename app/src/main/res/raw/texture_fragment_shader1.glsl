 precision mediump float;
//纹理的具体数据
 uniform sampler2D u_TextureUnit;
 //第二个纹理数据
  uniform sampler2D u_TextureUnit1;
 //纹理坐标st
 varying vec2 v_TextureCoordinates;
   void main() {
        //texture2D:根据纹理坐标st，取出具体的颜色值
      gl_FragColor = texture2D(u_TextureUnit1,v_TextureCoordinates)*texture2D(u_TextureUnit,v_TextureCoordinates);

    }
