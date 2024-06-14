# OpenGLDemo
> openGL的学习工程

## 一、OpenGL ES 概念
### 1、着色语言 <https://blog.csdn.net/jeffasd/article/details/77989274>
* 1、基本数据类型
| 类型          | 说明                                                          |
|---------------|---------------------------------------------------------------|
| void          | 空类型，不返回任何值                                           |
| bool          | 布尔类型，true，false                                         |
| int           | 带符号的整数 (signed integer)                                 |
| float         | 带符号的浮点数                                                |
| vec2, vec3, vec4 | n维浮点数向量，包括2，3，4个元素的浮点型向量                  |
| bvec2, bvec3, bvec4 | n维布尔向量，包含2，3，4个元素的布尔向量                     |
| ivec2, ivec3, ivec4 | n维整形向量，包含2，3，4个元素的整形向量                     |
| mat2, mat3, mat4 | 2✖️2，3✖️3，4✖️4，浮点数矩阵                                 |
| sampler2D     | 2D纹理                                                        |
| samplerCube   | 立方体纹理                                                    |

* 2、基本结构和数组
| 类型     | 描述                                                         |
|----------|--------------------------------------------------------------|
| 结构     | struct type-name{} 类似C语言中的结构体                        |
| 数组     | float foo[3] GLSL只支持1维数组，数组可以是结构体的成员         |

* 3、变量限定符
| 修饰符       | 描述                                                                                                   |
|--------------|--------------------------------------------------------------------------------------------------------|
| none（默认） | 可省略，本地变量可读可写，函数的入参就是这种变量                                                       |
| const        | 声明变量或函数的参数为只读类型                                                                         |
| attribute    | 只能存在于vertex shader（顶点着色器），一般用于保存顶点或法线数据，可以在缓冲区中读取数据               |
| uniform      | 在运行时shader无法改变uniform变量，一般用来放置程序传递给shader的变换矩阵，材质，光照参数               |
| varying      | 主要负责vertex和fragment之间传递数据                                                                   |

* 4、参数限定符
> 函数的参数默认是以拷贝的形式传递的，也就是值传递，任何传递给函数的变量，其值都会拷贝一份，然后再交给函数内部进行处理，
> 我们也可以添加限定符，来达到引用传递的效果
| 类型 | 描述                                                                                                           |
|------|---------------------------------------------------------------------------------------------------------------|
| 默认 | 默认使用in限定符                                                                                              |
| in   | 真正传入函数的是参数的一份拷贝，在函数内修改参数值，不会影响参数变量本身                                        |
| out  | 参数的值不会传递给函数，但是在函数内部修改值，会在函数结束后参数的值会改变                                     |
| inout | 传入函数的参数是引用，函数内部修改值，参数也会改变                                                             |

* 5、函数
> GLSL允许在程序的最外部声明函数，函数不能嵌套，不能递归调用，且必须声明返回值类型(无返回值时返回void)，在其他方面与c函数一样
~~~c
vec4 getPosition(){
    vec4 v4 = vec4(0.,0.,0.,1.);
    return v4;
}

void doubleSize(inout float size){
    size= size*2.0  ;
}
void main() {
    float psize= 10.0;
    doubleSize(psize);
    gl_Position = getPosition();
    gl_PointSize = psize;
}
~~~

* 6、类型转换
> GLSL可以使用构造函数进行显示类型转换
~~~c
bool t= true;
bool f = false;

int a = int(t); //true转换为1或1.0
int a1 = int(f);//false转换为0或0.0

float b = float(t);
float b1 = float(f);

bool c = bool(0);//0或0.0转换为false
bool c1 = bool(1);//非0转换为true

bool d = bool(0.0);
bool d1 = bool(1.0);

~~~
* 7、精度限制
> GLSL在进行光栅化的时候，会进行大量的浮点运算，这些运算可能是设备不能承受的，所以GLSL提供了三种浮点精度，
> 可以根据不同的设备选择不同的精度

  在变量前加highp mediump lowp，即可对精度的声明
~~~c
lowp float color;
varying mediump vec2 Coord;
lowp ivec2 foo(lowp mat3);
highp mat4 m;
//默认精度
precision highp float;//默认高精度float
precision mediump int;//默认中精度int
~~~
* 8、invariant关键字
> 由于shader在编译的时候内部会做一些优化，可能导致同样的运算在不同的shader中结果不一致的问题，这会引起一些问题，
>尤其是在vertex shader向fragment shader传值的时候，所以我们需要使用invariant关键字要求计算结果必须一致，
>除了这个，我们也可以用#pragma STDGL invariant(all)命令来保证所有的输出一致，这样会限制编译器的优化程度，降低性能
~~~c
#pragma STDGL invariant(all) //所有输出变量为 invariant
invariant varying texCoord; //varying在传递数据的时候声明为invariant
~~~
* 9、内置的特殊变量
> GLSL使用一些特殊的内置变量与硬件进行沟通，他们大致分为俩种，一种是input类型，他们负责向硬件发送数据，另一种是output类型，负责向程序回传数据，方便编程需要
 * 在vertex shader中 output的内置变量
 | 变量                          | 描述                               |
 |-------------------------------|------------------------------------|
 | highp vec4 gl_Position        | gl_Position 放置顶点坐标信息       |
 | mediump float gl_PointSize    | gl_PointSize 绘制点的大小          |
 * 在fragment shader中
    input类型的内置变量
