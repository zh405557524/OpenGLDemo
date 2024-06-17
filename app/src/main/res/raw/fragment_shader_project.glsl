precision highp float;

 uniform lowp sampler2D         unit2d;
 uniform vec4        light_positionEye;
 uniform lowp vec4   light_diffuseColor;

 varying highp   vec4 v_positionEye;
 varying highp   vec3 v_normalEye;
 varying highp   vec3 v_texCoord;

 vec4 baseLightingColor = vec4(0.04, 0.04, 0.04, 2);
 lowp vec4 materialDiffuseColor = vec4(0.8, 0.8, 0.8, 1);
 lowp vec4 ambientTerm = vec4(0, 0, 0, 1);

 void main()
{
    lowp vec4 color;
    vec4 diffuseTerm;
    vec3 vertexToLightVec;
    highp float nDotL;

    color = baseLightingColor;

    vertexToLightVec = normalize(light_positionEye.xyz - v_positionEye.xyz);
    nDotL = max(dot(v_normalEye, vertexToLightVec), 0.6);
    diffuseTerm = nDotL * materialDiffuseColor * light_diffuseColor;
    color += (ambientTerm + diffuseTerm);

    color.a = materialDiffuseColor.a;
    color = clamp(color, 0.0, 1.0);

    lowp vec4 texColor;
    texColor = texture2D(unit2d, v_texCoord.st);
    color *= texColor;

    gl_FragColor = color;
}