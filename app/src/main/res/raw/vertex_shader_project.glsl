precision highp float;

 attribute         vec4 a_position;
 attribute         vec3 a_texCoord0;
 attribute         vec3 a_normal;

 uniform mat4    modelviewMatrix;
 uniform mat4    mvpMatrix;

 varying highp vec4    v_positionEye;
 varying highp vec3    v_normalEye;
 varying highp vec3    v_texCoord;

 highp vec3 normalEye;
 highp vec4 positionEye;

 void main(void)
{
    vec3 normalizedNormal;

    normalEye = vec3(modelviewMatrix * vec4(a_normal, 0.0));
    positionEye = modelviewMatrix * a_position;

    v_texCoord = a_texCoord0;
    v_positionEye = positionEye;
    v_normalEye = normalEye;
    gl_Position = mvpMatrix * a_position;
}