| 变量                          | 描述                                                           |
|-------------------------------|----------------------------------------------------------------|
| highp vec4 gl_Position        | gl_Position 放置顶点坐标信息                                   |
| mediump float gl_PointSize    | gl_PointSize 绘制点的大小                                      |
| mediump vec4 gl_FragCoord     | 片元在framebuffer画面的相对位置                                |
| bool gl_FrontFacing           | 标志当前图元是不是正面图元的一部分                             |
| mediump vec2 gl_PointCoord    | 经过插值计算后的纹理坐标，点的范围是0.0到1.0                   |
     output类型内置变量
| 变量                          | 描述                                                           |
|-------------------------------|----------------------------------------------------------------|
| mediump vec4 gl_FragColor     | 设置当前片点的颜色，使用glDrawBuffers数据数组                   |
| mediump vec4 gl_FragData[n]   | 片元在framebuffer画面的相对位置                                |

## 二、OpenGL ES的使用方式
### 1、创建着色器
* 1.1、初始化 OpenGL ES
~~~java
verticeData =
            ByteBuffer.allocateDirect(tableVertices.size * BYTES_PER_FLOAT)//分配一块本地内存，分配大小由外部传入
                .order(ByteOrder.nativeOrder())//告诉缓冲区，按照本地字节序组织内容
                .asFloatBuffer()//我们希望操作Float，调用这个方法会返回FloatBuffer
                .put(tableVertices)//	填充数据
                .apply {
                    position(0)//把数据下标移动到指定位置
                }
~~~
* 1.2、创建顶点着色器 (vertex_shader.glsl)
~~~c
 attribute vec4 a_Position;
  void main() {
      gl_Position =  a_Position;//放置顶点坐标信息
      gl_PointSize=10.0;//绘制点的大小
   }
~~~
* 2、创建片段着色器 (fragment_shader.glsl)
~~~c
 precision mediump float;
 uniform vec4 u_Color;
   void main() {
        gl_FragColor = u_Color;//设置当前片点的颜色
    }
