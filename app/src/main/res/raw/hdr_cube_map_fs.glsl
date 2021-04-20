#version 300 es
precision highp float;
out vec3 FragColor;

uniform sampler2D HDRMap;

in vec3 LocalPos;

in vec2 Texcoords;

vec2 SampleSphericalMap(vec3 v){
    vec2 invAtan = vec2(0.1591, 0.3183);
    vec2 uv = vec2(atan(v.z, v.x), asin(v.y));
    uv *= invAtan;
    uv += 0.5;
    return uv;
}
void main(){
    vec2 uv = SampleSphericalMap(normalize(LocalPos));
    uv.y = 1.0 - uv.y;

    vec3 result = texture(HDRMap, uv).rgb;

    result = clamp(result, 0.0, 5.0);// todo 尝试使用clamp函数将HDR亮度控制在一定范围，防止因亮度过高出现亮斑的问题。

    FragColor = result;
}
