 precision mediump float;
//纹理的具体数据
 uniform sampler2D u_TextureUnit;

 //纹理坐标st
 varying vec2 v_TextureCoordinates;
   void main() {
        //texture2D:根据纹理坐标st，取出具体的颜色值
        gl_FragColor = texture2D(u_TextureUnit,v_TextureCoordinates);
    }