~~~
* 3、加载着色器
~~~java
 public static String readResoucetText(Context context, int resouceId) {
        StringBuffer body = new StringBuffer();

        try {
            InputStream inputStream = context.getResources().openRawResource(resouceId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String nextline;
            while ((nextline = bufferedReader.readLine()) != null) {
                body.append(nextline);
                body.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return body.toString();
    }
~~~
* 4、加载顶点着色器与片段着色器
~~~java
        //读取着色器源码
        String fragment_shader_source = ReadResouceText.readResoucetText(mContext, R.raw.fragment_shader);
        String vertex_shader_source = ReadResouceText.readResoucetText(mContext, R.raw.vertex_shader);
~~~
* 5、编译着色器
~~~java
 /**
 * @param type   着色器类型 GLES20.GL_VERTEX_SHADER  GLES20.GL_FRAGMENT_SHADER
 * @param source 地址 顶点着色器或者片段着色器的源码
 */
 public static int compileShader(int type, String source) {
        //创建shader
        int shaderId = GLES20.glCreateShader(type);
        if (shaderId == 0) {
            Log.d("mmm", "创建shader失败");
            return 0;
        }
        //上传shader源码
        GLES20.glShaderSource(shaderId, source);
        //编译shader源代码
        GLES20.glCompileShader(shaderId);
        //取出编译结果
        int[] compileStatus = new int[1];
        //取出shaderId的编译状态并把他写入compileStatus的0索引
        GLES20.glGetShaderiv(shaderId, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        Log.d("mmm编译状态", GLES20.glGetShaderInfoLog(shaderId));

        if (compileStatus[0] == 0) {
            GLES20.glDeleteShader(shaderId);
            Log.d("mmm", "创建shader失败");
            return 0;
        }

        return shaderId;
    }

~~~
~~~java
        //编译顶点着色器
        int vertex_shader = ShaderUtil.compileShader(GLES20.GL_VERTEX_SHADER, vertex_shader_source);
        //编译片段着色器
        int fragment_shader = ShaderUtil.compileShader(GLES20.GL_FRAGMENT_SHADER, fragment_shader_source);
~~~

* 6、链接着色器
~~~java
 public static int linkProgram(int mVertexshader, int mFragmentshader) {
        //创建程序对象
        int programId = GLES20.glCreateProgram();
        if (programId == 0) {
            Log.d("mmm", "创建program失败");
            return 0;
        }
        //依附着色器
        GLES20.glAttachShader(programId, mVertexshader);
        GLES20.glAttachShader(programId, mFragmentshader);
        //链接程序
        GLES20.glLinkProgram(programId);
        //检查链接状态
        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(programId, GLES20.GL_LINK_STATUS, linkStatus, 0);
        Log.d("mmm", "链接程序" + GLES20.glGetProgramInfoLog(programId));
        if (linkStatus[0] == 0) {
            GLES20.glDeleteProgram(programId);
            Log.d("mmm", "链接program失败");
            return 0;
        }

        return programId;

    }
~~~
~~~java
        //链接程序
        mProgram = ShaderUtil.linkProgram(vertex_shader, fragment_shader);
~~~

* 7、验证openGl程序
~~~java
 public static boolean validateProgram(int programId) {
        GLES20.glValidateProgram(programId);
        int[] validateStatus = new int[1];
        GLES20.glGetProgramiv(programId, GLES20.GL_VALIDATE_STATUS, validateStatus, 0);
        Log.d("mmm", "验证程序" + GLES20.glGetProgramInfoLog(programId));
        return validateStatus[0] != 0;
    }
~~~
* 8、使用openGL程序
~~~java
        //使用程序
        GLES20.glUseProgram(mProgram);
~~~

* 9、获取着色器中的变量
~~~java
//属性位置 “a_Position”
 public static int getAttribLocation(int programId, String name) {
        return GLES20.glGetAttribLocation(programId, name);
    }
//uniform位置 “u_Color”
    public static int getUniformLocation(int programId, String name) {
        return GLES20.glGetUniformLocation(programId, name);
    }
~~~

10、设置顶点数据
~~~java
 //绑定a_position和verticeData顶点位置
        /**
         * 第一个参数，这个就是shader属性
         * 第二个参数，每个顶点有多少分量，我们这个只有来个分量
         * 第三个参数，数据类型
         * 第四个参数，只有整形才有意义，忽略
         * 第5个参数，一个数组有多个属性才有意义，我们只有一个属性，传0
         * 第六个参数，opengl从哪里读取数据
         */
         verticeData.position(0);

        GLES20.glVertexAttribPointer(a_position, 2, GLES20.GL_FLOAT,
                false, 0, verticeData);
         //使用顶点
       GLES20.glEnableVertexAttribArray(a_position);

~~~

11、绘制进行
~~~java
//设置着色器程序中 uniform 变量
/**
location：这是 uniform 变量在着色器程序中的位置索引。这个位置可以通过调用 glGetUniformLocation() 获取，
需要提供着色器程序的 ID 和 uniform 变量的名称。
v0, v1, v2, v3：这些是要设置的 uniform 变量的四个浮点数值。这些值通常用于传递如颜色（RGBA），向量和小型数组等数据。
*/
 GLES20.glUniform4f(location, v0, v1, v2, v3);

//绘制图形
/**
GLES20.GL_POINTS：每个顶点作为一个点绘制。
GLES20.GL_LINES：每两个顶点形成一条线段。
GLES20.GL_LINE_STRIP：一系列连接的线段，依次连接每个顶点。
GLES20.GL_LINE_LOOP：与 GL_LINE_STRIP 类似，但是会在最后一个顶点和第一个顶点之间自动添加一条线段，形成一个闭环。
GLES20.GL_TRIANGLES：每三个顶点组成一个三角形。
GLES20.GL_TRIANGLE_STRIP：顶点数组中的每个顶点序列都与前两个顶点一起组成一个三角形。
GLES20.GL_TRIANGLE_FAN：第一个顶点和随后的所有顶点对组成的三角形序列。

first：指定从哪个顶点开始绘制，即顶点数组中的起始索引。
指定绘制顶点的数量。
*/
 GLES20.glDrawArrays(mode, first, count);

/**
location：uniform变量的位置。这个位置可以通过 glGetUniformLocation(program, name) 获取，其中 program 是着色器程序的句柄，而 name 是着色器中定义的uniform变量名。
count：要修改的矩阵数量。这通常用于传递矩阵数组，对于单个矩阵，这个值应该设置为1。
transpose：指示是否应该转置矩阵的布尔值。GLSL期望矩阵以列主序方式存储，如果你的矩阵是行主序存储的，则需要设置此参数为true来让OpenGL ES自动转置。但在实践中，大多数时候，你应该直接传递列主序矩阵，并将此参数设置为false。
value：包含一个或多个矩阵的浮点数组。
offset：value数组中的起始索引，从该位置开始读取矩阵数据。
*/
 void glUniformMatrix4fv(int location, int count, boolean transpose, float[] value, int offset)

~~~

## 三、OpenGL ES的API 说明





















