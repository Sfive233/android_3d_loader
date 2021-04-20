#version 300 es
precision mediump float;
layout (location = 0) out vec4 FragColor;

uniform sampler2D SourceImage;
uniform vec2 SourceImageTexelSize;

in vec3 FragPos;
in vec2 TexCoords;
in vec3 Normal;

float Rgb2Luma(vec3 color);
float QUALITY(int i);
vec2 InverseCoords(vec2 texcoords);
void main(){
    float EDGE_THRESHOLD_MIN = 0.0312;
    float EDGE_THRESHOLD_MAX = 0.125;
    int ITERATIONS = 5;

    // 边缘检测
    // .......
//    vec2 correctTexCoords = vec2(TexCoords.x, 1.0 - TexCoords.y);
    vec3 colorCenter = texture(SourceImage, InverseCoords(TexCoords)).rgb;
    float lumaCenter = Rgb2Luma(texture(SourceImage, InverseCoords(TexCoords)).rgb);
    float lumaDown   = Rgb2Luma(texture(SourceImage, InverseCoords(TexCoords) + (vec2( 0.0,  1.0) * SourceImageTexelSize)).rgb);
    float lumaUp     = Rgb2Luma(texture(SourceImage, InverseCoords(TexCoords) + (vec2( 0.0, -1.0) * SourceImageTexelSize)).rgb);
    float lumaLeft   = Rgb2Luma(texture(SourceImage, InverseCoords(TexCoords) + (vec2(-1.0,  0.0) * SourceImageTexelSize)).rgb);
    float lumaRight  = Rgb2Luma(texture(SourceImage, InverseCoords(TexCoords) + (vec2( 1.0,  0.0) * SourceImageTexelSize)).rgb);
    float lumaMin    = min(lumaCenter,min(min(lumaDown,lumaUp),min(lumaLeft,lumaRight)));
    float lumaMax    = max(lumaCenter,max(max(lumaDown,lumaUp),max(lumaLeft,lumaRight)));
    float lumaRange  = lumaMax - lumaMin;
    if(lumaRange < max(EDGE_THRESHOLD_MIN,lumaMax*EDGE_THRESHOLD_MAX)){
        FragColor = vec4(colorCenter, 1.0);
        return;
    }
//    FragColor = vec4(vec3(0.7, 0.7, 0), 1.0);

    // 水平垂直判断
    // ..........
    float lumaUpLeft    = Rgb2Luma(texture(SourceImage, InverseCoords(TexCoords) + (vec2(-1.0, -1.0) * SourceImageTexelSize)).rgb);
    float lumaDownLeft  = Rgb2Luma(texture(SourceImage, InverseCoords(TexCoords) + (vec2(-1.0,  1.0) * SourceImageTexelSize)).rgb);
    float lumaUpRight   = Rgb2Luma(texture(SourceImage, InverseCoords(TexCoords) + (vec2( 1.0, -1.0) * SourceImageTexelSize)).rgb);
    float lumaDownRight = Rgb2Luma(texture(SourceImage, InverseCoords(TexCoords) + (vec2( 1.0,  1.0) * SourceImageTexelSize)).rgb);

    float horizontalVal = abs((lumaUpLeft - lumaLeft) - (lumaLeft - lumaDownLeft)) +
                            2.0 * abs((lumaUp - lumaCenter) - (lumaCenter - lumaDown)) +
                            abs((lumaUpRight - lumaRight) - (lumaRight - lumaDownRight));
    float verticalVal = abs((lumaUpRight - lumaUp) - (lumaUp - lumaUpLeft)) +
                            2.0 * abs((lumaRight - lumaCenter) - (lumaCenter - lumaLeft)) +
                            abs((lumaDown - lumaDown) - (lumaDown - lumaDownLeft));
    bool isHorizontal = horizontalVal >= verticalVal;
    FragColor = isHorizontal? vec4(colorCenter, 1.0): vec4(0.0, 0.0, 1.0, 1.0);

//    // 当前的点是处于边缘的像素点的中点，现在要获取在边缘线之中的点，这里用纹理坐标来表示。
//    // .......................................................................
//    float luma1 = isHorizontal ? lumaDown : lumaLeft;// 左下
//    float luma2 = isHorizontal ? lumaUp : lumaRight;// 右上
//    float gradient1 = luma1 - lumaCenter;
//    float gradient2 = luma2 - lumaCenter;
//    bool is1Steepest = abs(gradient1) >= abs(gradient2);
//
//    // 供之后查找端点时使用
//    float gradientScaled = 0.25 * max(abs(gradient1), abs(gradient2));
//
//    float stepLength = isHorizontal ? SourceImageTexelSize.y : SourceImageTexelSize.x;
//    float lumaLocalAverage = 0.0;
//    if(is1Steepest){
//        stepLength = -stepLength;
//        lumaLocalAverage = 0.5 * (luma1 + lumaCenter);
//    }else{
//        lumaLocalAverage = 0.5 * (luma2 + lumaCenter);
//    }
//
//    vec2 currentUV = TexCoords;
//    if(isHorizontal){
//        currentUV.y += stepLength * 0.5;
//    }else{
//        currentUV.x += stepLength * 0.5;
//    }

    // 查找线段端点
    // ..........

}
float Rgb2Luma(vec3 color){
    return sqrt(dot(color, vec3(0.299, 0.587, 0.114)));
}
float QUALITY(int i){
    float array[] = float[12](1.5, 2.0, 2.0, 2.0, 2.0, 4.0, 8.0, 16.0, 32.0, 64.0, 128.0, 256.0);
    return array[i];
}
vec2 InverseCoords(vec2 texcoords){
    return vec2(texcoords.x, 1.0 - texcoords.y);
}